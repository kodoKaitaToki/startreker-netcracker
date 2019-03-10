package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.message.request.DiscountDTO;
import edu.netcracker.backend.message.request.DiscountTicketClassDTO;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.service.DiscountService;
import edu.netcracker.backend.service.TicketClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketClassServiceImpl implements TicketClassService {

    private static final Logger logger = LoggerFactory.getLogger(TicketClassService.class);

    private final TicketClassDAO ticketClassDAO;

    private final DiscountService discountService;

    @Autowired
    public TicketClassServiceImpl(TicketClassDAO ticketClassDAO, DiscountService discountService) {
        this.ticketClassDAO = ticketClassDAO;
        this.discountService = discountService;
    }

    @Override
    public TicketClass find(Number ticketClassId) {
        logger.debug("find TicketClass with id " + ticketClassId);
        Optional<TicketClass> optionalTicketClass = ticketClassDAO.find(ticketClassId);

        if (!optionalTicketClass.isPresent()) {
            logger.error("No such ticket class with id " + ticketClassId);
            throw new RequestException("No such ticket class", HttpStatus.NOT_FOUND);
        }

        return optionalTicketClass.get();
    }

    @Override
    public List<DiscountTicketClassDTO> getTicketClassesRelatedToCarrier(Number userId) {
        logger.debug("get ticket classes that belong to carrier with id " + userId);
        List<TicketClass> ticketClasses = ticketClassDAO.getAllTicketClassesRelatedToCarrier(userId);
        List<DiscountDTO> discountsDTO = discountService.getDiscountDTOs(ticketClasses.stream()
                .map(TicketClass::getDiscountId)
                .collect(Collectors.toList()));

        return createTicketClassDTOs(ticketClasses, discountsDTO);
    }

    @Override
    public DiscountTicketClassDTO createDiscountForTicketClass(DiscountTicketClassDTO ticketClassDTO, Number userId) {
        logger.debug("create discount for ticket class with id " + ticketClassDTO.getClassId());

        TicketClass ticketClass = getTicketClass(ticketClassDTO, userId);

        DiscountDTO discountDTO = discountService.saveDiscount(ticketClassDTO.getDiscountDTO());

        ticketClass.setDiscountId(discountDTO.getDiscountId());
        ticketClassDAO.save(ticketClass);

        return DiscountTicketClassDTO.toTicketClassDTO(ticketClass, discountDTO);
    }

    @Override
    public DiscountTicketClassDTO deleteDiscountForTicketClass(Number discountId, Number userId) {
        logger.debug("delete discount with id " + discountId + " from ticket class");

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

    @Override
    public Map<Long, List<TicketClass>> getAllTicketClassesBelongToTrips(List<Number> tripIds) {
        return ticketClassDAO.getAllTicketClassesBelongToTrips(tripIds);
    }

    private TicketClass getTicketClass(DiscountTicketClassDTO ticketClassDTO, Number userId) {
        Optional<TicketClass> optionalTicketClass
                = ticketClassDAO.findTicketClassBelongToCarrier(ticketClassDTO.getClassId(), userId);

        if (!optionalTicketClass.isPresent()) {
            logger.error("No such ticket class with id " + ticketClassDTO.getClassId());

            throw new RequestException("No such ticket class with id " + ticketClassDTO.getClassId(),
                    HttpStatus.NOT_FOUND);
        }

        TicketClass ticketClass = optionalTicketClass.get();

        if (ticketClass.getDiscountId() != null) {
            logger.error("Discount already exist for ticket class with id" + ticketClass.getClassId());

            throw new RequestException("Discount already exist",
                    HttpStatus.CONFLICT);
        }
        return ticketClass;
    }

    private List<DiscountTicketClassDTO> createTicketClassDTOs(List<TicketClass> ticketClasses,
                                                               List<DiscountDTO> discountDTOs) {
        List<DiscountTicketClassDTO> discountTicketClassDTOs = new ArrayList<>();
        for (TicketClass ticketClass : ticketClasses) {
            DiscountDTO relatedDiscount = discountService.getRelatedDiscountDTO(
                    ticketClass.getDiscountId(),
                    discountDTOs);
            discountTicketClassDTOs.add(DiscountTicketClassDTO.toTicketClassDTO(ticketClass, relatedDiscount));
        }
        return discountTicketClassDTOs;
    }
}
