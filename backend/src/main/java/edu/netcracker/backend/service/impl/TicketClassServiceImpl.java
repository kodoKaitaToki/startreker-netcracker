package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.message.request.DiscountDTO;
import edu.netcracker.backend.message.request.TicketClassDTO;
import edu.netcracker.backend.model.Discount;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.service.TicketClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    public List<TicketClassDTO> getTicketClassesRelatedToCarrier(Number userId) {
        List<TicketClass> ticketClasses = ticketClassDAO.getAllTicketClassesRelatedToCarrier(userId);
        List<Discount> discounts = discountDAO.findIn(ticketClasses.stream()
                .map(TicketClass::getDiscountId)
                .collect(Collectors.toList()));

        return createTicketClassDTOs(ticketClasses, discounts);
    }

    @Override
    public TicketClassDTO createDiscountForTicketClass(TicketClassDTO ticketClassDTO) {
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

        Discount discount = toDiscount(ticketClassDTO.getDiscount());

        discountDAO.save(discount);
        ticketClass.setDiscountId(discount.getDiscountId());
        ticketClassDAO.save(ticketClass);

        return TicketClassDTO.toTicketClassDTO(ticketClass, discount);
    }

    @Override
    public TicketClassDTO deleteDiscountForTicketClass(Number discountId, Number userId) {
        Optional<TicketClass> optionalTicketClass = ticketClassDAO.getTicketClassByDiscount(userId, discountId);

        if (!optionalTicketClass.isPresent()) {
            throw new RequestException("Discount not exist, or not allowed for this user",
                    HttpStatus.NOT_FOUND);
        }

        TicketClass ticketClass = optionalTicketClass.get();
        ticketClass.setDiscountId(null);
        ticketClassDAO.save(ticketClass);

        discountDAO.delete(discountId);

        return TicketClassDTO.toTicketClassDTO(ticketClass, null);
    }

    private List<TicketClassDTO> createTicketClassDTOs(List<TicketClass> ticketClasses, List<Discount> discounts) {
        List<TicketClassDTO> ticketClassDTOs = new ArrayList<>();
        for (TicketClass ticketClass : ticketClasses) {
            ticketClassDTOs.add(createTicketClassDTO(ticketClass, discounts));
        }
        return ticketClassDTOs;
    }

    private TicketClassDTO createTicketClassDTO(TicketClass ticketClass, List<Discount> discounts) {
        for (Discount discount : discounts) {
            if (discount.getDiscountId().equals(ticketClass.getDiscountId())) {
                return TicketClassDTO.toTicketClassDTO(ticketClass, discount);
            }
        }

        return TicketClassDTO.toTicketClassDTO(ticketClass, null);
    }

    private Discount toDiscount(DiscountDTO discountDTO) {
        Discount discount = new Discount();
        discount.setDiscountId(discountDTO.getDiscountId());
        discount.setDiscountRate(discountDTO.getDiscountRate());
        discount.setDiscountType(discountDTO.getDiscountType());
        discount.setStartDate(LocalDateTime
                .parse(discountDTO.getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        discount.setFinishDate(LocalDateTime
                .parse(discountDTO.getFinishDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        return discount;
    }
}
