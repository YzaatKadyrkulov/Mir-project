package kg.mir.Mirproject.entities;

import jakarta.persistence.*;
import kg.mir.Mirproject.enums.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "base_id_gen", sequenceName = "user_seq", allocationSize = 1)
public class User extends BaseEntity implements UserDetails {
    private String email;
    private String password;
    private String photoUrl;
    private String code;
    private Integer principalDebt;
    private Integer totalSum;
    private Integer goal;
    private String userName;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private World world;

    @OneToMany(mappedBy = "user", cascade = {REFRESH, REMOVE, MERGE})
    private List<Payment> payments;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
