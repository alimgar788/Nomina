<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 25/10/2023
  Time: 15:05
  To change this template use File | Settings | File Templates.
--%>
<%@include file="../header/header.jsp" %>
<c:if test="${actualizado}">
    <div class="info">El empleado ha sido actualizado correctamente</div>
</c:if>
<c:if test="${eliminado}">
    <div class="info">El empleado ha sido eliminado correctamente</div>
</c:if>

<div class="filtro-busqueda-empleados">
    <h2>B&uacutesqueda de Empleados</h2>
    <form id="formulario-filtro-busqueda-empleado" action="actualiza" method="get">
        <div>
            <label>Buscar por:</label>
            <select name="campo">
                <option value="nombre" ${campo == "nombre" ? "selected" : ""}>Nombre</option>
                <option value="dni" ${campo == "dni" ? "selected" : ""}>DNI</option>
                <option value="sexo" ${campo == "sexo" ? "selected" : ""}>Sexo</option>
                <option value="categoria" ${campo == "categoria" ? "selected" : ""}>Categor&iacutea</option>
                <option value="anyos" ${campo == "anyos" ? "selected" : ""}>Antig&uumledad</option>
                <option value="salario" ${campo == "salario" ? "selected" : ""}>Salario</option>
            </select>
        </div>
        <div>
            <label>Valor de b&uacutesqueda:</label>
            <input type="text" name="valor" value="${valor}" placeholder="Filtrar por el dato...">
        </div>
        <div>
            <input type="submit" value="Buscar"/>
            <button id="reset" onclick="resetearFormulario(event)">Resetear</button>
        </div>
    </form>
</div>
<div class="listado">
    <table class="tabla">
        <tr>
            <th>Nombre</th>
            <th>DNI</th>
            <th>Sexo</th>
            <th>Categor&iacutea</th>
            <th>Antig&uumledad</th>
            <th>Editar</th>
            <th>Eliminar</th>
        </tr>
        <c:forEach items="${listaEmpleados}" var="empleado">
            <tr>
                <td>${empleado.nombre}</td>
                <td>${empleado.dni}</td>
                <td>${empleado.sexo}</td>
                <td>${empleado.categoria}</td>
                <td>${empleado.anyos}</td>
                <td>
                    <a href="actualiza?editar=${empleado.dni}">
                        <img src="/Nomina_war_exploded/views/img/editar.png" class="icono-modificar">
                    </a>
                </td>
                <td>
                    <a onclick="confirmarEliminacion(event)" href="actualiza?eliminar=${empleado.dni}">
                        <img src="/Nomina_war_exploded/views/img/papelera.png" class="icono-eliminar">
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<%@include file="../footer/footer.jsp" %>
