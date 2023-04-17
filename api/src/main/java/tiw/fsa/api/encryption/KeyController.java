package tiw.fsa.api.encryption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiw.fsa.api.security.ForbiddenAccessException;
import tiw.fsa.api.security.SecurityUtils;
import tiw.fsa.api.user.UtilisateurNotFoundException;

/**
 * Point d'accès pour la gestion des clés
 */
@RestController
@RequestMapping("/key")
public class KeyController {
    private final KeyService keyService;

    public KeyController(KeyService keyService) {
        this.keyService = keyService;
    }

    @PostMapping(path = "/{login}/{keyname}")
    @ResponseStatus(HttpStatus.OK)
    public void createKey(
            @PathVariable("login") String login,
            @PathVariable("keyname") String keyname)
            throws UtilisateurNotFoundException, ForbiddenAccessException {
        SecurityUtils.checkCurrentUserOrAdmin(login);
        keyService.createKey(login, keyname);
    }

    @GetMapping(path = "/{login}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String[] getKeys(@PathVariable("login") String login) throws ForbiddenAccessException, UtilisateurNotFoundException {
        SecurityUtils.checkCurrentUserOrAdmin(login);
        return keyService.getKeys(login);
    }

}
