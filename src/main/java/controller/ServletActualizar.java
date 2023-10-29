package controller;

import dao.EmpleadoDao;
import exceptions.CierreRecursosException;
import exceptions.DatosNoCorrectosException;
import model.Empleado;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * El ServletActualizar gestiona las solicitudes HTTP relacionadas con la actualización y eliminación de empleados.
 */
@WebServlet(name = "ServletActualizar", urlPatterns = {"/actualiza"})
public class ServletActualizar extends HttpServlet {
    /**
     * Crea una nueva instancia de ServletActualizar.
     */
    public ServletActualizar() {
        super();
    }

    /**
     * Inicializa el servlet, cargando el controlador de la base de datos.
     */
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja las solicitudes GET para redirigir según la acción de actualización o eliminación del empleado.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String editar = request.getParameter("editar");
        String eliminar = request.getParameter("eliminar");
        request.setAttribute("paginaActual", "actualiza");
        if (editar != null) {
            this.redireccionaActualiza(editar, request, response);
        } else if (eliminar != null) {
            this.redireccionaEliminar(eliminar, request, response);
        } else {
            this.redireccionaListado(request, response);
        }
    }

    /**
     * Maneja las solicitudes POST para actualizar los datos de un empleado en la base de datos.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dniOriginal = request.getParameter("editar");
        String nombre = request.getParameter("nombre");
        String dni = request.getParameter("dni");
        String sexo = request.getParameter("sexo");
        String categoria = request.getParameter("categoria");
        String anyos = request.getParameter("anyos");

        EmpleadoDao empleadoDao = new EmpleadoDao();

        try {
            Empleado empl = empleadoDao.obtenerEmpleadoPorDni(dniOriginal);

            empl.setNombre(nombre);
            empl.setSexo(sexo.charAt(0));
            empl.setCategoria(Integer.parseInt(categoria));
            empl.setAnyos(Double.parseDouble(anyos));
            empl.setDni(dni);

            boolean empleadoActualizado = empleadoDao.actualizarEmpleado(empl);

            if (empleadoActualizado) {
                response.sendRedirect(request.getContextPath() + "/actualiza?confirmar-actualizacion=1");
            } else {
                response.getWriter().println("Error al actualizar el empleado en la base de datos");
            }

        } catch (SQLException e) {
            this.manejaException(e, request, response);
        } catch (CierreRecursosException e) {
            this.manejaException(e, request, response);
        } catch (DatosNoCorrectosException e) {
            this.manejaException(e, request, response);
        } catch (NumberFormatException e) {
            this.manejaException(e, request, response);
        } catch (IllegalArgumentException e) {
            this.manejaException(e, request, response);
        }
    }

    /**
     * Redirecciona a la página de listado de empleados después de realizar una operación de actualización o eliminación.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void redireccionaListado(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EmpleadoDao empleadoDao = new EmpleadoDao();
        String campo = request.getParameter("campo");
        String valor = request.getParameter("valor");
        String actualizado = request.getParameter("confirmar-actualizacion");
        String eliminado = request.getParameter("confirmar-eliminacion");
        List<Empleado> listaEmpleados = null;
        try {
            listaEmpleados = empleadoDao.obtenerListaEmpleados(campo, valor);
            request.setAttribute("campo", campo != null ? campo : "");
            request.setAttribute("valor", valor != null ? valor : "");
            request.setAttribute("listaEmpleados", listaEmpleados);
            request.setAttribute("actualizado", actualizado != null);
            request.setAttribute("eliminado", eliminado != null);
            request.getRequestDispatcher("/views/content/listado-actualizacion-empleados.jsp").forward(request, response);
        } catch (SQLException e) {
            this.manejaException(e, request, response);
        } catch (CierreRecursosException e) {
            this.manejaException(e, request, response);
        }
    }

    /**
     * Redirecciona a la página de eliminación de empleados después de eliminar un empleado.
     *
     * @param dni      El DNI del empleado a eliminar.
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void redireccionaEliminar(String dni, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EmpleadoDao empleadoDao = new EmpleadoDao();
        try {
            boolean eliminado = empleadoDao.eliminarEmpleado(dni);
            if (eliminado) {
                response.sendRedirect(request.getContextPath() + "/actualiza?confirmar-eliminacion=1");
            } else {
                response.getWriter().println("Error al eliminar el empleado en la base de datos");
            }
        } catch (SQLException e) {
            this.manejaException(e, request, response);
        } catch (CierreRecursosException e) {
            this.manejaException(e, request, response);
        }
    }

    /**
     * Redirecciona a la página de actualización de empleados para un empleado específico.
     *
     * @param dni      El DNI del empleado a actualizar.
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void redireccionaActualiza(String dni, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EmpleadoDao empleadoDao = new EmpleadoDao();
        Empleado empleado = null;
        try {
            empleado = empleadoDao.obtenerEmpleadoPorDni(dni);
            request.setAttribute("editar", dni);
            request.setAttribute("empleado", empleado);
            request.getRequestDispatcher("/views/content/formulario-actualizar.jsp").forward(request, response);
        } catch (SQLException e) {
            this.manejaException(e, request, response);
        } catch (CierreRecursosException e) {
            this.manejaException(e, request, response);
        }
    }

    /**
     * Maneja las excepciones SQLException y CierreRecursosException generadas durante las operaciones en la base de datos.
     *
     * @param e        La excepción que se ha generado.
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP a enviar.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException      Si ocurre un error de E/S al procesar la solicitud.
     */
    protected void manejaException(SQLException e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "Error SQL al actualizar el empleado en la base de datos. " + e.getMessage());
        request.getRequestDispatcher("/views/exception/error.jsp").forward(request, response);
    }

    /**
     * Maneja la excepción CierreRecursosException generada durante el cierre de recursos en la base de datos.
     *
     * @param e        La excepción que se ha generado.
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
     * Maneja la excepción de tipo IllegalArgumentException, configurando un mensaje de error y redirigiendo a una página de error específica.
     *
     * @param e        La excepción IllegalArgumentException que se va a manejar.
     * @param request  El objeto HttpServletRequest utilizado para la solicitud HTTP.
     * @param response El objeto HttpServletResponse utilizado para la respuesta HTTP.
     * @throws ServletException Si ocurre un error relacionado con el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida durante el manejo de la excepción.
     */
    protected void manejaException(IllegalArgumentException e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "Argumento inválido. " + e.getMessage());
        request.getRequestDispatcher("/views/exception/error.jsp").forward(request, response);
    }
    /**
     * Maneja la excepción DatosNoCorrectosException.
     * @param e La excepción DatosNoCorrectosException que se va a manejar.
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException si ocurre un error en el servlet.
     * @throws IOException si ocurre un error de E/S.
     */
    protected void manejaException(DatosNoCorrectosException e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "Error en los datos del empleado. " + e.getMessage());
        request.getRequestDispatcher("/views/exception/error.jsp").forward(request, response);
    }
    /**
     * Maneja la excepción NumberFormatException.
     * @param e La excepción NumberFormatException que se va a manejar.
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException si ocurre un error en el servlet.
     * @throws IOException si ocurre un error de E/S.
     */
    protected void manejaException(NumberFormatException e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensajeError", "Error de formato de número al actualizar el empleado. " + e.getMessage());
        request.getRequestDispatcher("/views/exception/error.jsp").forward(request, response);
    }

}
