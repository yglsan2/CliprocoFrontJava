// html.mjs
// Fonctions utilitaires DOM pour usage navigateur uniquement

/**
 * Sélectionne un élément par son ID
 * @param {string} id
 * @returns {HTMLElement|null}
 */
export function byId(id) {
    return document.getElementById(id);
}

/**
 * Sélectionne le premier élément correspondant au sélecteur
 * @param {string} selector
 * @param {Element|Document} [scope=document]
 * @returns {Element|null}
 */
export function qs(selector, scope = document) {
    return scope.querySelector(selector);
}

/**
 * Sélectionne tous les éléments correspondant au sélecteur
 * @param {string} selector
 * @param {Element|Document} [scope=document]
 * @returns {NodeListOf<Element>}
 */
export function qsa(selector, scope = document) {
    return scope.querySelectorAll(selector);
}

/**
 * Crée un nouvel élément HTML
 * @param {string} tag
 * @param {Object} [props]
 * @returns {HTMLElement}
 */
export function createEl(tag, props = {}) {
    const el = document.createElement(tag);
    Object.entries(props).forEach(([k, v]) => {
        if (k in el) {
            el[k] = v;
        } else {
            el.setAttribute(k, v);
        }
    });
    return el;
}

/**
 * Ajoute une ou plusieurs classes à un élément
 * @param {Element} el
 * @param {...string} classes
 */
export function addClass(el, ...classes) {
    if (el) el.classList.add(...classes);
}

/**
 * Retire une ou plusieurs classes d'un élément
 * @param {Element} el
 * @param {...string} classes
 */
export function removeClass(el, ...classes) {
    if (el) el.classList.remove(...classes);
}

/**
 * Affiche un élément
 * @param {Element} el
 */
export function show(el) {
    if (el) el.style.display = '';
}

/**
 * Masque un élément
 * @param {Element} el
 */
export function hide(el) {
    if (el) el.style.display = 'none';
}

/**
 * Ajoute un écouteur d'événement à un élément
 * @param {Element} el
 * @param {string} event
 * @param {Function} handler
 */
export function on(el, event, handler) {
    if (el) el.addEventListener(event, handler);
}

/**
 * Retire un écouteur d'événement à un élément
 * @param {Element} el
 * @param {string} event
 * @param {Function} handler
 */
export function off(el, event, handler) {
    if (el) el.removeEventListener(event, handler);
}

/**
 * Récupère ou définit la valeur d'un champ de formulaire
 * @param {Element} el
 * @param {string} [value]
 * @returns {string|undefined}
 */
export function val(el, value) {
    if (!el) return undefined;
    if (value === undefined) return el.value;
    el.value = value;
}

/**
 * Vide le contenu d'un élément
 * @param {Element} el
 */
export function empty(el) {
    while (el.firstChild) el.removeChild(el.firstChild);
}

/**
 * Bascule une classe sur un élément
 * @param {Element} el
 * @param {string} className
 * @param {boolean} [force]
 */
export function toggleClass(el, className, force) {
    el.classList.toggle(className, force);
}

/**
 * Vérifie si un élément est visible
 * @param {Element} el
 * @returns {boolean}
 */
export function isVisible(el) {
    return !!(el.offsetWidth || el.offsetHeight || el.getClientRects().length);
} 