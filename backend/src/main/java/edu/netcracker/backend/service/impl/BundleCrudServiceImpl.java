package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.service.BundleCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BundleCrudServiceImpl implements BundleCrudService {

    private BundleDAO bundleDAO;

    @Autowired
    public BundleCrudServiceImpl(BundleDAO bundleDAO) {
        this.bundleDAO = bundleDAO;
    }


    @Override
    public List<Bundle> getAll(Number limit, Number offset) {
        return bundleDAO.findAll(limit, offset);
    }

    @Override
    public Bundle getById(Number id) {
        Optional<Bundle> bundleOpt;
        try {
            bundleOpt = bundleDAO.find(id);
            return bundleOpt.get();
        } catch (EmptyResultDataAccessException e) {
            log.error("Could not find bundle with id {}", id, e);
            throw new RequestException(String.format("Bundle with id %s doesn't exist", id), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void add(Bundle bundle) {
        if (bundle.getFinishDate()
                  .isBefore(LocalDateTime.now())) {
            log.error("Finish date is less than current date");
            throw new RequestException("Finish date less than current date", HttpStatus.BAD_REQUEST);
        }

        if (bundle.getFinishDate()
                  .isBefore(bundle.getStartDate())) {
            log.error("Finish date is less than start date of discount");
            throw new RequestException("Finish date is less than start date of discount", HttpStatus.BAD_REQUEST);
        }

        bundleDAO.save(bundle);
    }

    @Override
    public void update(Bundle bundle) {
        bundleDAO.update(bundle);
    }

    @Override
    public void delete(Number id) {
        bundleDAO.delete(id);
    }

    @Override
    public Long count() {
        return bundleDAO.count();
    }
}
