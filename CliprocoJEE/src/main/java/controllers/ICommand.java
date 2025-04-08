package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ICommand {
    String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
