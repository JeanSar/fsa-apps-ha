package tiw.fsa.api.encryption;

import jakarta.persistence.*;
import tiw.fsa.api.user.Utilisateur;

import java.io.Serializable;
import java.util.Objects;

/**
 * Représentation en base de données d'une clé
 */
@Entity
@IdClass(Key.IdC.class)
public class Key {
    public static class IdC implements Serializable {
        public Utilisateur utilisateur;
        public String id;

        public IdC() {
        }

        public IdC(Utilisateur utilisateur, String id) {
            this.utilisateur = utilisateur;
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdC idC = (IdC) o;
            return utilisateur.equals(idC.utilisateur) && id.equals(idC.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(utilisateur, id);
        }
    }

    @Id
    @ManyToOne
    private Utilisateur utilisateur;
    @Id
    private String id;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return utilisateur.equals(key.utilisateur) && id.equals(key.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utilisateur, id);
    }
}
