<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 23/10/2023
  Time: 11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<link>
<meta charset="UTF-8">
<meta name="viewport"
      content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Web de gestion de nominas</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Outfit:wght@100;200;300&family=Roboto:wght@100;300;400;500;700&family=Special+Elite&display=swap" rel="stylesheet">
<link rel="stylesheet" href="/Nomina_war_exploded/views/css/style.css">
<script src="/Nomina_war_exploded/views/js/header.js"></script>
<script src="/Nomina_war_exploded/views/js/validacionFormulario.js"></script>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>WEB DE GESTI&OacuteN DE N&OacuteMINAS</h1>
    </div>
    <div class="content-wrapper">
        <div class="menu">
            <a class="boton-volver" onclick="volver(event)">Volver</a>
            <a href="registro" ${paginaActual == "registro" ? 'class="selected"' : ''}>Registro de empleado</a>
            <a href="listado" ${paginaActual == "listado" ? 'class="selected"' : ''}>Lista de empleados</a>
            <a href="consulta" ${paginaActual == "consulta" ? 'class="selected"' : ''}>Consultar salario</a>
            <a href="actualiza" ${paginaActual == "actualiza" ? 'class="selected"' : ''}>Actualizar datos</a>
        </div>
        <div class="content">