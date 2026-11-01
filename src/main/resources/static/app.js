/**
 * PassGuard – Client Frontend JavaScript
 * Gère les interactions utilisateur et la communication asynchrone (AJAX / Fetch)
 * avec l'API REST Spring Boot (/api/analyze).
 */

document.addEventListener('DOMContentLoaded', () => {

    // Éléments du DOM
    const passwordInput = document.getElementById('passwordInput');
    const toggleVisibilityBtn = document.getElementById('toggleVisibilityBtn');
    const eyeIcon = document.getElementById('eyeIcon');
    const eyeSlashIcon = document.getElementById('eyeSlashIcon');
    const clearInputBtn = document.getElementById('clearInputBtn');
    const analyzeBtn = document.getElementById('analyzeBtn');
    const resetAfterAnalysisBtn = document.getElementById('resetAfterAnalysisBtn');

    // Éléments d'affichage des résultats
    const resultCard = document.getElementById('resultCard');
    const emptyState = document.getElementById('emptyState');
    const securityBadge = document.getElementById('securityBadge');
    const scoreText = document.getElementById('scoreText');
    const percentageText = document.getElementById('percentageText');
    const progressBar = document.getElementById('progressBar');
    const recommendationsList = document.getElementById('recommendationsList');

    // Éléments des 5 critères
    const critLength = document.getElementById('crit-length');
    const critLower = document.getElementById('crit-lower');
    const critUpper = document.getElementById('crit-upper');
    const critDigit = document.getElementById('crit-digit');
    const critSpecial = document.getElementById('crit-special');

    let debounceTimer = null;

    // =========================================================================
    // 1. Gestion de la visibilité du mot de passe (Afficher / Masquer)
    // =========================================================================
    toggleVisibilityBtn.addEventListener('click', () => {
        const isPassword = passwordInput.getAttribute('type') === 'password';
        
        if (isPassword) {
            passwordInput.setAttribute('type', 'text');
            eyeIcon.classList.add('hidden');
            eyeSlashIcon.classList.remove('hidden');
            toggleVisibilityBtn.setAttribute('title', 'Masquer le mot de passe');
        } else {
            passwordInput.setAttribute('type', 'password');
            eyeIcon.classList.remove('hidden');
            eyeSlashIcon.classList.add('hidden');
            toggleVisibilityBtn.setAttribute('title', 'Afficher le mot de passe');
        }
    });

    // =========================================================================
    // 2. Gestion du bouton pour effacer la saisie
    // =========================================================================
    passwordInput.addEventListener('input', () => {
        const value = passwordInput.value;
        if (value.length > 0) {
            clearInputBtn.classList.remove('hidden');
        } else {
            clearInputBtn.classList.add('hidden');
            resetToEmptyState();
        }

        // Analyse en direct avec debounce (délai de 250ms)
        clearTimeout(debounceTimer);
        if (value.trim().length > 0) {
            debounceTimer = setTimeout(() => {
                performAnalysis(value);
            }, 250);
        }
    });

    clearInputBtn.addEventListener('click', () => {
        passwordInput.value = '';
        clearInputBtn.classList.add('hidden');
        resetToEmptyState();
        passwordInput.focus();
    });

    // Effacer le mot de passe tout en conservant le résultat affiché
    resetAfterAnalysisBtn.addEventListener('click', () => {
        passwordInput.value = '';
        clearInputBtn.classList.add('hidden');
    });

    // =========================================================================
    // 3. Déclencheurs d'analyse
    // =========================================================================
    analyzeBtn.addEventListener('click', () => {
        const value = passwordInput.value;
        if (!value || value.trim().length === 0) {
            passwordInput.focus();
            return;
        }
        performAnalysis(value);
    });

    passwordInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            const value = passwordInput.value;
            if (value && value.trim().length > 0) {
                performAnalysis(value);
            }
        }
    });

    // =========================================================================
    // 4. Appel de l'API REST Spring Boot (/api/analyze)
    // =========================================================================
    async function performAnalysis(rawPassword) {
        try {
            analyzeBtn.classList.add('loading');

            const response = await fetch('/api/analyze', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ password: rawPassword })
            });

            if (!response.ok) {
                const errData = await response.json();
                console.error('Erreur retournée par le serveur :', errData);
                return;
            }

            const data = await response.json();
            renderAnalysisResult(data);

        } catch (error) {
            console.error('Erreur réseau lors de la communication avec l\'API Spring Boot :', error);
        } finally {
            analyzeBtn.classList.remove('loading');
        }
    }

    // =========================================================================
    // 5. Mise à jour dynamique de l'interface utilisateur
    // =========================================================================
    function renderAnalysisResult(data) {
        // Afficher la carte résultat et masquer l'état vide
        emptyState.classList.add('hidden');
        resultCard.classList.remove('hidden');

        // Score et pourcentage
        scoreText.textContent = `${data.score} / ${data.totalCriteria}`;
        percentageText.textContent = `${data.percentage}%`;
        progressBar.style.width = `${data.percentage}%`;

        // Mise à jour de la jauge et du badge selon le niveau
        const level = data.securityLevel ? data.securityLevel.toLowerCase() : 'faible';

        securityBadge.className = 'security-badge';
        progressBar.className = 'progress-fill';

        if (level === 'fort') {
            securityBadge.textContent = 'Fort / Strong';
            securityBadge.classList.add('badge-strong');
            progressBar.classList.add('fill-strong');
        } else if (level === 'moyen') {
            securityBadge.textContent = 'Moyen / Medium';
            securityBadge.classList.add('badge-medium');
            progressBar.classList.add('fill-medium');
        } else {
            securityBadge.textContent = 'Faible / Weak';
            securityBadge.classList.add('badge-weak');
            progressBar.classList.add('fill-weak');
        }

        // Mise à jour des 5 critères visuels
        if (data.criteria) {
            setCriterionState(critLength, data.criteria.minLength);
            setCriterionState(critLower, data.criteria.lowercase);
            setCriterionState(critUpper, data.criteria.uppercase);
            setCriterionState(critDigit, data.criteria.digit);
            setCriterionState(critSpecial, data.criteria.specialChar);
        }

        // Affichage des recommandations
        renderRecommendations(data.recommendations);
    }

    function setCriterionState(element, isValid) {
        if (!element) return;
        element.setAttribute('data-valid', isValid ? 'true' : 'false');
    }

    function renderRecommendations(recommendations) {
        recommendationsList.innerHTML = '';

        if (!recommendations || recommendations.length === 0) {
            recommendationsList.innerHTML = `
                <div class="recommendations-success">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="20" height="20">
                        <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                        <polyline points="22 4 12 14.01 9 11.01"></polyline>
                    </svg>
                    <span>Félicitations ! Votre mot de passe respecte tous les critères de sécurité.</span>
                </div>
            `;
            return;
        }

        recommendations.forEach(rec => {
            const tip = document.createElement('div');
            tip.className = 'recommendation-tip';
            tip.innerHTML = `
                <span class="tip-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16">
                        <circle cx="12" cy="12" r="10"></circle>
                        <line x1="12" y1="8" x2="12" y2="12"></line>
                        <line x1="12" y1="16" x2="12.01" y2="16"></line>
                    </svg>
                </span>
                <span>${escapeHtml(rec)}</span>
            `;
            recommendationsList.appendChild(tip);
        });
    }

    function resetToEmptyState() {
        resultCard.classList.add('hidden');
        emptyState.classList.remove('hidden');
    }

    function escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
});
