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
    <title>Liste des clients</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="../header.jsp"/>
<main>
    <article>
        <header><h1>Bienvenue</h1></header>
        <section class="container" id="content">
            <span class="handlewidth">sur la partie clients</span>
            <a class="btn btn-primary float-end d-flex"
               href="?cmd=clients/add">
                <div class="material-symbols-outlined danger">Add</div>
                <div class="handlewidth">Ajout d'un</div>&nbsp;client
            </a>
        </section>
        <div class="hovertable">
            <div class="hovertable-head">
                <div class="hovertable-row">
                    <div class="hovertable-cell smaller">#</div>
                    <div class="hovertable-cell">Raison sociale</div>
                    <div class="hovertable-cell longer">Adresse postale</div>
                    <div class="hovertable-cell">Téléphone</div>
                    <div class="hovertable-cell long">Adresse Mail</div>
                    <div class="hovertable-cell handlewidth">
                        Chiffre d'affaires
                    </div>
                    <div class="hovertable-cell handlewidth">Nb Employés</div>
                    <div class="hovertable-cell small">Actions</div>
                </div>
                <div class="hovertable-row">
                    <div class="hovertable-cell smaller">
                        <input type="number"
                               placeholder="..."
                               step="1"
                               min="1">
                    </div>
                    <div class="hovertable-cell">
                        <input type="text" placeholder="...">
                    </div>
                    <div class="hovertable-cell longer">
                        <input type="text" placeholder="...">
                    </div>
                    <div class="hovertable-cell">
                        <input type="text" placeholder="...">
                    </div>
                    <div class="hovertable-cell long">
                        <input type="text" placeholder="...">
                    </div>
                    <div class="hovertable-cell handlewidth">
                        <input type="number"
                               placeholder="..."
                               step="0.01"
                               min="250">
                    </div>
                    <div class="hovertable-cell handlewidth">
                        <input type="number"
                               placeholder="..."
                               step="1"
                               min="1">
                    </div>
                    <div class="hovertable-cell small"></div>
                </div>
            </div>
            <div class="hovertable-body">
                <%-- If no clients --%>
                <c:if test="${empty clients}">
                    No clients found.
                </c:if>
                <!-- Client Rows (repeated structure) -->
                <c:forEach var="client" items="${clients}" varStatus="status">
                    <div class="hovertable-row">
                        <div class="hovertable-cell smaller">
                            <c:out value="${client.identifiant}" />
                        </div>
                        <div class="hovertable-cell">
                            <c:out value="${client.raisonSociale}" />
                        </div>
                        <div class="hovertable-cell longer">
                            <c:out value="${client.adresse.numeroRue}
                            ${client.adresse.nomRue}
                            ${client.adresse.codePostal}
                            ${client.adresse.ville}" />
                        </div>
                        <div class="hovertable-cell">
                            <c:out value="${client.telephone}" />
                        </div>
                        <div class="hovertable-cell long">
                            <c:out value="${client.mail}" />
                        </div>
                        <div class="hovertable-cell handlewidth">
                            <c:out value="${client.chiffreAffaires}" />
                        </div>
                        <div class="hovertable-cell handlewidth">
                            <c:out value="${client.nbEmployes}" />
                        </div>
                        <div class="hovertable-cell small">
                            <a href="<c:url value="?cmd=clients/view">
                                            <c:param name="clientId"
                                            value="${client.identifiant}"
                                            />
                                     </c:url>" title="Consulter">
                                <span class="material-symbols-outlined">visibility</span>
                            </a>
                            <a href="<c:url value="?cmd=clients/update">
                                            <c:param name="clientId"
                                            value="${client.identifiant}"
                                            />
                                     </c:url>" title="Mettre à jour">
                                <span class="material-symbols-outlined warning">edit</span>
                            </a>
                            <a href="<c:url value="?cmd=clients/delete">
                                            <c:param name="clientId"
                                            value="${client.identifiant}"
                                            />
                                     </c:url>" title="Supprimer">
                                <span class="material-symbols-outlined danger">delete</span>
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </article>
</main>
<jsp:include page="../footer.jsp"/>
<jsp:include page="../scripts.jsp"/>
</body>
</html>