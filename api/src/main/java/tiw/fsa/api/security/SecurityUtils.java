package tiw.fsa.api.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * Information about currently logged user
     *
     * @return userdetails on the currently logged user
     */
    public static ApiUserDetails getPrincipal() {
        // see https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-securitycontextholder
        return (ApiUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    /**
     * Checks the currently logged user wrt the given login.
     * If the login does not match the current username and is the current user is not an admin, raises an exception
     *
     * @param login the expected username
     * @throws ForbiddenAccessException if the current user can not access the login restricted resource
     */
    public static void checkCurrentUserOrAdmin(String login) throws ForbiddenAccessException {
        var principal = getPrincipal();
        if (!principal.getUsername().equals(login) && !principal.isAdmin()) {
            throw new ForbiddenAccessException("User " + principal.getUsername() + " cannot access resources of " + login);
        }
    }

    /**
     * Checks the currently logged user wrt the given login.
     * If the login does not match the current username, raises an exception
     *
     * @param login the expected username
     * @throws ForbiddenAccessException if the current user can not access the login restricted resource
     */
    public static void checkCurrentUser(String login) throws ForbiddenAccessException {
        var principal = getPrincipal();
        if (!principal.getUsername().equals(login)) {
            throw new ForbiddenAccessException("User " + principal.getUsername() + " cannot access resources of " + login);
        }
    }
}
