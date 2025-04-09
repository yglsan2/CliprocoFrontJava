<%--
  Created by IntelliJ IDEA.
  User: CDA-01
  Date: 18/03/2025
  Time: 09:19
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Connexion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<main>
    <article>
        <header>
            <h1>Connexion</h1>
        </header>
        <section class="container" id="content">
            <span class="handlewidth">Page de connexion sur Reverso.</span>
        </section>
        <form method="post">
            <fieldset class="row modal-dialog-centered">
                <ul class="banner-alert" id="bannerAlert">
                    <c:forEach var="violation" items="${violations}"
                               varStatus="status" >
                        <li>${violation.getPropertyPath().toString()} ${violation.getMessage()}</li>
                    </c:forEach>
                </ul>
                <div class="form-group col-md-6">
                    <label for="usernameInput">Username</label>
                    <input id="usernameInput"
                           class="form-control"
                           type="text"
                           name="username"
                           pattern="^[A-Za-z0-9._%+\-]+@[A-Za-z0-9]+\.[A-Za-z0-9.\-]{2,}"
                           placeholder="Username"
                           required
                           size="30">
                </div>
                <div class="form-group col-md-6">
                    <label for="passwordInput">Mot de passe</label>
                    <input id="passwordInput"
                           name="password"
                           class="form-control"
                           type="password"
                           placeholder="Mot de passe"
                           required
                           size="30">
                </div>
                <hr>
                <div class="form-group col-md-12">
                    <label for="rememberMe">Remember me</label>
                    <div class="checkbox-custom">
                        <input
                                class="form-control"
                                name="rememberMe"
                                id="rememberMe"
                                type="checkbox"
                                />
                        <label for="rememberMe"></label>
                    </div>
                </div>
                <div class="form-group col-md-12">
                    <button class="btn btn-primary float-end">Envoyer</button>
                </div>
            </fieldset>
        </form>
    </article>
</main>
<jsp:include page="footer.jsp"/>
<jsp:include page="scripts.jsp"/>
</body>
</html>