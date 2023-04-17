package tiw.fsa.api.encryption;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tiw.fsa.api.user.UtilisateurNotFoundException;
import tiw.fsa.api.user.UtilisateurRepository;

/**
 * Service gérant les clés de chiffrement.
 */
@Service
public class KeyService {
    private final KeyRepository keyRepository;
    private final UtilisateurRepository utilisateurRepository;

    public KeyService(KeyRepository keyRepository, UtilisateurRepository utilisateurRepository) {
        this.keyRepository = keyRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Créée une clé pour un utilisateur
     *
     * @param login   le nom de l'utilisateur
     * @param keyname le nom de la clé
     * @throws UtilisateurNotFoundException si l'utilisateur n'existe pas
     */
    public void createKey(String login, String keyname) throws UtilisateurNotFoundException {
        var uOpt = utilisateurRepository.findById(login);
        if (uOpt.isPresent()) {
            var id = new Key.IdC(uOpt.get(), keyname);
            if (!keyRepository.existsById(id)) {
                // If key does not exist
                Key key = new Key();
                key.setId(keyname);
                key.setUtilisateur(uOpt.get());
                keyRepository.save(key);
            }
        } else { // no such user
            throw new UtilisateurNotFoundException();
        }
    }

    /**
     * Récupère les clés d'un utilisateur
     *
     * @param login le nom de l'utilisateur
     * @return un tableau des noms de clés de cet utilisateur
     * @throws UtilisateurNotFoundException si l'utilisateur n'existe pas
     */
    public String[] getKeys(String login) throws UtilisateurNotFoundException {
        if (!utilisateurRepository.existsById(login)) {
            throw new UtilisateurNotFoundException();
        }
        var keys = keyRepository.getKeyIdsByLogin(login);
        return keys.toArray(new String[keys.size()]);
    }

    /**
     * Checks if a key exists
     *
     * @param login   the username for this key
     * @param keyname the name of the key
     * @return true if the key exists for this user
     */
    public boolean keyExists(String login, String keyname) {
        return keyRepository.existsByIdAndUtilisateur_Login(keyname, login);
    }

    /**
     * Delete all keys from some user
     * @param login the username of the user
     */
    @Transactional
    public void deleteUserKeys(String login) {
        keyRepository.deleteAllByUtilisateur_Login(login);
    }
}
