package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.BundleForm;
import edu.netcracker.backend.message.response.BundleDTO;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.service.BundleCrudService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BundleCrudController {

    private BundleCrudService bcs;
    private ModelMapper bundleMapper;

    @Autowired
    BundleCrudController(BundleCrudService bcs, ModelMapper objectMapper) {
        this.bcs = bcs;
        this.bundleMapper = objectMapper;
    }

    @GetMapping("api/v1/bundles")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    public List<BundleDTO> getAllBundles(@RequestParam("limit") Number limit,
                                         @RequestParam("offset") Number offset) {
        List<Bundle> bundles = bcs.getAll(limit, offset);
        return bundles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("api/v1/bundles/{id}")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    public BundleDTO getBundleById(@PathVariable("id") Number id) {
        return convertToDTO(bcs.getById(id));
    }

    @PostMapping("api/v1/bundles")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBundle(@RequestBody final BundleForm bundleForm) {
        bcs.add(convertFromDTO(bundleForm));
    }

    @PutMapping("api/v1/bundles/{id}")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void updateBundle(@RequestBody final BundleForm bundleForm) {
        bcs.update(convertFromDTO(bundleForm));
    }

    @DeleteMapping("api/v1/bundles/{id}")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBundle(@PathVariable Number id) {
        bcs.delete(id);
    }

    @GetMapping("api/v1/bundles/count")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
