package tiw.fsa.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configure Spring Security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic() // Active l'authentification par mot de passe avec HTTP Basic
                .and()
                .authorizeHttpRequests(autorize ->
                        autorize
                                // accès à /user, /key et /crypt OK pour les utilisateurs authentifiés
                                .requestMatchers("/user/**", "/key/**", "/crypt/**").authenticated()
                                // accès à /error ok pour tout le monde
                                .requestMatchers("/error").permitAll());
        http.csrf().disable(); // FIXME: empêche l'accès à l'API par POST via un client hors navigateur, voir si cela a du sens de le réactiver
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
