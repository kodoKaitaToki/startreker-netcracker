package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.BundleDTO;
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

    private ModelMapper modelMapper;

    @Autowired
    BundleCrudController(BundleCrudService bcs, ModelMapper objectMapper) {
        this.bcs = bcs;
        this.modelMapper = objectMapper;
    }

    @GetMapping("api/v1/admin/bundles/all")
    @ResponseBody
    public List<BundleDTO> getAllBundles() {
        List<Bundle> bundles = bcs.getAll();
        return bundles.stream()
                .map(bundle -> convertToDTO(bundle))
                .collect(Collectors.toList());
    }

    @GetMapping("api/v1/admin/bundles/paging")
    @ResponseBody
    public List<BundleDTO> getAllBundles(@RequestParam("limit") Number limit, @RequestParam("offset") Number offset) {
        List<Bundle> bundles = bcs.getAll(limit, offset);
        return bundles.stream()
                .map(bundle -> convertToDTO(bundle))
                .collect(Collectors.toList());
    }

    @GetMapping("api/v1/admin/bundles")
    @ResponseBody
    public BundleDTO getBundleById(@RequestParam("id") Number id) {
        return convertToDTO(bcs.getById(id));
    }

    @PostMapping("api/v1/admin/bundles")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBundle(@RequestBody BundleDTO bundleDTO) {
        bcs.add(convertFromDTO(bundleDTO));
    }

    @PutMapping("api/v1/admin/bundles")
    @ResponseStatus(HttpStatus.OK)
    public void updateBundle(@RequestBody BundleDTO bundleDTO) {
        bcs.update(convertFromDTO(bundleDTO));
    }

    @DeleteMapping("api/v1/admin/bundles")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBundle(@RequestBody BundleDTO bundleDTO) {
        bcs.delete(convertFromDTO(bundleDTO));
    }

    @GetMapping("api/v1/admin/bundles")
    public Long countBundles() {
        return bcs.count();
    }

    private BundleDTO convertToDTO(Bundle bundle) {
        BundleDTO bundleDTO = modelMapper.map(bundle, BundleDTO.class);
        bundleDTO.setStartDate(BundleDTO.convertToString(bundle.getStartDate()));
        bundleDTO.setFinishDate(BundleDTO.convertToString(bundle.getFinishDate()));
        return bundleDTO;
    }

    private Bundle convertFromDTO(BundleDTO bundleDTO) {
        Bundle bundle = modelMapper.map(bundleDTO, Bundle.class);
        bundle.setStartDate(BundleDTO.convertToLocalDate(bundleDTO.getStartDate()));
        bundle.setFinishDate(BundleDTO.convertToLocalDate(bundleDTO.getFinishDate()));

        if (bundleDTO.getId() != null) {
            Bundle oldBundle = bcs.getById(bundleDTO.getId());
            bundle.setBundleId(oldBundle.getBundleId());
        }
        return bundle;
    }
}
