<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 23/10/2023
  Time: 18:12
  To change this template use File | Settings | File Templates.
--%>
<%@include file="../header/header.jsp" %>
<div class="buscador">
    <h2>Consulta el Salario</h2>
    <form id="formulario-consulta-dni" action="consulta" method="get">
        <div id="formulario">
            <div>
                <label>Introduce el dni que deseas buscar:</label>
                <input type="text" id="dni" name="dni" placeholder="Ej: 12345678A" required>
            </div>
            <div>
                <input type="submit" value="Buscar">
            </div>
        </div>
    </form>
</div>
<%@include file="../footer/footer.jsp" %>
