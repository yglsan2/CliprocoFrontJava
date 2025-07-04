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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="../header.jsp"/>
<main class="container mt-5">
    <div class="row">
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1><i class="fas fa-users"></i> Liste des prospects</h1>
                <a class="btn btn-crud-create" href="${pageContext.request.contextPath}/app?cmd=prospects.create">
                    <i class="fas fa-plus"></i> Ajouter un prospect
                </a>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-12">
            <div class="table-responsive">
                <table id="prospectTable" class="table table-striped table-hover custom-table">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Raison sociale</th>
                            <th>Adresse</th>
                            <th>Téléphone</th>
                            <th>Email</th>
                            <th>Date prospection</th>
                            <th>Intéressé</th>
                            <th>Commentaires</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="prospect" items="${prospects}">
                            <tr>
                                <td>${prospect.identifiant}</td>
                                <td><strong>${prospect.raisonSociale}</strong></td>
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
                                    <c:if test="${not empty prospect.commentaires}">
                                        <span class="text-truncate d-inline-block" style="max-width: 150px;" 
                                              title="${prospect.commentaires}">
                                            ${prospect.commentaires}
                                        </span>
                                    </c:if>
                                </td>
                                <td>
                                    <div class="btn-group" role="group">
                                        <a href="${pageContext.request.contextPath}/app?cmd=prospects.view&id=${prospect.identifiant}" 
                                           class="btn btn-crud-view btn-sm" title="Voir">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/app?cmd=prospects.update&id=${prospect.identifiant}" 
                                           class="btn btn-crud-edit btn-sm" title="Modifier">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/app?cmd=prospects.delete&id=${prospect.identifiant}" 
                                           class="btn btn-crud-delete btn-sm" title="Supprimer"
                                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce prospect ?')">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty prospects}">
                            <tr>
                                <td colspan="9" class="text-center text-muted py-4">
                                    <i class="fas fa-inbox fa-2x mb-2"></i><br>
                                    Aucun prospect trouvé.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>
<jsp:include page="../footer.jsp"/>
<jsp:include page="../scripts.jsp"/>
</body>
</html>