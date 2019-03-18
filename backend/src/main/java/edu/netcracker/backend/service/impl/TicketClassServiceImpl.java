package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TicketDAO;
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

    private final TicketDAO ticketDAO;

    @Autowired
    public TicketClassServiceImpl(TicketClassDAO ticketClassDAO, DiscountService discountService, TicketDAO ticketDAO) {
        this.ticketClassDAO = ticketClassDAO;
        this.discountService = discountService;
        this.ticketDAO = ticketDAO;
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
            throw new RequestException("No such discount", HttpStatus.NOT_FOUND);
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

    @Override
    public void deleteTicketClass(Long id) {
        logger.debug("Deleting all ticket which belong to ticket class with id {}", id);
        this.ticketDAO.deleteAllTicketsOfTicketClass(id);
        logger.debug("Deleting class with id {}", id);
        this.ticketClassDAO.deleteTicketClassById(id);
    }

    @Override
    public void createOrUpdate(TicketClass ticketClass) {
        logger.debug("Checking whether ticket class with name {} and trip id {} exists",
                     ticketClass.getClassName(),
                     ticketClass.getTripId());
        if (this.ticketClassDAO.exists(ticketClass.getTripId(), ticketClass.getClassName())) {
            logger.debug("Ticket class exists in database: updating...");
            updateTicketClass(ticketClass);
        } else {
            logger.debug("Ticket class is absent in database: creating...");
            createNewTicketClass(ticketClass);
        }
    }

    private void updateTicketClass(TicketClass ticketClass) {
        logger.debug("Getting existing ticket class from database");
        TicketClass existingTicketClass =
                this.ticketClassDAO.getTicketClassByNameAndTripId(ticketClass.getTripId(), ticketClass.getClassName());
        logger.debug("Copying class id value to new version of ticket class");
        ticketClass.setClassId(existingTicketClass.getClassId());
        logger.debug("Updating ticket class info");
        this.ticketClassDAO.update(ticketClass);
        logger.debug("Checking whether number of seats has changed");
        if (!existingTicketClass.getClassSeats()
                                .equals(ticketClass.getClassSeats())) {
            logger.debug("Number of seats has changed - deleting all tickets and create new ones");
            this.ticketDAO.deleteAllTicketsOfTicketClass(ticketClass.getClassId());
            this.createTicketsForTicketClass(ticketClass);

        }
    }

    private void createNewTicketClass(TicketClass ticketClass) {
        logger.debug("Creating ticket class");
        ticketClassDAO.create(ticketClass);

        logger.debug("Getting id of created ticket class by its class name and trip id");
        ticketClass.setClassId(ticketClassDAO.getTicketClassId(ticketClass.getClassName(), ticketClass.getTripId()));

        this.createTicketsForTicketClass(ticketClass);
    }

    private void createTicketsForTicketClass(TicketClass ticketClass) {
        logger.debug("Adding {} new empty tickets for ticket class", ticketClass.getClassSeats());
        for (int i = 1; i <= ticketClass.getClassSeats(); i++) {
            ticketDAO.createEmptyTicketForTicketClass(ticketClass.getClassId(), ticketClass.getClassId() * 1000 + i);
        }
    }

    private TicketClass getTicketClass(DiscountTicketClassDTO ticketClassDTO, Number userId) {
        Optional<TicketClass> optionalTicketClass =
                ticketClassDAO.findTicketClassBelongToCarrier(ticketClassDTO.getClassId(), userId);

        if (!optionalTicketClass.isPresent()) {
            logger.error("No such ticket class with id " + ticketClassDTO.getClassId());

            throw new RequestException("No such ticket class with id " + ticketClassDTO.getClassId(),
                                       HttpStatus.NOT_FOUND);
        }

        TicketClass ticketClass = optionalTicketClass.get();

        if (ticketClass.getDiscountId() != null) {
            logger.error("Discount already exist for ticket class with id" + ticketClass.getClassId());

            throw new RequestException("Discount already exist", HttpStatus.CONFLICT);
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
