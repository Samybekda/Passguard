package fr.miage.passguard.model;

/**
 * Représente un mot de passe soumis pour analyse.
 * Cette classe métier respecte le principe d'encapsulation (POO).
 * La valeur est immuable et protégée.
 */
public class Password {

    private final String value;

    public Password(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être null.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getLength() {
        return value.length();
    }

    public boolean isEmpty() {
        return value.trim().isEmpty();
    }

    @Override
    public String toString() {
        // Ne jamais afficher le mot de passe en clair dans un toString pour éviter toute fuite dans les logs
        return "[PROTECTED_PASSWORD]";
    }
}
