package com.project.projectaquiler.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
@Builder
public class RoleEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @JoinColumn(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissions = new HashSet<>();
}
