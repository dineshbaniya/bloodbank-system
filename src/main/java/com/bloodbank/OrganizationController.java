package com.bloodbank;

import com.bloodbank.model.Organization;
import com.bloodbank.repository.OrganizationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationRepository organizationRepository;

    // Spring automatically supplies the repository here - this is called "dependency injection"
    public OrganizationController(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @PostMapping
    public Organization create(@RequestBody Organization organization) {
        return organizationRepository.save(organization);
    }

    @GetMapping
    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }
}