package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class IndexController implements ICommand {

    @Contract(pure = true)
    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {

        request.setAttribute("titlePage", "Accueil");
        request.setAttribute("titleGroup", "Général");
        return "index.jsp";
    }
}
