package tiw.fsa.api.encryption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface KeyRepository extends JpaRepository<Key, Key.IdC> {
    @Query("select k.id from Key k join k.utilisateur u where u.login = :login")
    Collection<String> getKeyIdsByLogin(@Param("login") String login);

    boolean existsByIdAndUtilisateur_Login(String Id, String login);

    void deleteAllByUtilisateur_Login(String login);
}
