<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 23/10/2023
  Time: 8:47
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../header/header.jsp" %>
<div class="listado">
    <c:if test="${usuarioCreado}">
        <div class="info">
            <p>El empleado ha sido creado correctamente</p>
        </div>
    </c:if>
    <div class="cabecera">
        <h2>Lista de Empleados</h2>
    </div>
    <div class="contenido">
        <c:choose>
            <c:when test="${not empty listaEmpleados}">
                <table class="tabla">
                    <tr>
                        <th>Nombre</th>
                        <th>DNI</th>
                        <th>Sexo</th>
                        <th>Categor&iacutea Laboral</th>
                        <th>Antig&uumledad</th>
                    </tr>
                    <c:forEach var="empleado" items="${listaEmpleados}">
                        <tr>
                            <td>${empleado.nombre}</td>
                            <td>${empleado.dni}</td>
                            <td>${empleado.sexo}</td>
                            <td>${empleado.categoria}</td>
                            <td>${empleado.anyos}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <div>
                    <p>No hay empleados registrados actualmente.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<%@ include file="../footer/footer.jsp" %>