package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.IPendingDao;
import edu.netcracker.backend.message.request.PendingActivationTrip;
import edu.netcracker.backend.service.IPendingSrvc;
import edu.netcracker.backend.utils.ServiceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "log")
public class TripPendingSrvc implements IPendingSrvc<PendingActivationTrip> {

    private IPendingDao<PendingActivationTrip> iPendingDao;

    @Override
    public List<PendingActivationTrip> getPendingEntries() {

        log.debug("TripPendingSrvc.getPendingEntries() was invoked");

        return iPendingDao.getPendingEntries().stream().map(obj -> {
            if (obj.getApproverName() == null) {
                obj.setApproverEmail("");
                obj.setApproverName("");
                obj.setApproverTel("");
            }

            if (((Integer)ServiceStatus.DRAFT.getValue()).toString().equals(obj.getTripStatus())) {
                obj.setTripStatus("Draft");
            } else if (((Integer)ServiceStatus.OPEN.getValue()).toString().equals(obj.getTripStatus())) {
                obj.setTripStatus("Opened");
            } else if (((Integer)ServiceStatus.ASSIGNED.getValue()).toString().equals(obj.getTripStatus())) {
                obj.setTripStatus("Assigned");
            }

            return obj;
        }).collect(Collectors.toList());
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
