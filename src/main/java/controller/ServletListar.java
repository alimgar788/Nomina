package controller;

import dao.EmpleadoDao;
import exceptions.CierreRecursosException;
import exceptions.EmpleadoNoEncontradoException;
import model.Empleado;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * El ServletListar gestiona las solicitudes HTTP relacionadas con la visualización de la lista de empleados.
 */
@WebServlet(name = "ServletListar", urlPatterns = {"/listado"})
public class ServletListar extends HttpServlet {
    /**
     * Maneja las solicitudes GET para mostrar la lista de empleados.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("paginaActual", "listado");
        EmpleadoDao empleadoDao = new EmpleadoDao();
        try {
            List<Empleado> listaEmpleados = empleadoDao.obtenerListaEmpleados(null, null);
            Boolean creacion = request.getParameter("confirmar-creacion") != null;
            request.setAttribute("usuarioCreado", creacion);
            request.setAttribute("listaEmpleados", listaEmpleados);
            request.getRequestDispatcher("/views/content/listado-empleados.jsp").forward(request, response);

        } catch (SQLException e) {
            this.manejaException(e, request, response);
        } catch (CierreRecursosException e) {
            this.manejaException(e, request, response);
        }
    }

    /**
     * Maneja las excepciones relacionadas con consultas SQL fallidas en la base de datos.
     *
     * @param e        La excepción de SQL que se ha capturado.
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void manejaException(SQLException e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "Error al consultar la base de datos. " + e.getMessage());
        request.getRequestDispatcher("/views/exception/error.jsp").forward(request, response);
    }

    /**
     * Maneja las excepciones relacionadas con el cierre de recursos en la base de datos.
     *
     * @param e        La excepción de CierreRecursosException que se ha capturado.
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void manejaException(CierreRecursosException e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "Error al cerrar los recursos utilizados en la base de datos. " + e.getMessage());
        request.getRequestDispatcher("/views/exception/error.jsp").forward(request, response);
    }
}
