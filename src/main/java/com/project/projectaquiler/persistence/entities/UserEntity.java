package com.project.projectaquiler.persistence.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(unique = true)
  private String userName;

  private String password;

  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private Integer dni;

  private String name;
  private String lastName;
  private Integer phone;
  private Integer age;
  private String gender;
  private String address;
  private boolean isEnable;
  private boolean accountNoExpired;
  private boolean accountNoLocked;
  private boolean credentialNoExpired;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  @Builder.Default
  private Set<RoleEntity> roles = new HashSet<>();
}
