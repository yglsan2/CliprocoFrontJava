package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class IndexController implements ICommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("titlePage", "Accueil");
        request.setAttribute("titleGroup", "Général");
        return "/WEB-INF/jsp/index.jsp";
    }
}
