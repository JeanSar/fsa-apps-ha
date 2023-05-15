package tiw.fsa.api.encryption;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiw.fsa.api.security.ForbiddenAccessException;
import tiw.fsa.api.security.SecurityUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Point d'accès pour la gestion des chiffrements
 */
@RestController
@RequestMapping("/crypt")
public class EncryptController {
    private final EncryptService encryptService;
    @Autowired
    private MeterRegistry meterRegistry;
    private Gauge gaugeEncrypt;
    private List<String> encryptList = new ArrayList<String>();

    public EncryptController(EncryptService encryptService) {
        this.encryptService = encryptService;
    }

    @PostConstruct
    public void init() {

        gaugeEncrypt = Gauge.builder("encrypt_api_request", encryptList, List::size)
                        .description("Gauge of encrypt request")
                        .register(meterRegistry);
    }
    /**
     * Encrypts data using the user's key. Data should be encoded using base64 encoding.
     *
     * @param login   the username
     * @param keyname the name of the key
     * @param data    the data to encrypt
     * @return the encrypted data
     */
    @PostMapping("/{login}/{keyname}/encrypt")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String encrypt(@PathVariable("login") String login, @PathVariable("keyname") String keyname, @RequestBody String data) throws ForbiddenAccessException, NoSuchKeyException, WorkerException {
        encryptList.add(login);
        gaugeEncrypt.measure();
        SecurityUtils.checkCurrentUser(login);
        return encryptService.encrypt(login, keyname, data);
    }


    /**
     * Decrypts data using the user's key. Data should be encoded using base64 encoding.
     *
     * @param login   the username
     * @param keyname the name of the key
     * @param data    the data to encrypt
     * @return the encrypted data
     */
    @PostMapping("/{login}/{keyname}/decrypt")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String decrypt(@PathVariable("login") String login, @PathVariable("keyname") String keyname, @RequestBody String data) throws ForbiddenAccessException, NoSuchKeyException, WorkerException {
        SecurityUtils.checkCurrentUser(login);
        return encryptService.decrypt(login, keyname, data);
    }
}
