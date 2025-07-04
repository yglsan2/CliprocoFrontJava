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
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>
        
        <section class="container" id="content">
            <a class="btn btn-add float-end" href="${pageContext.request.contextPath}/app?cmd=clients.create">
                <i class="fas fa-plus"></i> Ajouter un client
            </a>
        </section>
        <table id="clientTable" class="table table-striped table-hover custom-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Raison sociale</th>
                    <th>Adresse</th>
                    <th>Téléphone</th>
                    <th>Email</th>
                    <th>Chiffre d'affaires</th>
                    <th>Nb Employés</th>
                    <th>Commentaires</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="client" items="${clients}">
                    <tr>
                        <td>${client.identifiant}</td>
                        <td>${client.raisonSociale}</td>
                        <td>
                            ${client.adresse.numeroRue} ${client.adresse.nomRue},<br>
                            ${client.adresse.codePostal} ${client.adresse.ville}
                        </td>
                        <td>${client.telephone}</td>
                        <td>${client.mail}</td>
                        <td>${client.chiffreAffaires}</td>
                        <td>${client.nbEmployes}</td>
                        <td>${client.commentaires}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/app?cmd=clients.view&id=${client.identifiant}" class="btn btn-crud-view btn-sm">
                                <i class="fas fa-eye"></i> Voir
                            </a>
                            <a href="${pageContext.request.contextPath}/app?cmd=clients.update&id=${client.identifiant}" class="btn btn-crud-edit btn-sm">
                                <i class="fas fa-edit"></i> Modifier
                            </a>
                            <a href="${pageContext.request.contextPath}/app?cmd=clients.delete&id=${client.identifiant}" class="btn btn-crud-delete btn-sm" 
                               onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce client ?')">
                                <i class="fas fa-trash"></i> Supprimer
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty clients}">
                    <tr>
                        <td colspan="9" class="text-center">Aucun client trouvé.</td>
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