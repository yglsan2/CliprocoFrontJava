<%@ page import="com.cliproco.util.CsrfUtil" %>
<form action="login" method="post">
    <%= CsrfUtil.generateTokenInput() %>
</form> 