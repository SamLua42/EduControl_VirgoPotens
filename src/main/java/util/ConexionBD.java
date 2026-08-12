package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
	
	private static final String URL = "jdbc:mysql://localhost:3306/BD_EduControl";
	private static final String USER = "root";
	private static final String PASSWORD = "Tam181113";
	
	
	public static Connection miConexion()
	{
		Connection con = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL,USER,PASSWORD);
			}
		catch (Exception e) {
			System.out.println("Error al conectar a la BD_EduControl" + e.getMessage());
			e.printStackTrace();
			}
		
		return con;
	}
	

}
