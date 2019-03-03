package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.message.request.DiscountTicketClassDTO;
import edu.netcracker.backend.model.Discount;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.service.TicketClassService;
import edu.netcracker.backend.utils.DiscountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketClassServiceImpl implements TicketClassService {

    private final TicketClassDAO ticketClassDAO;

    private final DiscountDAO discountDAO;

    @Autowired
    public TicketClassServiceImpl(TicketClassDAO ticketClassDAO, DiscountDAO discountDAO) {
        this.ticketClassDAO = ticketClassDAO;
        this.discountDAO = discountDAO;
    }

    @Override
    public List<DiscountTicketClassDTO> getTicketClassesRelatedToCarrier(Number userId) {
        List<TicketClass> ticketClasses = ticketClassDAO.getAllTicketClassesRelatedToCarrier(userId);
        List<Discount> discounts = discountDAO.findIn(ticketClasses.stream()
                .map(TicketClass::getDiscountId)
                .collect(Collectors.toList()));

        attachTicketClassesToDiscounts(ticketClasses, discounts);

        return createTicketClassDTOs(ticketClasses);
    }

    @Override
    public DiscountTicketClassDTO createDiscountForTicketClass(DiscountTicketClassDTO ticketClassDTO) {
        Optional<TicketClass> optionalTicketClass = ticketClassDAO.find(ticketClassDTO.getClassId());

        if (!optionalTicketClass.isPresent()) {
            throw new RequestException("Ticket class with id " + ticketClassDTO.getClassId() + " is null",
                    HttpStatus.NOT_FOUND);
        }

        if (ticketClassDTO.getDiscount() == null) {
            throw new RequestException("Discount is null", HttpStatus.BAD_REQUEST);
        }

        TicketClass ticketClass = optionalTicketClass.get();

        if (ticketClass.getDiscountId() != null) {
            throw new RequestException("Discount already exist",
                    HttpStatus.CONFLICT);
        }

        Discount discount = Discount.toDiscount(ticketClassDTO.getDiscount());

        discountDAO.save(discount);
        ticketClass.setDiscountId(discount.getDiscountId());
        ticketClass.setDiscount(discount);
        ticketClassDAO.save(ticketClass);

        return DiscountTicketClassDTO.toTicketClassDTO(ticketClass);
    }

    @Override
    public DiscountTicketClassDTO deleteDiscountForTicketClass(Number discountId, Number userId) {
        Optional<TicketClass> optionalTicketClass = ticketClassDAO.getTicketClassByDiscount(userId, discountId);

        if (!optionalTicketClass.isPresent()) {
            throw new RequestException("Discount not exist, or not allowed for this user",
                    HttpStatus.NOT_FOUND);
        }

        TicketClass ticketClass = optionalTicketClass.get();
        ticketClass.setDiscountId(null);
        ticketClassDAO.save(ticketClass);

        discountDAO.delete(discountId);

        return DiscountTicketClassDTO.toTicketClassDTO(ticketClass);
    }

    private void attachTicketClassesToDiscounts(List<TicketClass> ticketClasses, List<Discount> discounts) {
        Map<Long, Long> ticketClassesIdWithOverdueDiscount = new HashMap<>();
        for (TicketClass ticketClass: ticketClasses) {
            Discount relatedDiscount = DiscountUtils.findDiscount(ticketClass.getDiscountId(), discounts);
            if (relatedDiscount!= null && DiscountUtils.isOverdueDiscount(relatedDiscount)) {
                ticketClassesIdWithOverdueDiscount.put(ticketClass.getClassId(), relatedDiscount.getDiscountId());

                ticketClass.setDiscountId(null);
                continue;
            }

            ticketClass.setDiscount(relatedDiscount);
        }

        deleteOverdueDiscount(ticketClassesIdWithOverdueDiscount);
    }

    private void deleteOverdueDiscount(Map<Long, Long> ticketClassesIdWithOverdueDiscount) {
        ticketClassDAO.deleteDiscountsForTicketClasses(new ArrayList<>(ticketClassesIdWithOverdueDiscount.keySet()));
        discountDAO.deleteDiscounts(new ArrayList<>(ticketClassesIdWithOverdueDiscount.values()));
    }

    private List<DiscountTicketClassDTO> createTicketClassDTOs(List<TicketClass> ticketClasses) {
        return ticketClasses.stream().map(DiscountTicketClassDTO::toTicketClassDTO).collect(Collectors.toList());
    }
}
