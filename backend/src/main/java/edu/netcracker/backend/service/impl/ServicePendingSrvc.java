package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.IPendingDao;
import edu.netcracker.backend.message.request.PendingActivationService;
import edu.netcracker.backend.service.IPendingSrvc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "log")
public class ServicePendingSrvc implements IPendingSrvc<PendingActivationService> {

    private IPendingDao<PendingActivationService> iPendingDao;

    @Override
    public List<PendingActivationService> getPendingEntries() {

        log.debug("ServicePendingSrvc.getPendingEntries() was invoked");

        return iPendingDao.getPendingEntries().stream().map(obj -> {

                    if (obj.getApproverName() == null) {
                        obj.setApproverTel("");
                        obj.setApproverName("");
                        obj.setApproverEmail("");
                    }

                    if ("1".equals(obj.getServiceStatus())) {
                        obj.setServiceStatus("Draft");
                    } else if ("2".equals(obj.getServiceStatus())) {
                        obj.setServiceStatus("Opened");
                    } else if ("3".equals(obj.getServiceStatus())) {
                        obj.setServiceStatus("Assigned");
                    }

                    return obj;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<PendingActivationService> getPendingWithOffsetAndLimit(Number limit, Number offset) {

        log.debug("ServicePendingSrvc.getPendingWithOffsetAndLimit(Number limit, Number offset) was invoked with parameters limit = {}, offset = {}", limit, offset);

        return iPendingDao.getPendingWithOffsetAndLimit(limit, offset);
    }

    @Autowired
    public void setiPendingDao(IPendingDao<PendingActivationService> iPendingDao) {
        this.iPendingDao = iPendingDao;
    }
}
