package tiw.fsa.api.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tiw.fsa.api.user.Utilisateur;
import tiw.fsa.api.user.UtilisateurRepository;

import java.util.Optional;

@Service
public class ApiUserDetailsService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;

    public ApiUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public ApiUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(username);
        if (utilisateurOpt.isPresent()) {
            return new ApiUserDetails(utilisateurOpt.get());
        } else {
            throw new UsernameNotFoundException("Utilisateur inconnu");
        }
    }
}
