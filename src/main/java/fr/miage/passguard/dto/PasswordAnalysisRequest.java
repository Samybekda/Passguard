package fr.miage.passguard.dto;

/**
 * Objet de transfert de données (DTO) pour la requête d'analyse.
 * Reçoit la saisie utilisateur depuis le frontend via un corps JSON.
 */
public class PasswordAnalysisRequest {

    private String password;

    public PasswordAnalysisRequest() {}

    public PasswordAnalysisRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
