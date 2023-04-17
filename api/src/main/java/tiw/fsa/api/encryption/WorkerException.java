package tiw.fsa.api.encryption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class WorkerException extends Exception{
    public WorkerException() {
    }

    public WorkerException(String message) {
        super(message);
    }

}
