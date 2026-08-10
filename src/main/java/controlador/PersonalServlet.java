package controlador;

import java.io.IOException;
import java.sql.Time;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import entidad.Personal;
import dao.PersonalDAO;


@WebServlet("/PersonalServlet")

public class PersonalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	public static final String VISTA = "personal/MantPersonal.jsp";
	
	public static final String LISTAR = "listar";
	public static final String REGISTRAR = "registar";
	public static final String ACTUALIZAR = "actualizar";
	public static final String BUSCAR = "buscar";
	public static final String ELIMINAR = "eliminar";
	
	
	
	private PersonalDAO personalDAO;
	
	public PersonalServlet()
	{
		personalDAO = new PersonalDAO();
	}

	
	
	protected  void doGet() {
		
	}
	
	
	
	
	
	
}
