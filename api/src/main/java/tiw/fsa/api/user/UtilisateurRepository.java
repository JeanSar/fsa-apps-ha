package tiw.fsa.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring pour les utilisateurs.
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
}
