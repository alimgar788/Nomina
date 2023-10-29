<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 23/10/2023
  Time: 8:47
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../header/header.jsp" %>
<div class="consulta-salario">
    <c:choose>
        <c:when test="${not empty salario}">
            <div>
                <h3>El salario correspondiente al empleado:</h3>
            </div>
            <div>
                <p>Con dni <strong>${dni}</strong> es de: <strong>${salario} &euro;</strong> al mes </p>
            </div>
        </c:when>
        <c:when test="${not empty error}">
            <div>
                <p>${error}</p>
            </div>
        </c:when>
        <c:otherwise>
            <div>
                <p>No se encuentra salario asociado a este dni</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="../footer/footer.jsp" %>

