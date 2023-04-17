package tiw.fsa.api.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents where a user is the wrong one.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenAccessException extends Exception {

    public ForbiddenAccessException(String message) {
        super(message);
    }

}
