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
    <title>Contact</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<main>
    <article>
        <header>
            <h1>Contact</h1>
        </header>

        <section class="container" id="content">
                <span class="handlewidth">
                    Vous souhaitez nous contacter pour un besoin bien précis ?
                    Veuillez utiliser le formulaire suivant.
                </span>
        </section>

        <form action="#" method="post">
            <fieldset class="row modal-dialog-centered">
                <div class="form-group col-md-6">
                    <label for="prenomInput">Prénom</label>
                    <input
                            class="form-control"
                            id="prenomInput"
                            type="text"
                            pattern="^[A-Za-zÀ-ÿ' \-]+$"
                            placeholder="Prénom"
                            required
                            size="30">
                </div>

                <div class="form-group col-md-6">
                    <label for="nomInput">Nom</label>
                    <input
                            class="form-control"
                            id="nomInput"
                            type="text"
                            pattern="^[A-Za-zÀ-ÿ' \-]+$"
                            placeholder="Nom"
                            required
                            size="30">
                </div>

                <div class="form-group col-md-6">
                    <label for="adresseMailInput">Adresse Mail</label>
                    <input
                            class="form-control"
                            id="adresseMailInput"
                            type="text"
                            pattern="^[A-Za-z0-9._%+\-]+@[A-Za-z0-9]+.[A-Za-z0-9.\-]{2,}"
                            placeholder="Adresse mail"
                            required
                            size="30">
                </div>

                <div class="form-group col-md-12">
                    <label for="commentairesTextarea">
                        Commentaires / Message
                    </label>
                    <textarea
                            class="form-control"
                            id="commentairesTextarea"
                            placeholder="Le contenu de votre message"
                            required
                            rows="10">
                        </textarea>
                </div>

                <hr>

                <div class="form-group col-md-12">
                    <button class="btn btn-primary float-end">
                        Envoyer
                    </button>
                </div>
            </fieldset>
        </form>
    </article>
</main>

<jsp:include page="footer.jsp"/>
<jsp:include page="scripts.jsp"/>
</body>
</html>