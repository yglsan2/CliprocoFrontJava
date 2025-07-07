<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Essential meta tags -->
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Theme and site identification -->
<meta name="theme-color" content="#000000">
<meta name="description" content="Site de gestion de clients">

<!-- Stylesheets -->
<!-- Bootstrap core CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<!-- Leaflet CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/leaflet.css">

<!-- Main application styles -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">

<title>${titlePage} | Gestion ${titleGroup}</title>