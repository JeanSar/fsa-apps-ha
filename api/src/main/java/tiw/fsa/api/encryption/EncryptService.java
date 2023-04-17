package tiw.fsa.api.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service implémentant le chiffrement de données
 */
@Service
public class EncryptService {
    private static final Logger LOG = LoggerFactory.getLogger(EncryptService.class);
    private final KeyService keyService;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${tiw.fsa.api.worker.url}")
    private String workerUrl;

    public EncryptService(KeyService keyService) {
        this.keyService = keyService;
    }

    /**
     * Calls a worker for encrypt/decrypt
     *
     * @param method  either encrypt or decrypt
     * @param login   the login's key
     * @param keyname the name of the key
     * @param data    the data to transmit to worker (must be base64 encoded)
     * @return the data transformed by the worker
     * @throws NoSuchKeyException if the key does not exist
     * @throws WorkerException    if the request to the worker failed somehow
     */
    private String callWorker(String method, String login, String keyname, String data) throws NoSuchKeyException, WorkerException {
        if (!keyService.keyExists(login, keyname)) {
            throw new NoSuchKeyException("La cle " + keyname + " pour l'utilisateur " + login + " n'existe pas.");
        }
        String url = workerUrl + "/crypt/{keyname}/" + method;
        var result = restTemplate.postForEntity(url, data, String.class, keyname);
        var status = result.getStatusCode();
        if (status.is5xxServerError() || status.is4xxClientError()) {
            LOG.error("Request to failed with code {}", status.value());
            throw new WorkerException();
        }
        if (data.length() > 0 && (!result.hasBody() || result.getBody().length() == 0)) {
            LOG.error("Got an empty result from worker");
            throw new WorkerException("Empty result");
        }
        return result.getBody();
    }


    /**
     * Calls a worker for encrypting data
     *
     * @param login   the login's key
     * @param keyname the name of the key
     * @param data    the data to transmit to worker (must be base64 encoded)
     * @return the data transformed by the worker
     * @throws NoSuchKeyException if the key does not exist
     * @throws WorkerException    if the request to the worker failed somehow
     */
    public String encrypt(String login, String keyname, String data) throws NoSuchKeyException, WorkerException {
        return callWorker("encrypt", login, keyname, data);
    }

    /**
     * Calls a worker for decrypting data
     *
     * @param login   the login's key
     * @param keyname the name of the key
     * @param data    the data to transmit to worker (must be base64 encoded)
     * @return the data transformed by the worker
     * @throws NoSuchKeyException if the key does not exist
     * @throws WorkerException    if the request to the worker failed somehow
     */
    public String decrypt(String login, String keyname, String data) throws NoSuchKeyException, WorkerException {
        return callWorker("decrypt", login, keyname, data);
    }
}
