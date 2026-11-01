package fr.miage.passguard.dto;

import fr.miage.passguard.model.PasswordResult;

import java.util.List;

/**
 * Objet de transfert de données (DTO) retourné par l'API REST.
 * Transmet au frontend l'ensemble des résultats nécessaires à l'affichage dynamique.
 */
public class PasswordAnalysisResponse {

    private String securityLevel;
    private String securityLevelEn;
    private int score;
    private int totalCriteria;
    private int percentage;
    private List<String> recommendations;
    private CriteriaDto criteria;

    public PasswordAnalysisResponse() {}

    public static PasswordAnalysisResponse fromResult(PasswordResult result) {
        PasswordAnalysisResponse resp = new PasswordAnalysisResponse();
        resp.setSecurityLevel(result.getSecurityLevel());
        resp.setSecurityLevelEn(result.getSecurityLevelEn());
        resp.setScore(result.getScore());
        resp.setTotalCriteria(result.getTotalCriteria());
        resp.setPercentage(result.getPercentage());
        resp.setRecommendations(result.getRecommendations());

        CriteriaDto c = new CriteriaDto();
        c.setMinLength(result.isHasMinLength());
        c.setLowercase(result.isHasLowercase());
        c.setUppercase(result.isHasUppercase());
        c.setDigit(result.isHasDigit());
        c.setSpecialChar(result.isHasSpecialChar());
        resp.setCriteria(c);

        return resp;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getSecurityLevelEn() {
        return securityLevelEn;
    }

    public void setSecurityLevelEn(String securityLevelEn) {
        this.securityLevelEn = securityLevelEn;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalCriteria() {
        return totalCriteria;
    }

    public void setTotalCriteria(int totalCriteria) {
        this.totalCriteria = totalCriteria;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public CriteriaDto getCriteria() {
        return criteria;
    }

    public void setCriteria(CriteriaDto criteria) {
        this.criteria = criteria;
    }

    public static class CriteriaDto {
        private boolean minLength;
        private boolean lowercase;
        private boolean uppercase;
        private boolean digit;
        private boolean specialChar;

        public boolean isMinLength() {
            return minLength;
        }

        public void setMinLength(boolean minLength) {
            this.minLength = minLength;
        }

        public boolean isLowercase() {
            return lowercase;
        }

        public void setLowercase(boolean lowercase) {
            this.lowercase = lowercase;
        }

        public boolean isUppercase() {
            return uppercase;
        }

        public void setUppercase(boolean uppercase) {
            this.uppercase = uppercase;
        }

        public boolean isDigit() {
            return digit;
        }

        public void setDigit(boolean digit) {
            this.digit = digit;
        }

        public boolean isSpecialChar() {
            return specialChar;
        }

        public void setSpecialChar(boolean specialChar) {
            this.specialChar = specialChar;
        }
    }
}
