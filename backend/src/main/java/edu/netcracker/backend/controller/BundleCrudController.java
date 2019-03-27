package edu.netcracker.backend.controller;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.request.BundleForm;
import edu.netcracker.backend.message.response.BundleDTO;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.service.BundleCrudService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api/v1/bundles")
public class BundleCrudController {

    private final BundleCrudService bcs;
    private final ModelMapper bundleMapper;

    @Autowired
    BundleCrudController(BundleCrudService bcs, ModelMapper objectMapper) {
        this.bcs = bcs;
        this.bundleMapper = objectMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<BundleDTO> getAllBundles(@RequestParam("limit") Number limit, @RequestParam("offset") Number offset) {
        log.info("Getting {} bundles with offset {}.", limit, offset);
        List<Bundle> bundles = bcs.getAll(limit, offset);

        log.info("Got {} bundles", bundles.size());

        return bundles.stream()
                      .map(this::convertToDTO)
                      .collect(Collectors.toList());
    }

    @GetMapping("/fresh")
    @ResponseBody
    public List<BundleDTO> getAllBundles() {
        log.debug("BundleCrudController.getAllBundles() was invoked");

        return bcs.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BundleDTO getBundleById(@PathVariable("id") Number id) {
        log.info("Getting bundle with id {}", id);
        return convertToDTO(bcs.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBundle(@RequestBody final BundleForm bundleForm) {
        bcs.add(convertFromDTO(bundleForm));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void updateBundle(@PathVariable("id") final Number id, @RequestBody final BundleForm bundleForm) {
        if (bundleForm.getId() == null) {
            log.error("Bundle id is null");
            throw new RequestException("Bundle id is null.", HttpStatus.BAD_REQUEST);
        }
        bcs.update(convertFromDTO(bundleForm));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBundle(@PathVariable final Number id) {
        bcs.delete(id);
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Long countBundles() {
        return bcs.count();
    }

    private BundleDTO convertToDTO(Bundle bundle) {
        BundleDTO bundleDTO = bundleMapper.map(bundle, BundleDTO.class);
        bundleDTO.setStartDate(BundleDTO.convertToString(bundle.getStartDate()));
        bundleDTO.setFinishDate(BundleDTO.convertToString(bundle.getFinishDate()));
        return bundleDTO;
    }

    private Bundle convertFromDTO(BundleForm bundleForm) {
        Bundle bundle = bundleMapper.map(bundleForm, Bundle.class);
        bundle.setStartDate(BundleDTO.convertToLocalDate(bundleForm.getStartDate()));
        bundle.setFinishDate(BundleDTO.convertToLocalDate(bundleForm.getFinishDate()));

        if (bundleForm.getId() != null) {
            Bundle oldBundle = bcs.getById(bundleForm.getId());
            bundle.setBundleId(oldBundle.getBundleId());
        }
        return bundle;
    }
}
