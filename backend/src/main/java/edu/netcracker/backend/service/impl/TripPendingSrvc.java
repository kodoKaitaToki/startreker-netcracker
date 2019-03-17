package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.IPendingDao;
import edu.netcracker.backend.message.request.PendingActivationTrip;
import edu.netcracker.backend.service.IPendingSrvc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "log")
public class TripPendingSrvc implements IPendingSrvc<PendingActivationTrip> {

    private IPendingDao<PendingActivationTrip> iPendingDao;

    @Override
    public List<PendingActivationTrip> getPendingEntries() {

        log.debug("TripPendingSrvc.getPendingEntries() was invoked");

        return iPendingDao.getPendingEntries();
    }

    @Override
    public List<PendingActivationTrip> getPendingWithOffsetAndLimit(Number limit, Number offset) {

        log.debug("TripPendingSrvc.getPendingWithOffsetAndLimit(Number limit, Number offset) was invoked with parameters limit = {} offset = {}", limit, offset);

        return iPendingDao.getPendingWithOffsetAndLimit(limit, offset);
    }

    @Autowired
    public void setiPendingDao(IPendingDao<PendingActivationTrip> iPendingDao) {
        this.iPendingDao = iPendingDao;
    }
}
