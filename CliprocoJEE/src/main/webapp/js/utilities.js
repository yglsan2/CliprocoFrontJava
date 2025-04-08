/**
 * @function getCurrentPage
 * @description Extracts the current page from the URL pathname
 * @returns {string} String representing the current page path
 */
function getCurrentPage() {
    const cmd = new URLSearchParams(window.location.search).get("cmd");
    const pathArray = cmd !== null ? cmd.split("/") : "index";
    let page;

    if (pathArray.length === 1) {
        page = pathArray[0];
    } else if (pathArray.length === 2) {
        page = pathArray[0] + "/" + pathArray[1];
    }
    return page;
}

/**
 * @function securiteChiffreAffaires
 * @description Validates if business revenue input is below 200
 * @returns {boolean} True if revenue is less than 200, false otherwise
 * @requires Form element with ID "chiffreAffairesInput"
 */
function securiteChiffreAffaires() {
    return document.querySelector("form input#chiffreAffairesInput").value < 200;
}

/**
 * @function securiteNbEmployes
 * @description Validates if employee count input is less than 1
 * @returns {boolean} True if employee count is less than 1, false otherwise
 * @requires Form element with ID "nbEmployesInput"
 */
function securiteNbEmployes() {
    return document.querySelector("form input#nbEmployesInput").value < 1;
}

/**
 * @function detectHovertable
 * @description Checks if a hovertable element exists on the page
 * @returns {boolean} True if an element with class "hovertable" exists, false otherwise
 */
function detectHovertable() {
    return document.querySelector(".hovertable") !== null;
}

/**
 * @function searchValueHovertable
 * @description Filters table rows based on search value in a specific column
 * @param {string} value - String to search for
 * @param {number} column - Index of the column to search in
 * @requires Hovertable element with class "hovertable"
 */
function searchValueHovertable(value, column) {
    Array.from(document.querySelectorAll(".hovertable .hovertable-body .hovertable-row")).forEach((row) => {
        let searchFalse;

        if (row.getAttribute("searchFalse") !== null) {
            searchFalse = JSON.parse(row.getAttribute("searchFalse"));
        } else {
            searchFalse = [];
        }

        if (!row.children[column].innerText.toLowerCase().includes(value.toLowerCase()) && value !== "") {
            !searchFalse.includes(column) ? searchFalse.push(column) : null;
        } else {
            let index = searchFalse.indexOf(column);
            if (index > -1) {
                searchFalse.splice(index, 1);
            }
        }

        row.setAttribute("searchFalse", JSON.stringify(searchFalse));
    });
    hideRowIfMarked();
}

/**
 * @function hideRowIfMarked
 * @description Hides table rows marked with searchFalse attribute
 * @requires Rows with searchFalse attributes
 */
function hideRowIfMarked() {
    Array.from(document.querySelectorAll(".hovertable .hovertable-body .hovertable-row")).forEach((row) => {
        let searchFalse;

        if (row.getAttribute("searchFalse") !== null) {
            searchFalse = JSON.parse(row.getAttribute("searchFalse"));
        }

        if (searchFalse.length > 0) {
            row.setAttribute("hidden", "hidden");
        } else {
            row.removeAttribute("hidden");
        }
    });
}

/**
 * @function searchGeolocalisation
 * @description Gets coordinates from address using French government API
 * @returns {Promise<Array>} Promise resolving to an array of [latitude, longitude]
 * @requires Form inputs with IDs "numeroRueInput", "nomRueInput", "codePostalInput", "villeInput"
 * @uses API at "https://api-adresse.data.gouv.fr/search/"
 */
async function searchGeolocalisation() {
    const adresse = document.getElementById("numeroRueInput").value + " " +
        document.getElementById("nomRueInput").value + " " +
        document.getElementById("codePostalInput").value + " " +
        document.getElementById("villeInput").value;

    const response = await fetch("https://api-adresse.data.gouv.fr/search/?q=" + adresse.replaceAll(" ", "+"));

    if (!response.ok) {
        throw new Error(`Response status: ${response.status}`);
    }

    const json = await response.json();

    return [json.features[0].geometry.coordinates[1], json.features[0].geometry.coordinates[0]];
}

/**
 * @function createLeafletMap
 * @description Creates and displays a Leaflet map centered on provided coordinates
 * @param {Array} array - Array containing [latitude, longitude]
 * @requires Leaflet.js library
 * @requires Form inputs with IDs "numeroRueInput", "nomRueInput", "codePostalInput", "villeInput", "raisonSocialeInput"
 * @requires Element with ID "map" to render the map
 */
function createLeafletMap(array) {
    const adresse = document.getElementById("numeroRueInput").value + " " +
        document.getElementById("nomRueInput").value + " " +
        document.getElementById("codePostalInput").value + " " +
        document.getElementById("villeInput").value;

    map = L.map('map').setView(array, 16);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 23,
        attribution: '&copy; <a href="https://osm.org/copyright%22%3EOpenStreetMap</a> contributors',
        id: 'mapbox.streets'
    }).addTo(map);

    var marker = L.marker(array).addTo(map);
    marker.bindPopup(document.getElementById("raisonSocialeInput").value + "<br/>" + adresse).openPopup();
}

/**
 * @function createMeteoModale
 * @description Fetches weather data for specified coordinates
 * @param {Array} array - Array containing [latitude, longitude]
 * @returns {Promise<Object>} Promise resolving to weather data for current time period
 * @uses InfoClimat API
 */
async function createMeteoModale(array) {
    const url = "https://www.infoclimat.fr/public-api/gfs/json?_ll=" + array[0] + "," + array[1] +
        "&_auth=Bx0DFAV7U3FRfFZhUCYFLAdvAjcLfQUiAX1QM1w5XyJWPVAxBGRQNgRqWicAL1dhUH0EZww3CTkHbAJ6WigDYgdtA28FblM0UT5WM1B%2FBS4HPQJnCzwFNAFrUChcLl89Vj1QPAR5UDAEaFo5AC5XZVBmBHoMMgkxB2wCelooA2EHYwNjBWdTMVE3VjNQZAUxBzICfQsrBTsBZVBkXDZfPFY8UGYEMlBhBGlaPAA4VzBQZAR6DDEJMQdgAmNaMANjB2ADYwV5Uy5RR1ZHUH0FcQd2AjcLcgUgATdQaVxl&_c=83c427140f87bd975fcfb36035f83e4b";
    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(`Response status: ${response.status}`);
    }

    const json = await response.json();

    try {
        const date = new Date();
        let dateString = date.toISOString().split("T")[0];

        if (date.getHours() <= 1) {
            dateString += " 01:00:00";
        } else if (date.getHours() > 1 && date.getHours() <= 4) {
            dateString += " 04:00:00";
        } else if (date.getHours() > 4 && date.getHours() <= 7) {
            dateString += " 07:00:00";
        } else if (date.getHours() > 7 && date.getHours() <= 10) {
            dateString += " 10:00:00";
        } else if (date.getHours() > 10 && date.getHours() <= 13) {
            dateString += " 13:00:00";
        } else if (date.getHours() > 13 && date.getHours() <= 16) {
            dateString += " 16:00:00";
        } else if (date.getHours() > 16 && date.getHours() <= 19) {
            dateString += " 19:00:00";
        } else if (date.getHours() > 19 && date.getHours() <= 22) {
            dateString += " 22:00:00";
        } else if (date.getHours() > 22) {
            date.setDate(date.getDate() + 1);
            dateString = date.toISOString().split("T")[0] + "01:00:00";
        }

        return json[dateString];
    } catch (error) {
        console.error(error);
        return "";
    }
}

/**
 * @function getTemperature
 * @description Extracts and categorizes temperature data
 * @param {Object} json - Weather data from API
 * @returns {Array} [numerical value, descriptive category]
 * @categories "Très froid", "Froid", "Frais", "Doux", "Chaud/Canicule"
 */
function getTemperature(json) {
    const valeur = Math.floor(json.temperature["sol"] - 273.15);

    const libelle = (valeur < 0 && "Très froid") ||
        (valeur >= 0 && valeur < 10 && "Froid") ||
        (valeur >= 10 && valeur < 20 && "Frais") ||
        (valeur >= 20 && valeur < 30 && "Doux") ||
        (valeur >= 30 && "Chaud/Canicule");

    return [valeur, libelle];
}

/**
 * @function getPluie
 * @description Extracts and categorizes precipitation data
 * @param {Object} json - Weather data from API
 * @returns {Array} [numerical value, descriptive category]
 * @categories "Aucune", "Très faible", "Faible", "Modérée", "Forte", "Très forte"
 */
function getPluie(json) {
    const valeur = Math.floor(json.pluie);

    const libelle = (valeur === 0 && "Aucune") ||
        (valeur > 0 && valeur < 2 && "Très faible") ||
        (valeur >= 2 && valeur < 10 && "Faible") ||
        (valeur >= 10 && valeur < 30 && "Modérée") ||
        (valeur >= 30 && valeur < 50 && "Forte") ||
        (valeur >= 50 && "Très forte");

    return [valeur, libelle];
}

/**
 * @function getVent
 * @description Extracts and categorizes wind data
 * @param {Object} json - Weather data from API
 * @returns {Array} [numerical value, descriptive category]
 * @categories "Calme", "Léger", "Modéré", "Fort", "Tempête/Ouragan"
 */
function getVent(json) {
    const valeur = Math.floor(json.vent_moyen["10m"]);

    const libelle = (valeur >= 0 && valeur < 10 && "Calme") ||
        (valeur >= 10 && valeur < 20 && "Léger") ||
        (valeur >= 20 && valeur < 40 && "Modéré") ||
        (valeur >= 40 && valeur < 60 && "Fort") ||
        (valeur >= 60 && "Tempête/Ouragan");

    return [valeur, libelle];
}

/**
 * @function getNebulosite
 * @description Extracts and categorizes cloud cover data
 * @param {Object} json - Weather data from API
 * @returns {Array} [numerical value, descriptive category]
 * @categories "Dégagé", "Partiellement nuageux", "Nuageux", "Très nuageux", "Brouillard"
 */
function getNebulosite(json) {
    const valeur = Math.floor(json.nebulosite["totale"]);

    const libelle = (valeur >= 0 && valeur < 20 && "Dégagé") ||
        (valeur >= 20 && valeur < 50 && "Partiellement nuageux") ||
        (valeur >= 50 && valeur < 80 && "Nuageux") ||
        (valeur >= 80 && valeur < 100 && "Très nuageux") ||
        (valeur >= 100 && "Brouillard");

    return [valeur, libelle];
}

/**
 * @function getHumidite
 * @description Extracts and categorizes humidity data
 * @param {Object} json - Weather data from API
 * @returns {Array} [numerical value, descriptive category]
 * @categories "Très sec", "Sec", "Modéré", "Humide", "Très humide"
 */
function getHumidite(json) {
    const valeur = Math.floor(json.humidite["2m"]);

    const libelle = (valeur < 30 && "Très sec") ||
        (valeur >= 30 && valeur < 50 && "Sec") ||
        (valeur >= 50 && valeur < 70 && "Modéré") ||
        (valeur >= 70 && valeur < 90 && "Humide") ||
        (valeur >= 90 && "Très humide");

    return [valeur, libelle];
}

/**
 * @function getTypeMeteo
 * @description Determines overall weather type based on multiple parameters
 * @param {number} valueTemperature - Temperature value
 * @param {number} valuePluie - Precipitation value
 * @param {number} valueVent - Wind value
 * @param {number} valueNebulosite - Cloud cover value
 * @param {number} valueHumidite - Humidity value
 * @returns {string} Description of overall weather condition
 */
function getTypeMeteo(valueTemperature, valuePluie, valueVent, valueNebulosite, valueHumidite) {
    if (valueTemperature >= 10 && valueTemperature < 30 &&
        valuePluie >= 0 && valuePluie < 2 &&
        valueVent >= 0 && valueVent < 20 &&
        valueNebulosite >= 0 && valueNebulosite < 20 &&
        valueHumidite >= 30 && valueHumidite < 70) {
        return "Météo claire et ensoleillée"
    } else if (valueTemperature >= 10 && valueTemperature < 30 &&
        valuePluie >= 2 && valuePluie < 30 &&
        valueVent >= 10 && valueVent < 40 &&
        valueNebulosite >= 50 && valueNebulosite <= 100 &&
        valueHumidite >= 50 && valueHumidite < 90) {
        return "Météo nuageuse/grise"
    } else if (valueTemperature >= 5 && valueTemperature < 25 &&
        valuePluie >= 2 && valuePluie < 10 &&
        valueVent >= 10 && valueVent < 20 &&
        valueNebulosite >= 20 && valueNebulosite < 80 &&
        valueHumidite >= 70 && valueHumidite < 90) {
        return "Pluie légère (Bruine/Averse)"
    } else if (valueTemperature >= 15 && valueTemperature < 30 &&
        valuePluie > 30 &&
        valueVent > 40 &&
        valueNebulosite >= 80 && valueNebulosite <= 100 &&
        valueHumidite > 90) {
        return "Orage/Pluie intense"
    } else if (valueTemperature > 30 &&
        valuePluie >= 0 && valuePluie < 2 &&
        valueVent >= 0 && valueVent < 20 &&
        valueNebulosite >= 0 && valueNebulosite < 50 &&
        valueHumidite >= 30 && valueHumidite < 90) {
        return "Canicule"
    } else if (valuePluie >= 0 && valuePluie < 30 &&
        valueVent > 40 &&
        valueNebulosite >= 50 && valueNebulosite <= 100 &&
        valueHumidite >= 50 && valueHumidite < 90) {
        return "Tempête de vent (Vent fort)"
    } else if (valueTemperature >= 0 && valueTemperature < 15 &&
        valuePluie >= 0 && valuePluie < 2 &&
        valueVent >= 0 && valueVent < 10 &&
        valueNebulosite === 100 &&
        valueHumidite > 90) {
        return "Brouillard"
    } else if (valueTemperature < 0 &&
        valuePluie >= 10 && valuePluie <= 100 &&
        valueVent >= 10 && valueVent < 40 &&
        valueNebulosite >= 50 && valueNebulosite <= 100 &&
        valueHumidite >= 50 && valueHumidite < 90) {
        return "Neige"
    } else if (valueTemperature >= 10 && valueTemperature < 35 &&
        valuePluie >= 0 && valuePluie < 2 &&
        valueVent >= 10 && valueVent < 40 &&
        valueNebulosite >= 0 && valueNebulosite < 20 &&
        valueHumidite < 30) {
        return "Temps sec et aride"
    } else if (valueTemperature > 25 &&
        valuePluie >= 10 &&
        valueVent >= 10 && valueVent < 60 &&
        valueNebulosite >= 20 && valueNebulosite <= 100 &&
        valueHumidite > 80) {
        return "Temps tropical (chaud et humide)"
    } else {
        return "Météo changeante"
    }
}