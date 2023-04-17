package tiw.fsa.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tiw.fsa.api.user.Utilisateur;

import java.util.Collection;
import java.util.List;

/**
 * Encapsule un utilisateur pour fournir une implémentation attendue par Spring Security.
 */
public class ApiUserDetails implements UserDetails {
    private final Utilisateur utilisateur;

    public ApiUserDetails(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(utilisateur.getRole()));
    }

    /**
     * Indique si l'utilisateur est admin.
     * @return true si l'utilisateur est admin
     */
    public boolean isAdmin() {
        return Utilisateur.ADMIN.equals(utilisateur.getRole());
    }

    @Override
    public String getPassword() {
        return utilisateur.getPassword();
    }

    @Override
    public String getUsername() {
        return utilisateur.getLogin();
    }

    /**
     * Renvoie le role de l'utilisateur.
     * @return le role de l'utilisateur.
     */
    public String getRole() {
        return utilisateur.getRole();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
