# PassGuard - Analyseur de Robustesse des Mots de Passe

PassGuard est une application web dédiée à l'évaluation cryptographique de la robustesse des mots de passe. Elle repose sur un backend Java Spring Boot 3 exposant une API REST et un client web interactif développé en HTML5, CSS3 et JavaScript natif.

---

## 1. Description fonctionnelle

L'application permet d'analyser en temps réel un mot de passe soumis par l'utilisateur selon cinq critères fondamentaux :

1. Longueur minimale : au moins 8 caractères.
2. Présence de lettres minuscules : au moins une lettre entre a et z.
3. Présence de lettres majuscules : au moins une lettre entre A et Z.
4. Présence de chiffres : au moins un chiffre entre 0 et 9.
5. Présence de caractères spéciaux : au moins un symbole de ponctuation ou caractère non alphanumérique.

### Échelle de robustesse
- Fort : les 5 critères sont validés (score de 5/5).
- Moyen : 3 ou 4 critères sont validés, avec obligatoirement une longueur supérieure ou égale à 8 caractères.
- Faible : moins de 3 critères validés ou longueur strictement inférieure à 8 caractères.

---

## 2. Architecture logicielle

Le projet adopte une séparation stricte des responsabilités (Single Responsibility Principle) et une architecture en couches :

```text
passguard/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── fr/miage/passguard/
│   │   │       ├── PassGuardApplication.java        # Point d'entrée de l'application Spring Boot
│   │   │       ├── controller/
│   │   │       │   └── PasswordController.java      # Contrôleur REST (/api/analyze)
│   │   │       ├── service/
│   │   │       │   └── PasswordAnalyzerService.java # Moteur d'analyse basé sur Pattern et Matcher
│   │   │       ├── model/
│   │   │       │   ├── Password.java                # Encapsulation et intégrité de la donnée
│   │   │       │   └── PasswordResult.java          # Modèle de résultat et métriques associées
│   │   │       └── dto/
│   │   │           ├── PasswordAnalysisRequest.java # DTO d'entrée (JSON)
│   │   │           └── PasswordAnalysisResponse.java# DTO de sortie (JSON)
│   │   └── resources/
│   │       ├── application.properties               # Paramétrage du serveur embarqué
│   │       └── static/                              # Ressources statiques servies par Spring Boot
│   │           ├── index.html                       # Structure de l'interface utilisateur
│   │           ├── style.css                        # Feuille de styles responsive
│   │           └── app.js                           # Client Fetch et manipulation du DOM
│   └── test/
│       └── java/
│           └── fr/miage/passguard/
│               └── PassGuardApplicationTests.java   # Tests d'intégration et unitaires
├── mvnw & mvnw.cmd                                  # Wrapper Maven pour la compilation
├── pom.xml                                          # Gestion des dépendances Maven
└── README.md                                        # Documentation technique
```

---

## 3. Rôle des composants

### Couche Modèle
- Password : encapsule la valeur brute du mot de passe. Attribut privé immuable avec contrôle de non-nullité. La méthode toString() retourne une valeur masquée pour éviter toute fuite d'information.
- PasswordResult : stocke le verdict de l'analyse, le score obtenu, le pourcentage, l'état booléen de chaque critère et la liste des recommandations d'amélioration.

### Couche Service
- PasswordAnalyzerService : contient les expressions régulières pré-compilées (Pattern et Matcher) sous forme de constantes statiques pour optimiser les performances d'exécution. Applique les règles de validation et instancie l'objet PasswordResult.

### Couche Contrôleur et DTO
- PasswordAnalysisRequest : transporte la saisie utilisateur au format JSON.
- PasswordAnalysisResponse : structure les résultats retournés au format JSON (score, niveau, statut par critère et recommandations).
- PasswordController : expose les routes HTTP de l'API REST, valide les requêtes et transmet les données au service.

### Couche Client (Frontend)
- index.html : interface sémantique sans framework lourd.
- style.css : styles responsives adaptés aux écrans mobiles et ordinateurs de bureau.
- app.js : gestionnaire d'événements, communication asynchrone avec l'API REST via fetch, mise à jour dynamique de la jauge et des indicateurs visuels.

---

## 4. Spécification de l'API REST

### POST /api/analyze

Analyse la robustesse d'un mot de passe.

#### Requête
- Méthode : POST
- En-tête : Content-Type: application/json
- Corps :
```json
{
  "password": "MonMotDePasse123!"
}
```

#### Réponse (200 OK)
```json
{
  "securityLevel": "Fort",
  "securityLevelEn": "Strong",
  "score": 5,
  "totalCriteria": 5,
  "percentage": 100,
  "recommendations": [],
  "criteria": {
    "minLength": true,
    "lowercase": true,
    "uppercase": true,
    "digit": true,
    "specialChar": true
  }
}
```

### GET /api/health

Vérifie la disponibilité du service.

#### Réponse (200 OK)
```json
{
  "status": "UP",
  "service": "PassGuard API"
}
```

---

## 5. Principes de sécurité et confidentialité

- Traitement sans persistance : aucun mot de passe n'est enregistré dans une base de données ou un système de fichiers.
- Architecture sans état (stateless) : aucune session utilisateur n'est conservée côté serveur.
- Absence de journalisation sensible : les mots de passe ne sont jamais inscrits dans les journaux d'application (logs).
- Contrôle de la visibilité côté client : possibilité d'afficher ou masquer la saisie, ainsi que de vider le champ à l'écran tout en conservant le résultat affiché.

---

## 6. Installation et exécution

### Prérequis
- Java Development Kit (JDK) version 17 ou supérieure.
- Git.

### Cloner le dépôt
```bash
git clone https://github.com/Samybekda/Passguard.git
cd Passguard
```

### Lancer les tests automatisés
```bash
./mvnw test
```
Sur Windows :
```powershell
.\mvnw.cmd test
```

### Démarrer le serveur
```bash
./mvnw spring-boot:run
```
Sur Windows :
```powershell
.\mvnw.cmd spring-boot:run
```

Le serveur web démarre par défaut sur le port 8080. L'application est accessible à l'adresse :
http://localhost:8080
