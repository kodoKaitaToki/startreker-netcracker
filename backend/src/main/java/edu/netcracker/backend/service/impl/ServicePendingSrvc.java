package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.IPendingDao;
import edu.netcracker.backend.message.request.ServicePendingActivationDto;
import edu.netcracker.backend.service.IPendingSrvc;
import edu.netcracker.backend.utils.ServiceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "log")
public class ServicePendingSrvc implements IPendingSrvc<ServicePendingActivationDto> {

    private IPendingDao<ServicePendingActivationDto> iPendingDao;

    @Override
    public List<ServicePendingActivationDto> getPendingEntries() {

        log.debug("ServicePendingSrvc.getPendingEntries() was invoked");

        return iPendingDao.getPendingEntries();
    }

    @Override
    public List<ServicePendingActivationDto> getPendingWithOffsetAndLimit(Number limit, Number offset) {

        log.debug("ServicePendingSrvc.getPendingWithOffsetAndLimit(Number limit, Number offset) was invoked with parameters limit = {}, offset = {}", limit, offset);

        return iPendingDao.getPendingWithOffsetAndLimit(limit, offset);
    }

    @Autowired
    public void setiPendingDao(IPendingDao<ServicePendingActivationDto> iPendingDao) {
        this.iPendingDao = iPendingDao;
    }
}
