package tiw.fsa.worker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crypt/{keyName}")
public class EncryptionController {
    @Autowired
    private MeterRegistry meterRegistry;
    private Counter encryptInitCounter;
    private Counter encryptEndCounter;
    private Counter decryptInitCounter;
    private Counter decryptEndCounter;

    private final EncryptionService encryptionService;

    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostConstruct
    public void init() {
        encryptInitCounter =
                Counter.builder("encrypt_init_worker_request")
                        .description("Number of encrypt request initiated on worker")
                        .register(meterRegistry);
        decryptInitCounter =
                Counter.builder("decrypt_init_worker_request")
                        .description("Number of decrypt request initiated on worker")
                        .register(meterRegistry);

        encryptEndCounter =
                Counter.builder("encrypt_end_worker_request")
                        .description("Number of encrypt request terminated on worker")
                        .register(meterRegistry);
        decryptEndCounter =
                Counter.builder("decrypt_end_worker_request")
                        .description("Number of decrypt request terminated on worker")
                        .register(meterRegistry);
    }
    @PostMapping(path = "/encrypt")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String encryptData(@RequestBody String data, @PathVariable(name = "keyName") String keyName) throws EncryptException {
        encryptInitCounter.increment();
        String res = encryptionService.encrypt(keyName, data);
        encryptEndCounter.increment();
        return res;
    }

    @PostMapping(path = "/decrypt")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String decryptData(@RequestBody String data, @PathVariable(name="keyName") String keyName) throws DecryptException {
        decryptInitCounter.increment();
        String res = encryptionService.decrypt(keyName, data);
        decryptEndCounter.increment();
        return res;
    }
}
