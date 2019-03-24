package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.PossibleServiceDTO;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.User;

import java.util.List;

public interface PossibleServiceService {
    PossibleServiceDTO getPossibleService(Number id);

    List<PossibleServiceDTO> getAllWithClassId(Number classId);

    PossibleServiceDTO createPossibleService(PossibleServiceDTO possibleServiceDTO);

    void deletePossibleService(Number id);

    PossibleServiceDTO updatePossibleService(PossibleServiceDTO possibleServiceDTO);

    List<PossibleServiceDTO> getCarrierPossibleServices(User user);
}
