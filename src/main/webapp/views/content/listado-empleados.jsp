<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 23/10/2023
  Time: 8:47
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../header/header.jsp" %>
    <h2>Lista de Empleados</h2>
    <c:if test="${usuarioCreado}">
        <div class="info">
            <p>El empleado ha sido creado correctamente</p>
        </div>
    </c:if>
<div class="contenido">
        <c:choose>
            <c:when test="${not empty listaEmpleados}">
                <table class="tabla">
                    <tr>
                        <th>Nombre</th>
                        <th>DNI</th>
                        <th class="center">Sexo</th>
                        <th class="center">Categor&iacutea</th>
                        <th class="center">Antig&uumledad</th>
                    </tr>
                    <c:forEach var="empleado" items="${listaEmpleados}">
                        <tr>
                            <td>${empleado.nombre}</td>
                            <td>${empleado.dni}</td>
                            <td class="center">${empleado.getSexoFormato()}</td>
                            <td class="center">${empleado.categoria}</td>
                            <td class="center">${empleado.anyos}</td>
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
<%@ include file="../footer/footer.jsp" %>