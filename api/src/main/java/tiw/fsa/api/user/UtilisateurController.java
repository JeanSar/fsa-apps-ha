package tiw.fsa.api.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiw.fsa.api.security.ApiUserDetails;
import tiw.fsa.api.security.ForbiddenAccessException;
import tiw.fsa.api.security.SecurityUtils;

/**
 * Point d'entrée de l'API pour les utilisateurs.
 */
@RestController
@RequestMapping(path = "/user")
public class UtilisateurController {
    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurController.class);

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    /**
     * Récupères les information sur un utilisateur
     * @param login le login de l'utilisateur
     * @return une description de l'utilisateur
     * @throws UtilisateurNotFoundException si l'utilisateur n'existe pas
     */
    @GetMapping("/{login}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UtilisateurDTO getUtilisateur(@PathVariable(name = "login") String login)
            throws UtilisateurNotFoundException {
        return utilisateurService.getUtilisateur(login);
    }

    /**
     * Mets à jour ou crée un utilisateur.
     * @param utilisateurDTO la description de l'utilisateur
     * @return les informations sur l'utilisateur
     * @throws UtilisateurIncompletException si il manque des informations à mettre à jour
     * @throws ForbiddenAccessException si un utilisateur non admin essaie de mettre à jour ou de créer un autre utilisateur.
     */
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UtilisateurDTO createOrUpdateUtilisateur
            (@RequestBody UtilisateurDTO utilisateurDTO) throws UtilisateurIncompletException, ForbiddenAccessException {
        ApiUserDetails principal = SecurityUtils.getPrincipal();
        if (!principal.isAdmin()) {
            LOG.debug("is not admin");
            if (principal.getUsername().equals(utilisateurDTO.login())) {
                LOG.debug("is same user");
                // only admin can change role
                utilisateurDTO = new UtilisateurDTO(utilisateurDTO.login(), utilisateurDTO.password(), null);
            } else {
                LOG.debug("Attempt to post to user with {} role", principal.getRole());
                // only admin or the user him/herself can change details of a user
                throw new ForbiddenAccessException("User "+principal.getUsername()+" cannot modify user "+utilisateurDTO.login());
            }
        }
        return utilisateurService.createOrUpdateUtilisateur(utilisateurDTO);
    }
}
