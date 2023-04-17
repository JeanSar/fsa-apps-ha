package tiw.fsa.worker;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EncryptException extends Exception {

    public EncryptException(Throwable cause) {
        super(cause);
    }

}
