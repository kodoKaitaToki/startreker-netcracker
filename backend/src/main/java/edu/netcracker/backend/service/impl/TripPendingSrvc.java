package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.IPendingDao;
import edu.netcracker.backend.message.request.TripPendingActivationDto;
import edu.netcracker.backend.service.IPendingSrvc;
import edu.netcracker.backend.utils.ServiceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "log")
public class TripPendingSrvc implements IPendingSrvc<TripPendingActivationDto> {

    private IPendingDao<TripPendingActivationDto> iPendingDao;

    @Override
    public List<TripPendingActivationDto> getPendingEntries() {

        log.debug("TripPendingSrvc.getPendingEntries() was invoked");

        return iPendingDao.getPendingEntries();
    }

    @Override
    public List<TripPendingActivationDto> getPendingWithOffsetAndLimit(Number limit, Number offset) {

        log.debug("TripPendingSrvc.getPendingWithOffsetAndLimit(Number limit, Number offset) was invoked with parameters limit = {} offset = {}", limit, offset);

        return iPendingDao.getPendingWithOffsetAndLimit(limit, offset);
    }

    @Autowired
    public void setiPendingDao(IPendingDao<TripPendingActivationDto> iPendingDao) {
        this.iPendingDao = iPendingDao;
    }
}
