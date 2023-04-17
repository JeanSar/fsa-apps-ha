package tiw.fsa.worker;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkerApplicationTests {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    void contextLoads() {
    }

    @Test
    void testEncodeDecode() throws EncryptException, DecryptException {
        String keyName = "unecle";
        String data = Base64.encodeBase64String("des données à encoder".getBytes(StandardCharsets.UTF_8));
        String chiffre = encryptionService.encrypt(keyName, data);
        String clair = encryptionService.decrypt(keyName, chiffre);
        assertEquals(data, clair);
    }

}
