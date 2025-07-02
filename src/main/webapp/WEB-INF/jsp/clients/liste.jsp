<%--
  Created by IntelliJ IDEA.
  User: CDA-01
  Date: 18/03/2025
  Time: 09:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        <header><h1>Liste des clients</h1></header>
        <section class="container" id="content">
            <a class="btn btn-primary float-end d-flex" href="${pageContext.request.contextPath}/app?cmd=clients.create">
                <div class="material-symbols-outlined danger">Add</div>
                <div class="handlewidth">Ajout d'un</div>&nbsp;client
            </a>
        </section>
        <table id="clientTable" class="table table-striped table-hover custom-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Raison sociale</th>
                    <th>Adresse</th>
                    <th>Téléphone</th>
                    <th>Email</th>
                    <th>Chiffre d'affaires</th>
                    <th>Nb Employés</th>
                    <th>Date création</th>
                    <th>Gestionnaire</th>
                    <th>Statut</th>
                    <th>Commentaires</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="client" items="${clients}">
                    <tr>
                        <td>${client.identifiant}</td>
                        <td>${client.nom}</td>
                        <td>${client.prenom}</td>
                        <td>${client.raisonSociale}</td>
                        <td>
                            ${client.adresse.numeroRue} ${client.adresse.nomRue},<br>
                            ${client.adresse.codePostal} ${client.adresse.ville}
                        </td>
                        <td>${client.telephone}</td>
                        <td>${client.mail}</td>
                        <td>${client.chiffreAffaire}</td>
                        <td>${client.nbrEmploye}</td>
                        <td>${client.dateCreation}</td>
                        <td>${client.gestionnaireId}</td>
                        <td>${client.statut}</td>
                        <td>${client.commentaires}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/app?cmd=clients.view&clientId=${client.identifiant}" class="btn btn-info btn-sm">Voir</a>
                            <a href="${pageContext.request.contextPath}/app?cmd=clients.update&clientId=${client.identifiant}" class="btn btn-warning btn-sm">Modifier</a>
                            <a href="${pageContext.request.contextPath}/app?cmd=clients.delete&clientId=${client.identifiant}" class="btn btn-danger btn-sm">Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty clients}">
                    <tr>
                        <td colspan="14" class="text-center">Aucun client trouvé.</td>
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