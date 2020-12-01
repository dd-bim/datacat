package de.bentrm.datacat.auth.domain;

import de.bentrm.datacat.base.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = "User")
public class User extends Entity implements UserDetails {

    @ToString.Include
    @EqualsAndHashCode.Include
    @Index(unique = true)
    @NotBlank
    protected String username;

    @NotBlank
    private String password;

    @ToString.Include
    @NotBlank
    private String firstName;

    @ToString.Include
    @NotBlank
    private String lastName;

    @Index(unique = true)
    @NotBlank
    private String email;

    @NotNull
    private String organization;

    private boolean expired = false;

    private boolean locked = false;

    private boolean credentialsExpired = false;

    private boolean emailConfirmed = false;

    private Set<Role> roles = new HashSet<>();

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return !locked && !expired && !credentialsExpired && emailConfirmed;
    }

    @Override
    public List<Role> getAuthorities() {
        return roles.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
