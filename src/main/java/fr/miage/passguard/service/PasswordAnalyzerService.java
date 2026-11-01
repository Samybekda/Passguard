package fr.miage.passguard.service;

import fr.miage.passguard.model.Password;
import fr.miage.passguard.model.PasswordResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service métier Spring encapsulant la logique d'analyse de robustesse.
 * Réutilise et prolonge la logique éprouvée du projet console PassGuard.
 * Traitement 100% sans état (stateless) : aucun mot de passe n'est stocké ni journalisé.
 */
@Service
public class PasswordAnalyzerService {

    public static final int MIN_LENGTH = 8;
    public static final int TOTAL_CRITERIA = 5;

    // Expressions régulières compilées (static final pour des performances optimales)
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~]");

    /**
     * Analyse un objet Password et génère son rapport PasswordResult.
     */
    public PasswordResult analyze(Password password) {
        if (password == null) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être null.");
        }

        List<String> recommendations = new ArrayList<>();
        int score = 0;
        String rawValue = password.getValue();

        // 1. Longueur minimale
        boolean hasMinLength = password.getLength() >= MIN_LENGTH;
        if (hasMinLength) {
            score++;
        } else {
            recommendations.add("Augmenter la longueur à au moins " + MIN_LENGTH + " caractères (actuellement : " + password.getLength() + ").");
        }

        // 2. Lettres minuscules
        boolean hasLowercase = checkPattern(LOWERCASE_PATTERN, rawValue);
        if (hasLowercase) {
            score++;
        } else {
            recommendations.add("Ajouter au moins une lettre minuscule (a-z).");
        }

        // 3. Lettres majuscules
        boolean hasUppercase = checkPattern(UPPERCASE_PATTERN, rawValue);
        if (hasUppercase) {
            score++;
        } else {
            recommendations.add("Ajouter au moins une lettre majuscule (A-Z).");
        }

        // 4. Chiffres
        boolean hasDigit = checkPattern(DIGIT_PATTERN, rawValue);
        if (hasDigit) {
            score++;
        } else {
            recommendations.add("Ajouter au moins un chiffre (0-9).");
        }

        // 5. Caractères spéciaux
        boolean hasSpecialChar = checkPattern(SPECIAL_CHAR_PATTERN, rawValue);
        if (hasSpecialChar) {
            score++;
        } else {
            recommendations.add("Ajouter au moins un caractère spécial (ex: @, #, $, !, etc.).");
        }

        // Évaluation du niveau de sécurité
        String securityLevel;
        String securityLevelEn;

        if (!hasMinLength || score <= 2) {
            securityLevel = "Faible";
            securityLevelEn = "Weak";
        } else if (score == 3 || score == 4) {
            securityLevel = "Moyen";
            securityLevelEn = "Medium";
        } else {
            securityLevel = "Fort";
            securityLevelEn = "Strong";
        }

        return new PasswordResult(
                securityLevel,
                securityLevelEn,
                score,
                TOTAL_CRITERIA,
                recommendations,
                hasMinLength,
                hasLowercase,
                hasUppercase,
                hasDigit,
                hasSpecialChar
        );
    }

    private boolean checkPattern(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
}
