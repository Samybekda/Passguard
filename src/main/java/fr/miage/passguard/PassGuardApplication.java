package fr.miage.passguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application Spring Boot PassGuard.
 * Démarre le serveur web embarqué (Tomcat) sur le port 8080.
 */
@SpringBootApplication
public class PassGuardApplication {

    public static void main(String[] args) {
        SpringApplication.run(PassGuardApplication.class, args);
    }
}
