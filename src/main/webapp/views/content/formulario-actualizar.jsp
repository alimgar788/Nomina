<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 23/10/2023
  Time: 8:46
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../header/header.jsp" %>
<div class="formulario">
    <h2>Actualizar Detalles del Empleado</h2>
    <form action="actualiza?editar=${editar}" method="post">
        <div>
            <label>Nombre:</label>
            <input type="text" name="nombre" value="${empleado.nombre}" required>
        </div>
        <div>
            <label>DNI:</label>
            <input type="text" name="dni" value="${empleado.dni}" readonly required>
        </div>
        <div>
            <label>Sexo:</label>
            <div id="formulario-sexo">
                <div>
                    <label>Masculino</label>
                    <input type="radio" id="masculino" ${empleado.sexo.toString() == "m" ? "checked" : ""} name="sexo" value="m" required>
                </div>
                <div>
                    <label>Femenino</label>
                    <input type="radio" id="femenino" ${empleado.sexo.toString() == "f" ? "checked" : ""} name="sexo" value="f" required>
                </div>
                <div>
                    <label>Prefiero no contestar</label>
                    <input type="radio" id="no_sexo" ${empleado.sexo.toString() == "i" ? "checked" : ""} name="sexo" value="i" required>
                </div>
            </div>
        </div>
        <div>
            <label>Categor&iacutea:</label>
            <select name="categoria" id="categoria">
                <% for (int i = 1; i < 10; i++) { %>
                <option value="<%= i %>"><%= i %>
                    ${empleado.categoria == i ? "selected" : ""}
                </option>
                <% } %>
            </select>
        </div>
        <div>
            <label>Antig&uumledad:</label>
            <input type="number" min="0" max="65" step="0.1" name="anyos" value="${empleado.anyos}" required>
        </div>
        <input type="submit" value="Actualizar">
    </form>
</div>
<%@ include file="../footer/footer.jsp" %>