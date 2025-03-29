package com.cliproco.form;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UserForm {
    private String user;
    private String pwd;
    private boolean remember;
    private String error;

    public UserForm() {
    }

    public UserForm(HttpServletRequest request) {
        this.user = request.getParameter("user");
        this.pwd = request.getParameter("pwd");
        this.remember = request.getParameter("remember") != null;
    }

    public boolean validate() {
        if (user == null || user.trim().isEmpty()) {
            error = "Le nom d'utilisateur est requis";
            return false;
        }
        if (pwd == null || pwd.trim().isEmpty()) {
            error = "Le mot de passe est requis";
            return false;
        }
        return true;
    }

    public void setSession(HttpSession session) {
        session.setAttribute("user", user);
        session.setAttribute("username", user);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public String getError() {
        return error;
    }
} 