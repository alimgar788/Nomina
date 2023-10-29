package controller;

import dao.SalarioDao;
import exceptions.CierreRecursosException;
import exceptions.EmpleadoNoEncontradoException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * El ServletConsultar gestiona las solicitudes HTTP relacionadas con la consulta de salarios de los empleados.
 */
@WebServlet(name = "ServletConsultarSalario", urlPatterns = {"/consulta"})
public class ServletConsultar extends HttpServlet {
    /**
     * Maneja las solicitudes GET para consultar el salario de un empleado según su DNI.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String dni = request.getParameter("dni");
        request.setAttribute("paginaActual", "consulta");
        SalarioDao salarioDao = new SalarioDao();
        try {
            if (dni == null | dni == "") {
                request.getRequestDispatcher("/views/buscador/buscador.jsp").forward(request, response);
                return;
            } else {
                request.setAttribute("dni", dni);
                Double salario = salarioDao.muestraSalarioPorDni(dni);
                request.setAttribute("salario", salario);
                request.getRequestDispatcher("/views/content/consulta-salario.jsp").forward(request, response);
            }

        } catch (CierreRecursosException e) {
            this.manejaException(e, request, response);
        } catch (SQLException e) {
            this.manejaException(e, request, response);
        } catch (EmpleadoNoEncontradoException e) {
            this.manejaException(e, request, response);
        } catch (RuntimeException e) {
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

    /**
     * Maneja las excepciones relacionadas con la falta de empleados encontrados.
     *
     * @param e        La excepción de EmpleadoNoEncontradoException que se ha capturado.
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void manejaException(EmpleadoNoEncontradoException e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "Error al encontrar al empleado. " + e.getMessage());
        request.getRequestDispatcher("/views/exception/error.jsp").forward(request, response);
    }

    /**
     * Maneja las excepciones generales e inesperadas que pueden ocurrir durante el procesamiento.
     *
     * @param e        La excepción general que se ha capturado.
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void manejaException(RuntimeException e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "Ha ocurrido un error inesperado. " + e.getMessage());
        request.getRequestDispatcher("/views/exception/error.jsp").forward(request, response);
    }
}
