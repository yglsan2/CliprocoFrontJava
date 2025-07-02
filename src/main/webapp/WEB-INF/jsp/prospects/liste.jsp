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
        <header><h1>Liste des prospects</h1></header>
        <section class="container" id="content">
            <a class="btn btn-primary float-end d-flex" href="?cmd=prospects/add">
                <div class="material-symbols-outlined danger">Add</div>
                <div class="handlewidth">Ajout d'un</div>&nbsp;prospect
            </a>
        </section>
        <table id="prospectTable" class="table table-striped table-hover custom-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Raison sociale</th>
                    <th>Adresse</th>
                    <th>Téléphone</th>
                    <th>Email</th>
                    <th>Date prospection</th>
                    <th>Intéressé</th>
                    <th>Gestionnaire</th>
                    <th>Statut</th>
                    <th>Commentaires</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="prospect" items="${prospects}">
                    <tr>
                        <td>${prospect.identifiant}</td>
                        <td>${prospect.nom}</td>
                        <td>${prospect.prenom}</td>
                        <td>${prospect.raisonSociale}</td>
                        <td>
                            ${prospect.adresse.numeroRue} ${prospect.adresse.nomRue},<br>
                            ${prospect.adresse.codePostal} ${prospect.adresse.ville}
                        </td>
                        <td>${prospect.telephone}</td>
                        <td>${prospect.mail}</td>
                        <td>${prospect.dateProspection}</td>
                        <td>${prospect.prospectInteresse}</td>
                        <td>${prospect.gestionnaireId}</td>
                        <td>${prospect.statut}</td>
                        <td>${prospect.commentaires}</td>
                        <td>
                            <a href="?cmd=prospects/view&prospectId=${prospect.identifiant}" class="btn btn-info btn-sm">Afficher</a>
                            <a href="?cmd=prospects/update&prospectId=${prospect.identifiant}" class="btn btn-warning btn-sm">Modifier</a>
                            <a href="?cmd=prospects/delete&prospectId=${prospect.identifiant}" class="btn btn-danger btn-sm">Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty prospects}">
                    <tr>
                        <td colspan="13" class="text-center">Aucun prospect trouvé.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </article>
</main>
<jsp:include page="../footer.jsp"/>
<jsp:include page="../scripts.jsp"/>
</body>
</html>