package util;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {
        Connection con = ConexionBD.miConexion();

        if (con != null) {
            System.out.println("¡Conexión exitosa a BD_EduControl!");
        } else {
            System.out.println("No se pudo conectar. Revisa usuario, contraseña o que MySQL esté prendido.");
        }

        try {
            if (con != null) {
                con.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}