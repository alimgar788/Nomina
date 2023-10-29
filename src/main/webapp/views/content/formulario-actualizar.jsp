<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 23/10/2023
  Time: 8:46
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../header/header.jsp" %>
<h2>Actualizar Detalles del Empleado</h2>
<div class="formulario">
    <form action="actualiza?editar=${editar}" method="post">
        <div class="form_group">
            <input class="form_field" type="text" name="nombre" value="${empleado.nombre}" required>
            <label class="form_label">Nombre:</label>
        </div>
        <div class="form_group">
            <input class="form_field" type="text" name="dni" value="${empleado.dni}" readonly required>
            <label class="form_label">DNI:</label>
        </div>
        <div>
            <label>Sexo:</label>
            <div id="formulario-sexo">
                <label class="radio-button">
                    <input type="radio" ${empleado.sexo.toString() == "m" ? "checked" : ""} name="sexo" value="m" required>
                    <span class="radio"></span>
                    Masculino
                </label>
                <label class="radio-button">
                    <input type="radio" ${empleado.sexo.toString() == "f" ? "checked" : ""} name="sexo" value="f" required>
                    <span class="radio"></span>
                    Femenino
                </label>
                <label class="radio-button">
                    <input type="radio" ${empleado.sexo.toString() == "i" ? "checked" : ""} name="sexo" value="i" required>
                    <span class="radio"></span>
                    N/C
                </label>
            </div>
        </div>
        <div class="form_group">
            <select class="form_field select" name="categoria" id="categoria">
                <% for (int i = 1; i < 10; i++) { %>
                <option value="<%= i %>"><%= i %>
                    ${empleado.categoria == i ? "selected" : ""}
                </option>
                <% } %>
            </select>
            <label class="form_label">Categor&iacutea:</label>
        </div>
        <div class="form_group">
            <input class="form_field" type="number" min="0" max="65" step="0.1" name="anyos" value="${empleado.anyos}" required>
            <label class="form_label">Antig&uumledad</label>
        </div>
        <input type="submit" value="Actualizar">
    </form>
</div>
<%@ include file="../footer/footer.jsp" %>