package de.bentrm.datacat.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NodeEntity(label = "User")
public class User extends Entity implements UserDetails {

    @Index(unique = true)
    @NotBlank
    protected String username;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Index(unique = true)
    @NotBlank
    private String email;

    @NotNull
    private String organization;

    private boolean expired = false;

    private boolean locked = true;

    private boolean credentialsExpired = false;

    private Set<Roles> roles = new HashSet<>();

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
        return !locked && !expired && !credentialsExpired;
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return roles.stream()
                .sorted()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }
}
