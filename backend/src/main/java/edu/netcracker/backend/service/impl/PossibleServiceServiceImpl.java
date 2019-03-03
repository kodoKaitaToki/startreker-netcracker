package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.PossibleServiceDAO;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.message.response.PossibleServiceDTO;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.service.PossibleServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PossibleServiceServiceImpl implements PossibleServiceService {
    private PossibleServiceDAO possibleServiceDAO;
    private ServiceDAO serviceDAO;

    @Autowired
    public PossibleServiceServiceImpl(PossibleServiceDAO possibleServiceDAO, ServiceDAO serviceDAO) {
        this.possibleServiceDAO = possibleServiceDAO;
        this.serviceDAO = serviceDAO;
    }

    @Override
    public PossibleServiceDTO getPossibleService(Number id) {
        Optional<PossibleService> optPossibleService = possibleServiceDAO.find(id);

        if (!optPossibleService.isPresent())
            throw new RequestException("Possible service with id " + id + " not found", HttpStatus.NOT_FOUND);

        return PossibleServiceDTO.from(optPossibleService.get());
    }

    @Override
    public List<PossibleServiceDTO> getAllWithClassId(Number classId) {
        List<PossibleService> possibleServices = possibleServiceDAO.findAllWithClassId(classId);

        if (possibleServices.size() == 0)
            throw new RequestException("No possible services yet", HttpStatus.NOT_FOUND);

        return possibleServices.stream().map(PossibleServiceDTO::from).collect(Collectors.toList());
    }

    @Override
    public PossibleServiceDTO createPossibleService(PossibleServiceDTO possibleServiceDTO) {
        PossibleService possibleService = from(possibleServiceDTO);
        possibleServiceDAO.save(possibleService);

        return PossibleServiceDTO.from(possibleService);
    }

    @Override
    public void deletePossibleService(Number id) {
        Optional<PossibleService> optPossibleService = possibleServiceDAO.find(id);

        if (!optPossibleService.isPresent())
            throw new RequestException("Possible service with id" + id + " not found ", HttpStatus.NOT_FOUND);

        possibleServiceDAO.delete(optPossibleService.get());
    }

    @Override
    public PossibleServiceDTO updatePossibleService(PossibleServiceDTO possibleServiceDTO) {
        Optional<PossibleService> optPossibleService = possibleServiceDAO.find(possibleServiceDTO.getId());

        if (!optPossibleService.isPresent())
            throw new RequestException("Possible service with id" + possibleServiceDTO.getId() + " not found ", HttpStatus.NOT_FOUND);

        possibleServiceDAO.save(from(possibleServiceDTO));

        return PossibleServiceDTO.from(possibleServiceDAO.find(possibleServiceDTO.getId()).get());
    }

    private PossibleService from(PossibleServiceDTO possibleServiceDTO) {
        PossibleService possibleService = new PossibleService();

        possibleService.setClassId(possibleServiceDTO.getClassId());
        possibleService.setServiceId(possibleServiceDTO.getServiceId());
        possibleService.setServicePrice(possibleServiceDTO.getServicePrice());
        possibleService.setPServiceId(possibleServiceDTO.getId());

        Optional<edu.netcracker.backend.model.Service> optService = serviceDAO.find(possibleServiceDTO.getServiceId());

        if (!optService.isPresent())
            throw new RequestException("Service with id " + possibleServiceDTO.getServiceId() + " not found ", HttpStatus.NOT_FOUND);

        possibleService.setService(optService.get());

        return possibleService;

    }
}
