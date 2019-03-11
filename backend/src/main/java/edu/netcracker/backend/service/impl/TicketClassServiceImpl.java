package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.message.request.DiscountDTO;
import edu.netcracker.backend.message.request.DiscountTicketClassDTO;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.service.DiscountService;
import edu.netcracker.backend.service.TicketClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketClassServiceImpl implements TicketClassService {

    private final TicketClassDAO ticketClassDAO;

    private final DiscountService discountService;

    @Autowired
    public TicketClassServiceImpl(TicketClassDAO ticketClassDAO, DiscountService discountService) {
        this.ticketClassDAO = ticketClassDAO;
        this.discountService = discountService;
    }

    @Override
    public List<DiscountTicketClassDTO> getTicketClassesRelatedToCarrier(Number userId) {
        List<TicketClass> ticketClasses = ticketClassDAO.getAllTicketClassesRelatedToCarrier(userId);
        List<DiscountDTO> discountsDTO = discountService.getDiscountDTOs(ticketClasses.stream()
                                                                                      .map(TicketClass::getDiscountId)
                                                                                      .collect(Collectors.toList()));

        return createTicketClassDTOs(ticketClasses, discountsDTO);
    }

    @Override
    public DiscountTicketClassDTO createDiscountForTicketClass(DiscountTicketClassDTO ticketClassDTO) {
        TicketClass ticketClass = getTicketClass(ticketClassDTO);

        DiscountDTO discountDTO = discountService.saveDiscount(ticketClassDTO.getDiscountDTO());

        ticketClass.setDiscountId(discountDTO.getDiscountId());
        ticketClassDAO.save(ticketClass);

        return DiscountTicketClassDTO.toTicketClassDTO(ticketClass, discountDTO);
    }

    @Override
    public DiscountTicketClassDTO deleteDiscountForTicketClass(Number discountId, Number userId) {
        Optional<TicketClass> optionalTicketClass = ticketClassDAO.getTicketClassByDiscount(userId, discountId);

        if (!optionalTicketClass.isPresent()) {
            throw new RequestException("No such discount",
                    HttpStatus.NOT_FOUND);
        }

        TicketClass ticketClass = optionalTicketClass.get();
        ticketClass.setDiscountId(null);
        ticketClassDAO.save(ticketClass);

        DiscountDTO discountDTO = discountService.deleteDiscount(discountId);

        return DiscountTicketClassDTO.toTicketClassDTO(ticketClass, discountDTO);
    }

    private TicketClass getTicketClass(DiscountTicketClassDTO ticketClassDTO) {
        Optional<TicketClass> optionalTicketClass = ticketClassDAO.find(ticketClassDTO.getClassId());

        if (!optionalTicketClass.isPresent()) {
            throw new RequestException("Ticket class with id " + ticketClassDTO.getClassId() + " is null",
                    HttpStatus.NOT_FOUND);
        }

        TicketClass ticketClass = optionalTicketClass.get();

        if (ticketClass.getDiscountId() != null) {
            throw new RequestException("Discount already exist",
                    HttpStatus.CONFLICT);
        }
        return ticketClass;
    }

    private List<DiscountTicketClassDTO> createTicketClassDTOs(List<TicketClass> ticketClasses,
                                                               List<DiscountDTO> discountDTOs) {
        List<DiscountTicketClassDTO> discountTicketClassDTOs = new ArrayList<>();
        for (TicketClass ticketClass : ticketClasses) {
            DiscountDTO relatedDiscount =
                    discountService.getRelatedDiscountDTO(ticketClass.getDiscountId(), discountDTOs);
            discountTicketClassDTOs.add(DiscountTicketClassDTO.toTicketClassDTO(ticketClass, relatedDiscount));
        }
        return discountTicketClassDTOs;
    }
}
