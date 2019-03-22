package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.message.response.BundleDTO;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.service.BundleCrudService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BundleCrudServiceImpl implements BundleCrudService {

    private BundleDAO bundleDAO;
    private final ModelMapper bundleMapper;

    @Autowired
    public BundleCrudServiceImpl(BundleDAO bundleDAO, ModelMapper bundleMapper) {
        this.bundleDAO = bundleDAO;
        this.bundleMapper = bundleMapper;
    }

    @Override
    public List<Bundle> getAll(Number limit, Number offset) {
        return bundleDAO.findAll(limit, offset);
    }

    @Override
    public List<BundleDTO> getAll() {
        log.debug("BundleCrudService.getAll() was invoked");
        List<Bundle> bundles = bundleDAO.findAll();
        return bundles.stream()
                .map(bundle -> convertToDTO(bundle))
                .collect(Collectors.toList());
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

    private BundleDTO convertToDTO(Bundle bundle) {
        BundleDTO bundleDTO = bundleMapper.map(bundle, BundleDTO.class);
        bundleDTO.setStartDate(BundleDTO.convertToString(bundle.getStartDate()));
        bundleDTO.setFinishDate(BundleDTO.convertToString(bundle.getFinishDate()));
        return bundleDTO;
    }
}
