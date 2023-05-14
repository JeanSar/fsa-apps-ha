package tiw.fsa.api.probe;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tiw.fsa.api.encryption.EncryptService;
import tiw.fsa.api.encryption.WorkerException;

@RestController()
public class ProbeController {
    private final EncryptService encryptService;

    public ProbeController(EncryptService encryptService) {
        this.encryptService = encryptService;
    }

    @GetMapping("/liveness")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean isAlive() {
        return true;
    }

    @GetMapping("/readiness")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean isWorkerAlive() throws WorkerException {
        return encryptService.getWorker("liveness");
    }
}
