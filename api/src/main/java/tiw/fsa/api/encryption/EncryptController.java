package tiw.fsa.api.encryption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiw.fsa.api.security.ForbiddenAccessException;
import tiw.fsa.api.security.SecurityUtils;

/**
 * Point d'accès pour la gestion des chiffrements
 */
@RestController
@RequestMapping("/crypt")
public class EncryptController {
    private final EncryptService encryptService;

    public EncryptController(EncryptService encryptService) {
        this.encryptService = encryptService;
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
