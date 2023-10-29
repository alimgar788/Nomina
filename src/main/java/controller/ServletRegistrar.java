package controller;

import dao.EmpleadoDao;
import dao.SalarioDao;
import exceptions.CierreRecursosException;
import exceptions.DatosNoCorrectosException;
import model.Empleado;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * El ServletRegistrar gestiona las solicitudes HTTP relacionadas con el registro de empleados.
 */
@WebServlet(name = "ServletRegistrar", urlPatterns = {"/registro"})
public class ServletRegistrar extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Maneja las solicitudes GET para mostrar la página de registro de empleados.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("paginaActual", "registro");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/content/registro.jsp");
        requestDispatcher.forward(request, response);
    }

    /**
     * Maneja las solicitudes POST para crear un nuevo registro de empleado en la base de datos.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String dni = request.getParameter("dni");
        Character sexo = request.getParameter("sexo").charAt(0);
        int categoria = Integer.parseInt(request.getParameter("categoria"));
        double anyos = Double.parseDouble(request.getParameter("anyos"));

        try {
            Empleado empl = new Empleado(nombre, dni, sexo, categoria, anyos);

            EmpleadoDao empleadoDao = new EmpleadoDao();
            empleadoDao.insertarEmpleado(empl);

            SalarioDao salarioDao = new SalarioDao();
            salarioDao.insertarSalario(empl);

            response.sendRedirect(request.getContextPath() + "/listado?confirmar-creacion=1");

        } catch (DatosNoCorrectosException e) {
            this.manejaException(e, request, response);
        } catch (SQLException e) {
            this.manejaException(e, request, response);
        } catch (CierreRecursosException e) {
            this.manejaException(e, request, response);
        } catch (Exception e) {
            this.manejaException(e, request, response);
        }
    }

    /**
     * Maneja las excepciones relacionadas con la base de datos, específicamente para errores de SQL.
     *
     * @param e        La excepción de SQL que se ha capturado.
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void manejaException(SQLException e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "El DNI que intentas registrar ya se encuentra en la base de datos.");
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
     * Maneja las excepciones relacionadas con los datos que no son correctos para la operación.
     *
     * @param e        La excepción de DatosNoCorrectosException que se ha capturado.
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void manejaException(DatosNoCorrectosException e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "Los datos que estás queriendo introducir no son correcto. " + e.getMessage());
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
    protected void manejaException(Exception e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "Ha ocurrido un error inesperado. " + e.getMessage());
        request.getRequestDispatcher("/views/exception/error.jsp").forward(request, response);
    }
}
