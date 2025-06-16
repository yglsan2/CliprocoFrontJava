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
    <jsp:include page="meta.jsp"/>
    <title>Déconnexion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="header.jsp"/>

<main>
    <article>
        <header>
            <h1>Déconnexion</h1>
        </header>

        <section class="container" id="content">
                <span class="handlewidth">
                    Souhaitez-vous vous déconnecter de l'application ?
                </span>
        </section>

        <form method="post">
            <fieldset class="row modal-dialog-centered">
                <div class="form-group col-md-6 d-flex justify-content-center">
                    <input type="submit"
                           class="btn btn-success float-end"
                           name="answer"
                           value="Oui">
                </div>
                <div class="form-group col-md-6 d-flex justify-content-center">
                    <input type="submit"
                           class="btn btn-danger float-end"
                           name="answer"
                           value="Non">
                </div>
            </fieldset>
        </form>
    </article>
</main>

<jsp:include page="footer.jsp"/>
<jsp:include page="scripts.jsp"/>
</body>
</html>