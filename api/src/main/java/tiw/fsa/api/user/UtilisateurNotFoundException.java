package tiw.fsa.api.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Représente le cas où un utilisateur n'a pas été trouvé.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UtilisateurNotFoundException extends Exception {
    public UtilisateurNotFoundException() {
    }

}
