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
    <title>Liste des prospects</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="../header.jsp"/>
<main>
    <article>
        <header>
            <h1>Bienvenue</h1>
        </header>
        <section class="container" id="content">
            <span class="handlewidth">sur la partie prospects</span>
            <a class="btn btn-primary float-end d-flex"
               href="?cmd=prospects/add">
                <div class="material-symbols-outlined danger">Add</div>
                <div class="handlewidth">Ajout d'un</div>&nbsp;prospect</a>
        </section>
        <div class="hovertable">
            <div class="hovertable-head">
                <div class="hovertable-row">
                    <div class="hovertable-cell smaller">#</div>
                    <div class="hovertable-cell ">Raison sociale</div>
                    <div class="hovertable-cell longer">Adresse postale</div>
                    <div class="hovertable-cell ">Téléphone</div>
                    <div class="hovertable-cell long">Adresse Mail</div>
                    <div class="hovertable-cell handlewidth">Date prospection
                    </div>
                    <div class="hovertable-cell handlewidth">Prospect
                        intéressé
                    </div>
                    <div class="hovertable-cell small">Actions</div>
                </div>
                <div class="hovertable-row">
                    <div class="hovertable-cell smaller">
                        <input type="number" placeholder="..." step="1"
                               min="1">
                    </div>
                    <div class="hovertable-cell ">
                        <input type="text" placeholder="...">
                    </div>
                    <div class="hovertable-cell longer">
                        <input type="text" placeholder="...">
                    </div>
                    <div class="hovertable-cell ">
                        <input type="text" placeholder="...">
                    </div>
                    <div class="hovertable-cell long">
                        <input type="text" placeholder="...">
                    </div>
                    <div class="hovertable-cell handlewidth">
                        <input type="text" placeholder="jj/mm/aaaa">
                    </div>
                    <div class="hovertable-cell handlewidth">
                        <select>
                            <option default=""></option>
                            <option>Oui</option>
                            <option>Non</option>
                        </select>
                    </div>
                    <div class="hovertable-cell small"></div>
                </div>
            </div>
            <div class="hovertable-body">
                <%-- If no clients --%>
                <c:if test="${empty prospects}">
                    No clients found.
                </c:if>
                <!-- Client Rows (repeated structure) -->
                <c:forEach var="prospect" items="${prospects}"
                           varStatus="status">
                    <div class="hovertable-row">
                        <div class="hovertable-cell smaller">
                            <c:out value="${prospect.identifiant}" />
                        </div>
                        <div class="hovertable-cell ">
                            <c:out value="${prospect.raisonSociale}" />
                        </div>
                        <div class="hovertable-cell longer">
                            <c:out value="${prospect.adresse.numeroRue}
                            ${prospect.adresse.nomRue}
                            ${prospect.adresse.codePostal}
                            ${prospect.adresse.ville}" />
                        </div>
                        <div class="hovertable-cell ">
                            <c:out value="${prospect.telephone}" />
                        </div>
                        <div class="hovertable-cell long">
                            <c:out value="${prospect.mail}" />
                        </div>
                        <div class="hovertable-cell handlewidth">
                            <c:out value="${prospect.dateProspection}" />
                        </div>
                        <div class="hovertable-cell handlewidth boolean
                        ${prospect.prospectInteresse == "oui"}">
                            <c:out value="${prospect.prospectInteresse}" />
                        </div>
                        <div class="hovertable-cell small">
                            <a href="<c:url value="?cmd=prospects/view">
                                            <c:param name="prospectId"
                                            value="${prospect.identifiant}"
                                            />
                                     </c:url>" title="Consulter">
                            <span class="material-symbols-outlined">
                                visibility</span>
                            </a>
                            <a href="<c:url value="?cmd=prospects/update">
                                            <c:param name="prospectId"
                                            value="${prospect.identifiant}"
                                            />
                                     </c:url>" title="Mettre à jour">
                            <span class="material-symbols-outlined warning">
                                edit
                            </span>
                            </a>
                            <a href="<c:url value="?cmd=prospects/delete">
                                            <c:param name="prospectId"
                                            value="${prospect.identifiant}"
                                            />
                                     </c:url>" title="Supprimer">
                            <span class="material-symbols-outlined danger">
                                delete
                            </span>
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