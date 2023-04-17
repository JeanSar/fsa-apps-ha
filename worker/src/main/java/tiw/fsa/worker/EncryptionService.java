package tiw.fsa.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
public class EncryptionService {
    private static final Logger log = LoggerFactory.getLogger(EncryptionService.class);

    @Value("${tiw.fsa.worker.latence}")
    private long latence;

    public String encrypt(String keyName, String data) throws EncryptException {
        String key = keyName;
        byte[] dataB = Base64.getDecoder().decode(data);
        byte[] keyB = key.getBytes();
        byte[] dataC = new byte[dataB.length];
        for (int i = 0; i < dataB.length; i++) {
            dataC[i] = (byte) (dataB[i] + keyB[i % keyB.length]);
        }
        try {
            Thread.sleep(latence); // Simulation de temps de calcul
        } catch (InterruptedException e) {
            throw new EncryptException(e);
        }
        return new String(Base64.getEncoder().encode(dataC), StandardCharsets.ISO_8859_1);
    }

    public String decrypt(String keyName, String data) throws DecryptException {
        String key = keyName;
        byte[] dataB = Base64.getDecoder().decode(data);
        byte[] keyB = key.getBytes();
        byte[] dataC = new byte[dataB.length];
        for (int i = 0; i < dataB.length; i++) {
            dataC[i] = (byte) (dataB[i] - keyB[i % keyB.length]);
        }
        try {
            Thread.sleep(latence); // Simulation de temps de calcul
        } catch (InterruptedException e) {
            throw new DecryptException(e);
        }
        return new String(Base64.getEncoder().encode(dataC), StandardCharsets.ISO_8859_1);
    }
}
