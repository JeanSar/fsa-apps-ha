package tiw.fsa.api.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Composant de gestion des utilisateurs
 */
@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Récupère un utilisateur à partir de son login
     *
     * @param login le login de l'utilisateur.
     * @return l'utilisateur sous forme de DTO
     * @throws UtilisateurNotFoundException si l'utilisateur n'existe pas
     */
    public UtilisateurDTO getUtilisateur(String login) throws UtilisateurNotFoundException {
        return utilisateurRepository
                .findById(login)
                .map(UtilisateurDTO::fromUtilisateur)
                .orElseThrow(UtilisateurNotFoundException::new);
    }

    /**
     * Créée ou met à jour un utilisateur.
     *
     * @param utilisateurDTO Les informations sur l'utilisateur.
     * @return l'utilisateur mis à jour.
     * @throws UtilisateurIncompletException S'il manque des informations (mot de passe manquant sur un nouvel utilisateur).
     */
    @Transactional
    public UtilisateurDTO createOrUpdateUtilisateur(UtilisateurDTO utilisateurDTO) throws UtilisateurIncompletException {
        var utilisateurOpt = utilisateurRepository.findById(utilisateurDTO.login());
        if (utilisateurOpt.isPresent()) {
            var utilisateur = utilisateurOpt.get();
            if (utilisateurDTO.password() != null) {
                utilisateur.setPassword(passwordEncoder.encode(utilisateurDTO.password()));
            }
            if (utilisateurDTO.role() != null) {
                utilisateur.setRole(utilisateurDTO.role());
            }
            utilisateurRepository.save(utilisateur);
            return UtilisateurDTO.fromUtilisateur(utilisateur);
        } else {
            if (utilisateurDTO.password() == null) {
                throw new UtilisateurIncompletException("No password provided for new user");
            } else {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setLogin(utilisateurDTO.login());
                utilisateur.setPassword(passwordEncoder.encode(utilisateurDTO.password()));
                var role = utilisateurDTO.role();
                if (!Utilisateur.ADMIN.equals(role) && !Utilisateur.NORMAL.equals(role)) {
                    role = Utilisateur.NORMAL;
                }
                utilisateur.setRole(role);
                utilisateurRepository.save(utilisateur);
                return UtilisateurDTO.fromUtilisateur(utilisateur);
            }
        }
    }

    public boolean utilisateurExists(String login) {
        return utilisateurRepository.existsById(login);
    }

    public void deleteUtilisateur(String login) {

        utilisateurRepository.deleteById(login);
    }
}
