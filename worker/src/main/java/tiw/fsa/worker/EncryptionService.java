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
        log.debug("Starting to encrypt with {}", keyName);
        String key = keyName;
        byte[] dataB = Base64.getDecoder().decode(data);
        byte[] keyB = key.getBytes();
        byte[] dataC = new byte[dataB.length];
        for (int i = 0; i < dataB.length; i++) {
            dataC[i] = (byte) (dataB[i] + keyB[i % keyB.length]);
        }
        try {
            lock.sleep(latence);
        } catch (InterruptedException e) {
            throw new EncryptException(e);
        }
        log.debug("Encryption finished");
        return new String(Base64.getEncoder().encode(dataC), StandardCharsets.ISO_8859_1);
    }

    public String decrypt(String keyName, String data) throws DecryptException {
        log.debug("Starting to decrypt with {}", keyName);
        String key = keyName;
        byte[] dataB = Base64.getDecoder().decode(data);
        byte[] keyB = key.getBytes();
        byte[] dataC = new byte[dataB.length];
        for (int i = 0; i < dataB.length; i++) {
            dataC[i] = (byte) (dataB[i] - keyB[i % keyB.length]);
        }
        try {
            lock.sleep(latence);
        } catch (InterruptedException e) {
            throw new DecryptException(e);
        }
        log.debug("Decryption finished");
        return new String(Base64.getEncoder().encode(dataC), StandardCharsets.ISO_8859_1);
    }

    private static class Sleeper {
        /**
         * Méthode qui simule le calcul. Afin de simuler une charge processeur en multithreading
         * dans un cadre monocœur, on rend la méthode synchronized, ce qui va poser un verrou pour
         * empêcher deux threads de l'utiliser simultanément.
         *
         * @param time nombre de milisecond à attendre
         * @throws InterruptedException si un problème survient pendant le thread.sleep
         */
        public synchronized void sleep(long time) throws InterruptedException {
            log.debug("Compute simulation in {}", this);
            Thread.sleep(time); // Simulation de temps de calcul
        }
    }

    private final static Sleeper lock = new Sleeper();
}
