package tiw.fsa.worker;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crypt/{keyName}")
public class EncryptionController {
    private final EncryptionService encryptionService;

    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping(path = "/encrypt")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String encryptData(@RequestBody String data, @PathVariable(name = "keyName") String keyName) throws EncryptException {
        return encryptionService.encrypt(keyName, data);
    }

    @PostMapping(path = "/decrypt")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String decryptData(@RequestBody String data, @PathVariable(name="keyName") String keyName) throws DecryptException {
        return encryptionService.decrypt(keyName, data);
    }
}
