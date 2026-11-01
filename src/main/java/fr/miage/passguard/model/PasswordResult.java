package fr.miage.passguard.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente le résultat détaillé de l'analyse d'un mot de passe.
 * Fournit les métriques nécessaires au frontend (statut de chaque critère,
 * score global, niveau de sécurité en français et en anglais, recommandations).
 */
public class PasswordResult {

    private final String securityLevel;
    private final String securityLevelEn;
    private final int score;
    private final int totalCriteria;
    private final int percentage;
    private final List<String> recommendations;

    // Détail visuel par critère (pour les badges vert/rouge sur l'interface web)
    private final boolean hasMinLength;
    private final boolean hasLowercase;
    private final boolean hasUppercase;
    private final boolean hasDigit;
    private final boolean hasSpecialChar;

    public PasswordResult(String securityLevel, String securityLevelEn, int score, int totalCriteria,
                          List<String> recommendations, boolean hasMinLength, boolean hasLowercase,
                          boolean hasUppercase, boolean hasDigit, boolean hasSpecialChar) {
        this.securityLevel = securityLevel;
        this.securityLevelEn = securityLevelEn;
        this.score = score;
        this.totalCriteria = totalCriteria;
        this.percentage = (int) Math.round(((double) score / totalCriteria) * 100);
        this.recommendations = new ArrayList<>(recommendations);
        this.hasMinLength = hasMinLength;
        this.hasLowercase = hasLowercase;
        this.hasUppercase = hasUppercase;
        this.hasDigit = hasDigit;
        this.hasSpecialChar = hasSpecialChar;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public String getSecurityLevelEn() {
        return securityLevelEn;
    }

    public int getScore() {
        return score;
    }

    public int getTotalCriteria() {
        return totalCriteria;
    }

    public int getPercentage() {
        return percentage;
    }

    public List<String> getRecommendations() {
        return Collections.unmodifiableList(recommendations);
    }

    public boolean isHasMinLength() {
        return hasMinLength;
    }

    public boolean isHasLowercase() {
        return hasLowercase;
    }

    public boolean isHasUppercase() {
        return hasUppercase;
    }

    public boolean isHasDigit() {
        return hasDigit;
    }

    public boolean isHasSpecialChar() {
        return hasSpecialChar;
    }
}
