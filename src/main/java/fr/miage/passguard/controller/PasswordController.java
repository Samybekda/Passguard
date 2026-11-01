package fr.miage.passguard.controller;

import fr.miage.passguard.dto.PasswordAnalysisRequest;
import fr.miage.passguard.dto.PasswordAnalysisResponse;
import fr.miage.passguard.model.Password;
import fr.miage.passguard.model.PasswordResult;
import fr.miage.passguard.service.PasswordAnalyzerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * Contrôleur REST pour l'analyse de robustesse des mots de passe.
 * Expose les points de terminaison HTTP consommés par le frontend.
 *
 * Respect des règles de cybersécurité et confidentialité :
 * - Aucun mot de passe n'est stocké en base de données ou en session.
 * - Aucun mot de passe n'est consigné dans les logs.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PasswordController {

    private final PasswordAnalyzerService analyzerService;

    public PasswordController(PasswordAnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    /**
     * Point d'accès POST /api/analyze
     * Reçoit le mot de passe en JSON, effectue l'analyse côté serveur et retourne le résultat.
     */
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzePassword(@RequestBody(required = false) PasswordAnalysisRequest request) {
        if (request == null || request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Veuillez fournir un mot de passe à analyser."));
        }

        try {
            // Création de l'objet Password encapsulé
            Password password = new Password(request.getPassword());

            // Analyse métier via le service
            PasswordResult result = analyzerService.analyze(password);

            // Transformation en DTO pour le frontend
            PasswordAnalysisResponse response = PasswordAnalysisResponse.fromResult(result);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Point d'accès GET /api/health
     * Vérification de l'état du serveur backend.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "PassGuard API"));
    }
}
