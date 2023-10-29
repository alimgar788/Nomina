package dao;

import connection.Connector;
import exceptions.CierreRecursosException;
import exceptions.DatosNoCorrectosException;
import model.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase EmpleadoDao proporciona métodos para realizar operaciones relacionadas con los empleados en la base de datos.
 */
public class EmpleadoDao {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet rs;

    /**
     * Obtiene una lista de empleados según un campo y valor especificados.
     *
     * @param campo El campo por el cual se va a realizar la consulta. Puede ser null.
     * @param valor El valor a buscar en el campo especificado.
     * @return Una lista de objetos Empleado que coinciden con los criterios de búsqueda.
     * @throws SQLException            Si ocurre un error de SQL.
     * @throws CierreRecursosException Si ocurre un error al cerrar los recursos.
     */
    public List<Empleado> obtenerListaEmpleados(String campo, Object valor) throws SQLException, CierreRecursosException {
        connection = obtenerConexion();
        List<Empleado> listaEmpleados = new ArrayList<>();
        String sql = "SELECT * FROM empleados";
        preparedStatement = connection.prepareStatement(sql);

        try {
            if (campo != null && campo != "" && valor != null && valor != "") {
                String columna = getColumn(campo);
                String operator = getWhereOperator(campo);
                sql = "SELECT e.*, n.salario FROM empleados e JOIN nominas n ON e.dni = n.dni WHERE " + columna + operator;
                preparedStatement = connection.prepareStatement(sql);
                setPreparedStatementValue(preparedStatement, campo, valor);
            }

            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Empleado empl = obtenerEmpleadoDeResultSet(rs);
                listaEmpleados.add(empl);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar la lista de empleados en la base de datos");
        } catch (DatosNoCorrectosException e) {
            throw new RuntimeException(e);
        } finally {
            cierraRecursosUtilizadosEnDB();
        }
        return listaEmpleados;
    }

    /**
     * Actualiza la información de un empleado en la base de datos.
     *
     * @param empl El objeto Empleado con la información actualizada.
     * @return true si se realiza la actualización correctamente, de lo contrario false.
     * @throws SQLException            Si ocurre un error de SQL.
     * @throws CierreRecursosException Si ocurre un error al cerrar los recursos.
     */
    public boolean actualizarEmpleado(Empleado empl) throws SQLException, CierreRecursosException {

        connection = obtenerConexion();
        preparedStatement = null;

        try {
            StringBuilder sqlBuilder = new StringBuilder("UPDATE empleados SET ");
            List<Object> params = new ArrayList<>();

            if (empl.getNombre() != null) {
                sqlBuilder.append("nombre = ?, ");
                params.add(empl.getNombre());
            }
            if (empl.getSexo() != null) {
                sqlBuilder.append("sexo = ?, ");
                params.add(String.valueOf(empl.getSexo()));
            }
            if (empl.getCategoria() > 0 && empl.getCategoria() < 10) {
                sqlBuilder.append("categoria = ?, ");
                params.add(empl.getCategoria());
            }
            if (empl.getAnyos() >= 0.0) {
                sqlBuilder.append("anyos = ?, ");
                params.add(empl.getAnyos());
            }

            sqlBuilder.setLength(sqlBuilder.length() - 2); //así eliminamos la última coma y el último espacio para que no de error la consulta

            sqlBuilder.append(" WHERE dni = ?");
            params.add(empl.getDni());

            String sql = sqlBuilder.toString();

            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            Boolean filasAfectadas = preparedStatement.executeUpdate() > 0;
            if (filasAfectadas) {
                SalarioDao salarioDao = new SalarioDao();
                salarioDao.actualizaSalario(empl);
            }
            return filasAfectadas;

        } finally {
            cierraRecursosUtilizadosEnDB();
        }
    }

    /**
     * Obtiene un objeto Empleado de la base de datos según el DNI especificado.
     *
     * @param dni El DNI del empleado a buscar.
     * @return El objeto Empleado que corresponde al DNI especificado.
     * @throws CierreRecursosException Si ocurre un error al cerrar los recursos.
     * @throws SQLException            Si ocurre un error de SQL.
     */
    public Empleado obtenerEmpleadoPorDni(String dni) throws CierreRecursosException, SQLException {
        connection = obtenerConexion();
        preparedStatement = null;

        ResultSet rs = null;
        Empleado empl = null;

        try {
            String sql = "SELECT * from empleados WHERE dni=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dni);

            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                Character sexo = rs.getString("sexo").charAt(0);
                int categoria = rs.getInt("categoria");
                double anyos = rs.getDouble("anyos");

                empl = new Empleado(nombre, dni, sexo, categoria, anyos);
            } else {
                System.out.println("No se encontro un empleado con el DNI: " + dni);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar los datos de los empleados de la base de datos");
        } catch (DatosNoCorrectosException e) {
            throw new RuntimeException(e);
        } finally {
            cierraRecursosUtilizadosEnDB();
        }
        return empl;
    }

    /**
     * Inserta un nuevo empleado en la base de datos.
     *
     * @param empl El objeto Empleado a ser insertado en la base de datos.
     * @return true si se realiza la inserción correctamente, de lo contrario false.
     * @throws SQLException            Si ocurre un error de SQL.
     * @throws CierreRecursosException Si ocurre un error al cerrar los recursos.
     */
    public boolean insertarEmpleado(Empleado empl) throws SQLException, CierreRecursosException {
        connection = obtenerConexion();

        String checkSql = "SELECT COUNT(*) AS count FROM empleados WHERE dni = ?";

        try (PreparedStatement checkSqlStatement = connection.prepareStatement(checkSql)) {
            checkSqlStatement.setString(1, empl.getDni());
            try (ResultSet rs = checkSqlStatement.executeQuery()) {
                rs.next();
                int count = rs.getInt("count");

                if (count > 0) {
                    System.out.println("El empleado con DNI " + empl.getDni() + " ya está registrado en la base de datos.");
                    return false;
                }
            }
        }

        String sql = "INSERT INTO empleados(nombre, dni, sexo, categoria, anyos) VALUE (?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, empl.getNombre());
            preparedStatement.setString(2, empl.getDni());
            preparedStatement.setString(3, empl.getSexo().toString());
            preparedStatement.setInt(4, empl.getCategoria());
            preparedStatement.setDouble(5, empl.getAnyos());

            int filasInsertadas = preparedStatement.executeUpdate();
            return filasInsertadas > 0;
        } finally {
            cierraRecursosUtilizadosEnDB();
        }
    }

    /**
     * Elimina un empleado de la base de datos según el DNI especificado.
     *
     * @param dni El DNI del empleado que se va a eliminar.
     * @return true si se realiza la eliminación correctamente, de lo contrario false.
     * @throws SQLException            Si ocurre un error de SQL.
     * @throws CierreRecursosException Si ocurre un error al cerrar los recursos.
     */
    public boolean eliminarEmpleado(String dni) throws SQLException, CierreRecursosException {
        connection = obtenerConexion();
        preparedStatement = null;
        String sql = "DELETE FROM empleados WHERE dni = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dni);
            int filasAfectadas = preparedStatement.executeUpdate();
            return filasAfectadas > 0;
        } finally {
            cierraRecursosUtilizadosEnDB();
        }
    }

    /**
     * Establece los valores correspondientes en un objeto PreparedStatement según el campo y el valor especificados.
     *
     * @param preparedStatement El objeto PreparedStatement al cual se le van a establecer los valores.
     * @param campo             El campo en la base de datos para el que se va a establecer el valor.
     * @param valor             El valor que se va a establecer en el campo correspondiente.
     * @throws SQLException Si ocurre un error de SQL.
     */
    private void setPreparedStatementValue(PreparedStatement preparedStatement, String campo, Object valor) throws SQLException {
        switch (campo) {
            case "nombre":
            case "dni":
                preparedStatement.setString(1, "%" + valor.toString().toLowerCase() + "%");
                break;
            case "sexo":
                if(valor instanceof String){
                    String sexo = (String) valor;
                    if(sexo.length() ==1){
                        char sexoMinuscula = Character.toLowerCase(sexo.charAt(0));
                        if(sexoMinuscula == 'm' || sexoMinuscula == 'f' || sexoMinuscula == 'i'){
                            preparedStatement.setString(1, String.valueOf(sexoMinuscula));
                        }else{
                            throw new IllegalArgumentException("Solo se puede escribir en el sexo los caracteres m, f o i. Pruebe con un caracter valido");
                        }
                    }else{
                        throw new IllegalArgumentException("El campo sexo debe tener sólo un caracter. Inténtelo de nuevo");
                    }
                }else{
                    throw new IllegalArgumentException("El valor de sexo no es una cadena de caracteres.");
                }
                break;

            case "categoria":
                try{
                    int valorInt = Integer.parseInt((String) valor);
                    preparedStatement.setInt(1, valorInt);
                }catch (IllegalArgumentException e){
                    throw new IllegalArgumentException("El valor introducido no es un numero entero.");
                }
                break;

            case "salario":
            case "anyos":
                try {
                    String valorString = (String) valor;
                    double valorDouble = Double.parseDouble(valorString.replace(',', '.'));
                    preparedStatement.setDouble(1, valorDouble);
                }catch (NumberFormatException e){
                    throw new IllegalArgumentException("El valor introducido no es un numero decimal.");
                }
                break;
        }
    }

    private String getWhereOperator(String campo) {
        String operator = " = ?";
        switch (campo) {
            case "nombre":
            case "dni":
                operator = " like ?";
                break;
      }
      return operator;
    }
    private String getColumn(String campo) {
        String operator = " = ";
        switch (campo) {
            case "nombre":
            case "dni":
                operator = " LOWER("+campo+")";
                break;
      }
      return operator;
    }
    /**
     * Crea un objeto Empleado a partir de un conjunto de resultados de ResultSet.
     *
     * @param rs El conjunto de resultados de ResultSet del cual se obtendrán los valores para crear el objeto Empleado.
     * @return Un objeto Empleado creado a partir de los resultados de ResultSet.
     * @throws SQLException              Si ocurre un error de SQL.
     * @throws DatosNoCorrectosException Si los datos obtenidos no son correctos.
     */
    private Empleado obtenerEmpleadoDeResultSet(ResultSet rs) throws SQLException, DatosNoCorrectosException {
        String nombre = rs.getString("nombre");
        String dni = rs.getString("dni");
        Character sexo = rs.getString("sexo").charAt(0);
        int categoria = rs.getInt("categoria");
        double anyos = rs.getDouble("anyos");

        return new Empleado(nombre, dni, sexo, categoria, anyos);
    }

    /**
     * Obtiene una conexión a la base de datos.
     *
     * @return Una conexión a la base de datos.
     * @throws SQLException Si ocurre un error al intentar establecer la conexión.
     */
    private Connection obtenerConexion() throws SQLException {
        return Connector.getConnection();
    }

    /**
     * Cierra los recursos utilizados en la base de datos.
     *
     * @throws CierreRecursosException Si ocurre un error al intentar cerrar los recursos.
     */
    private void cierraRecursosUtilizadosEnDB() throws CierreRecursosException {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new CierreRecursosException("Los recursos utilizados para la base de datos no se han podido cerrar correctamente");
        }
    }

}