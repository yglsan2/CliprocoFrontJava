package controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class ContactController implements ICommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("titlePage", "Contact");
        request.setAttribute("titleGroup", "Général");

        return "contact.jsp";
    }
}
