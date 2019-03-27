package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.PossibleServiceDTO;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.PossibleServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PossibleServiceController {

    private PossibleServiceService possibleServiceService;
    private final SecurityContext securityContext;

    @Autowired
    public PossibleServiceController(PossibleServiceService possibleServiceService, SecurityContext securityContext) {
        this.possibleServiceService = possibleServiceService;
        this.securityContext = securityContext;
    }

    @GetMapping("/possible-services")
    public List<PossibleServiceDTO> getAll(@RequestParam("class-id") Integer classId) {
        return possibleServiceService.getAllWithClassId(classId);
    }

    @GetMapping("/possible-services/{possibleServiceId}")
    public PossibleServiceDTO getPossibleService(@PathVariable Integer possibleServiceId) {
        return possibleServiceService.getPossibleService(possibleServiceId);
    }

    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    @PostMapping("/possible-services")
    public PossibleServiceDTO createPossibleService(@Valid @RequestBody PossibleServiceDTO possibleServiceDTO) {
        return possibleServiceService.createPossibleService(possibleServiceDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    @PutMapping("/possible-services")
    public PossibleServiceDTO updatePossibleService(@Valid @RequestBody PossibleServiceDTO possibleServiceDTO) {
        return possibleServiceService.updatePossibleService(possibleServiceDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    @DeleteMapping("/possible-services/{possibleServiceId}")
    public void deletePossibleService(@PathVariable Integer possibleServiceId) {
        possibleServiceService.deletePossibleService(possibleServiceId);
    }

    @GetMapping("/carrier/possible-services")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<PossibleServiceDTO> getCarrierPossibleServices() {
        return possibleServiceService.getCarrierPossibleServices(securityContext.getUser());
    }
}
