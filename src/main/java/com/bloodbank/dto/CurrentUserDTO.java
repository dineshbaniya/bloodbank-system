package com.bloodbank.dto;

import com.bloodbank.model.User;

public class CurrentUserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private Long organizationId;
    private String organizationName;

    public static CurrentUserDTO fromEntity(User user) {
        CurrentUserDTO dto = new CurrentUserDTO();
        dto.id = user.getId();
        dto.fullName = user.getFullName();
        dto.email = user.getEmail();
        dto.role = user.getRole() != null ? user.getRole().name() : null;
        if (user.getOrganization() != null) {
            dto.organizationId = user.getOrganization().getId();
            dto.organizationName = user.getOrganization().getName();
        }
        return dto;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Long getOrganizationId() { return organizationId; }
    public String getOrganizationName() { return organizationName; }
}