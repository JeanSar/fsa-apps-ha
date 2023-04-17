package tiw.fsa.api.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Représente le cas où on tente de créer un utilisateur sans mot de passe.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UtilisateurIncompletException extends Exception{

    public UtilisateurIncompletException(String message) {
        super(message);
    }

}
