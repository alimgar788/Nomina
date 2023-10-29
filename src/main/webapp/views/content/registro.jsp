<%--
  Created by IntelliJ IDEA.
  User: alg19
  Date: 23/10/2023
  Time: 8:47
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../header/header.jsp" %>
<div class="formulario">
    <h2>Registro de nuevo empleado</h2>
    <form id="formulario-registro" action="registro" method="post">
        <div>
            <label>Nombre:</label>
            <input type="text" id="nombre" name="nombre" placeholder="Ej: Alba Lima Garcia" required>
        </div>
        <div>
            <label>DNI:</label>
            <input type="text" id="dni" name="dni" placeholder="Ej: 12345678A" required>
        </div>
        <label>Sexo:</label>
        <div id="formulario-sexo">
            <div>
                <label>Masculino</label>
                <input type="radio" id="masculino" name="sexo" value="m" required>
            </div>
            <div>
                <label>Femenino</label>
                <input type="radio" id="femenino" name="sexo" value="f" required>
            </div>
            <div>
                <label>Prefiero no contestar</label>
                <input type="radio" id="no_sexo" name="sexo" value="i" required>
            </div>
        </div>
        <div>
            <label>Categor&iacutea:</label>
            <select name="categoria" id="categoria">
                <% for (int i = 1; i < 10; i++) { %>
                <option value="<%= i %>"><%= i %>
                </option>
                <% } %>
            </select>
        </div>
        <div>
            <label>Antig&uumledad</label>
            <input type="number" step="0.1" id="anyos" name="anyos" min="0" required>
        </div>
        <input type="submit" value="Registrar">
    </form>
</div>
<%@ include file="../footer/footer.jsp" %>
