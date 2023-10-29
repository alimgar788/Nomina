<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 23/10/2023
  Time: 18:12
  To change this template use File | Settings | File Templates.
--%>
<%@include file="../header/header.jsp" %>
<h2>Consulta el Salario</h2>
<div class="formulario">
    <form id="formulario-consulta-dni" action="consulta" method="get">
        <div class="form_group">
            <input class="form_field" type="text" id="dni" name="dni" placeholder="Ej: 12345678A" required>
            <label class="form_label">Introduce el dni que deseas buscar:</label>
        </div>
        <div>
            <input type="submit" value="Buscar">
        </div>
    </form>
</div>
<%@include file="../footer/footer.jsp" %>
