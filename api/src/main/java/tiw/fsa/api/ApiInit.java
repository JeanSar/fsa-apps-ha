package tiw.fsa.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tiw.fsa.api.user.Utilisateur;
import tiw.fsa.api.user.UtilisateurDTO;
import tiw.fsa.api.user.UtilisateurIncompletException;
import tiw.fsa.api.user.UtilisateurService;

/**
 * Composant d'initialisation de l'application.
 * Vérifie qu'un utilisateur admin est bien présent.
 */
@Component
public class ApiInit implements CommandLineRunner {
    private final UtilisateurService utilisateurService;
    @Value("${tiw.fsa.api.defaultAdminPassword}")
    private String defaultAdminPassword;
    @Value("${tiw.fsa.api.defaultAdminLogin}")
    private String defaultAdminLogin;

    public ApiInit(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        checkAdmin();
    }

    /**
     * Vérifie si l'utilisateur admin est bien présent et le crée sinon.
     * @throws UtilisateurIncompletException bug: l'utilisateur devrait être complet.
     */
    private void checkAdmin() throws UtilisateurIncompletException {
        if (!utilisateurService.utilisateurExists(defaultAdminLogin)) {
            utilisateurService.createOrUpdateUtilisateur(
                    new UtilisateurDTO(defaultAdminLogin,
                            defaultAdminPassword, Utilisateur.ADMIN));
        }
    }
}
