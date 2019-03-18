package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.message.response.SpaceportDTO;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.service.SpaceportService;
import edu.netcracker.backend.service.TicketClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpaceportServiceImpl implements SpaceportService {

    private static final Logger logger = LoggerFactory.getLogger(TicketClassService.class);

    @Autowired
    private SpaceportDAO spaceportDAO;

    @Override
    public List<SpaceportDTO> findSpaceportsOfPlanet(Integer planetId) {
        List<SpaceportDTO> spaceports = spaceportDAO.findSpaceportsOfPlanet(planetId).stream()
                .map(spaceport -> SpaceportDTO.from(spaceport))
                .collect(Collectors.toList());
        return spaceports;
    }
}
