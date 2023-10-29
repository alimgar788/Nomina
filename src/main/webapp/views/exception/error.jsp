<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 23/10/2023
  Time: 14:43
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../header/header.jsp" %>
<div class="error">
    <div>
        <h3>Oh vaya! Ha ocurrido un error</h3>
    </div>
    <c:if test="${not empty mensajeError}">
        <div>
            <p>${mensajeError}</p>
        </div>
    </c:if>

    <%
        int statusCode = response.getStatus();
        String mensajeError = null;
        if (statusCode == 404) {
            mensajeError = "No se encontró el recurso solicitado. " +
                    "Vuelve a la página anterior e inténtalo de nuevo.";
        } else if (statusCode == 500) {
            mensajeError = "Se produjo un error en el servidor. Inténtalo de nuevo más tarde.";
        } else {
            mensajeError = "Ha ocurrido un error inesperado. Vuelve a la página anterior e inténtalo de nuevo.";
        }
        request.setAttribute("mensajeError", mensajeError);
    %>
</div>
<%@ include file="../footer/footer.jsp" %>
