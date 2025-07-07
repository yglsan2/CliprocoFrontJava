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
    <title>Liste des prospects</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="../header.jsp"/>
<main>
    <article>
        <header><h1>Liste des prospects</h1></header>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>
        
        <section class="container" id="content">
            <a class="btn btn-add float-end" href="${pageContext.request.contextPath}/app?cmd=prospects.create">
                <i class="fas fa-plus"></i> Ajouter un prospect
            </a>
        </section>
        <table id="prospectTable" class="table table-striped table-hover custom-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Raison sociale</th>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Adresse</th>
                            <th>Téléphone</th>
                            <th>Email</th>
                            <th>Date prospection</th>
                            <th>Intéressé</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="prospect" items="${prospects}">
                            <tr>
                                <td>${prospect.identifiant}</td>
                                <td>${prospect.raisonSociale}</td>
                                <td class="lorraine-name">${prospect.nom}</td>
                                <td class="lorraine-firstname">${prospect.prenom}</td>
                                <td>
                                    ${prospect.adresse.numeroRue} ${prospect.adresse.nomRue},<br>
                                    ${prospect.adresse.codePostal} ${prospect.adresse.ville}
                                </td>
                                <td>${prospect.telephone}</td>
                                <td>${prospect.mail}</td>
                                <td>${prospect.dateProspection}</td>
                                <td>
                                    <span class="badge ${prospect.prospectInteresse == true ? 'bg-success' : 'bg-danger'}">
                                        ${prospect.prospectInteresse == true ? 'Oui' : 'Non'}
                                    </span>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/app?cmd=prospects.view&id=${prospect.identifiant}" class="btn btn-crud-view btn-sm">
                                        <i class="fas fa-eye"></i> Voir
                                    </a>
                                    <a href="${pageContext.request.contextPath}/app?cmd=prospects.update&id=${prospect.identifiant}" class="btn btn-crud-edit btn-sm">
                                        <i class="fas fa-edit"></i> Modifier
                                    </a>
                                    <form method="post" action="${pageContext.request.contextPath}/app?cmd=prospects.delete" style="display: inline;">
                                        <input type="hidden" name="id" value="${prospect.identifiant}">
                                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                        <button type="submit" class="btn btn-crud-delete btn-sm" 
                                                onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce prospect ?')">
                                            <i class="fas fa-trash"></i> Supprimer
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty prospects}">
                            <tr>
                                <td colspan="10" class="text-center">Aucun prospect trouvé.</td>
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