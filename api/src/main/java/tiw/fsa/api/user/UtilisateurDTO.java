package tiw.fsa.api.user;

/**
 * DTO représentant un utilisateur
 * @param login
 * @param password
 * @param role doit être Utilisateur.ADMIN ou Utilisateur.NORMAL
 */
public record UtilisateurDTO(String login, String password, String role) {
    public static UtilisateurDTO fromUtilisateur(Utilisateur utilisateur) {
        return new UtilisateurDTO(utilisateur.getLogin(), null, utilisateur.getRole());
    }
}
