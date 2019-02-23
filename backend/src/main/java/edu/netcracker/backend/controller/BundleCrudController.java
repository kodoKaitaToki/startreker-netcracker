package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.BundleDTO;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.service.BundleCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BundleCrudController {

    private BundleCrudService bcs;

    @Autowired
    BundleCrudController(BundleCrudService bcs) {
        this.bcs = bcs;
    }

    @GetMapping("api/v1/admin/bundles/all")
    public List<BundleDTO> getAllBundles() {
        ArrayList<Bundle> bundles = new ArrayList<>(bcs.getAll());
        ArrayList<BundleDTO> bundleDTOS = new ArrayList<>();
        for (Bundle bundle : bundles) {
            bundleDTOS.add(BundleDTO.from(bundle));
        }
        return null;
    }

    @GetMapping("api/v1/admin/bundles/paging")
    public List<BundleDTO> getAllBundles(Number limit, Number offset) {
        return null;
    }


}
