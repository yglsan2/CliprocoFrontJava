<%--@elvariable id="prospect" type="models.Prospect"--%>
<%--@elvariable id="titlePage" type="String"--%>
<%--@elvariable id="violations" type="Set<ConstraintViolation<Prospect>>"--%>
<%--
  Created by IntelliJ IDEA.
  User: CDA-01
  Date: 18/03/2025
  Time: 09:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../meta.jsp"/>
    <title>Détails du prospect</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="../header.jsp"/>
<main>
    <article>
        <header><h1>Consultation</h1></header>
        <section class="container" id="content"><span class="handlewidth">Vous consultez les données actuellement disponibles pour le prospect n°1 :</span>
        </section>
        <form method="post">
            <input type="hidden" name="_csrf" value="${_csrf}"/>
            <fieldset class="row modal-dialog-centered">
                <c:if test="${titlePage != 'Création'}">
                    <input type="hidden" value="1" id="identifiantInput">
                </c:if>
                <c:if test="${['Création', 'Mise à jour'].contains(titlePage)}">
                    <ul class="banner-alert" id="bannerAlert">
                        <c:forEach var="violation" items="${violations}" varStatus="status">
                            <li>${violation.getPropertyPath().toString()} ${violation.getMessage()}</li>
                        </c:forEach>
                    </ul>
                </c:if>
                <legend class="border-bottom mb-4">Partie société</legend>
                <div class="form-group col-md-6">
                    <label for="raisonSocialeInput">Raison Sociale</label>
                    <input
                            class="form-control"
                            name="raisonSociale"
                            id="raisonSocialeInput"
                            type="text"
                            placeholder="Raison Sociale"
                            pattern="^[A-Za-zÀ-ÿ' \-]+$"
                            required
                            size="30"
                    <c:if test="${['Consultation', 'Suppression'].contains(titlePage)}">
                            disabled
                    </c:if>
                            value="${prospect.raisonSociale}"
                    >
                </div>
                <div class="form-group col-md-6">
                    <label for="telephoneInput">Téléphone</label>
                    <input
                            class="form-control"
                            name="telephone"
                            id="telephoneInput"
                            type="text"
                            placeholder="Téléphone"
                            pattern="^(?:\+33|0033|0)[1-9](?:[ .\-]?\d{2}){4}$"
                            required
                            size="12"
                    <c:if test="${['Consultation', 'Suppression'].contains(titlePage)}">
                            disabled
                    </c:if>
                            value="${prospect.telephone}"
                    >
                </div>
                <div class="form-group col-md-6">
                    <label for="adresseMailInput">Adresse Mail</label>
                    <input
                            class="form-control"
                            name="adresseMail"
                            id="adresseMailInput"
                            type="text"
                            placeholder="Adresse Mail"
                            pattern="^[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}$"
                            required
                            size="30"
                    <c:if test="${['Consultation', 'Suppression'].contains(titlePage)}">
                            disabled
                    </c:if>
                            value="${prospect.mail}"
                    >
                </div>
                <div class="form-group col-md-12">
                    <label for="commentairesTextarea">Commentaires</label>
                    <textarea
                            class="form-control"
                            name="commentaires"
                            id="commentairesTextarea"
                            placeholder="Commentaires sur le prospect"
                            rows="5"
                            <c:if test="${['Consultation', 'Suppression'].contains(titlePage)}">
                                disabled
                            </c:if>
                    >${prospect.commentaires}</textarea>
                </div>
                <legend class="border-bottom mb-4 d-flex">
                    Partie adresse -
                    <div id="labelAdresseMeteo">Météo claire et ensoleillée</div>
                    <div class="btn btn-primary" data-bs-target="#modal" data-bs-toggle="modal">Voir détails</div>
                </legend>
                <div id="map"></div>
                <div class="form-group col-md-6">
                    <label for="numeroRueInput">Numéro rue</label>
                    <input
                            class="form-control"
                            name="numeroRue"
                            id="numeroRueInput"
                            type="text"
                            placeholder="Numero Rue"
                            pattern="(?:\d{0,3} +(bis|ter|quat)|(?:^|\b))|(?:\b\d{0,3}[ab]*\b)"
                            required
                            size="15"
                    <c:if test="${['Consultation', 'Suppression'].contains(titlePage)}">
                            disabled
                    </c:if>
                            value="${prospect.adresse.numeroRue}"
                    >
                </div>
                <div class="form-group col-md-6">
                    <label for="nomRueInput">Nom rue</label>
                    <input
                            class="form-control"
                            name="nomRue"
                            id="nomRueInput"
                            type="text"
                            placeholder="Nom Rue"
                            pattern="\b([a-zA-Z0-9]+(?:[.\- ']*[a-zA-Z0-9]+)*)\b"
                            required
                            size="30"
                    <c:if test="${['Consultation', 'Suppression'].contains(titlePage)}">
                            disabled
                    </c:if>
                            value="${prospect.adresse.nomRue}"
                    >
                </div>
                <div class="form-group col-md-6">
                    <label for="codePostalInput">Code Postal</label>
                    <input
                            class="form-control"
                            name="codePostal"
                            id="codePostalInput"
                            type="text"
                            placeholder="Code Postal"
                            pattern="\b\d{5}\b"
                            required
                            size="5"
                    <c:if test="${['Consultation', 'Suppression'].contains(titlePage)}">
                            disabled
                    </c:if>
                            value="${prospect.adresse.codePostal}"
                    >
                </div>
                <div class="form-group col-md-6">
                    <label for="villeInput">Ville</label>
                    <input
                            class="form-control"
                            name="ville"
                            id="villeInput"
                            type="text"
                            pattern="\b([a-zA-Z]+(?:[.\- ']*[a-zA-Z]+)*)\b"
                            required
                            placeholder="Ville"
                            size="30"
                    <c:if test="${['Consultation', 'Suppression'].contains(titlePage)}">
                            disabled
                    </c:if>
                            value="${prospect.adresse.ville}"
                    >
                </div>
                <legend class="border-bottom mb-4">Partie prospect</legend>
                <div class="form-group col-md-6">
                    <label for="dateProspectionInput">Date Prospection</label>
                    <input
                            class="form-control"
                            name="dateProspection"
                            id="dateProspectionInput"
                            type="date"
                            placeholder="Date Prospection"
                    <c:if test="${['Consultation', 'Suppression'].contains(titlePage)}">
                            disabled
                    </c:if>
                            value="${prospect.dateProspection}"
                    >
                </div>
                <div class="form-group col-md-6">
                    <label for="prospectInteresseInput">Prospect Interesse</label>
                    <div class="checkbox-custom">
                        <input
                                class="form-control"
                                name="prospectInteresse"
                                id="prospectInteresseInput"
                                type="checkbox"
                        <c:if test="${['Consultation', 'Suppression'].contains(titlePage)}">
                                disabled
                        </c:if>
                                <c:if
                                        test="${prospect.prospectInteresse ==
                                         'oui'}">checked</c:if>
                        >
                        <label for="prospectInteresseInput"></label>
                    </div>
                </div>
                <hr class="mt-4">
                <c:if test="${titlePage != 'Consultation'}">
                    <div class="form-group col-md-12">
                        <c:if test="${titlePage == 'Suppression'}">
                            <input type="hidden" name="delete" value="true">
                        </c:if>
                        <button class="btn btn-primary float-end">
                            <c:out value="${(titlePage == 'Suppression' ? 'Supprimer' : 'Sauvegarder')}"/>
                        </button>
                    </div>
                </c:if>
            </fieldset>
        </form>
        <c:if test="${titlePage == 'Consultation'}">
            <div aria-hidden="true" class="modal fade" id="modal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="modalMeteo">Météo claire et ensoleillée</h1>
                            <button aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"></button>
                        </div>
                        <div class="modal-body">
                            <div><h5>Température</h5><div id="resultT">11°C - Frais</div></div>
                            <div><h5>Pluie</h5><div id="resultP">0mm - Aucune</div></div>
                            <div><h5>Vent</h5><div id="resultV">13km/h - Léger</div></div>
                            <div><h5>Nébulosité</h5><div id="resultN">0% - Dégagé</div></div>
                            <div><h5>Humidité</h5><div id="resultH">43% - Sec</div></div>
                        </div>
                        <div class="modal-footer">
                            <button class="btn btn-secondary" data-bs-dismiss="modal" type="button">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </article>
</main>
<jsp:include page="../footer.jsp"/>
<jsp:include page="../scripts.jsp"/>
</body>
</html>