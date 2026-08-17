package controlador;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.PersonalDAO;
import entidad.Personal;



@WebServlet("/LoginServlet")

public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	
	private static final String VISTA = "login/login.jsp";
	private static final String LOGIN = "login";
	private static final String LOGOUT = "logout";
	
	
	private PersonalDAO personalDAO;
	
	
	public LoginServlet()
	{
		personalDAO = new PersonalDAO();
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		procesar(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		procesar(request, response);
	}
	
	
	private void procesar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		
		String operacion = request.getParameter("accion");
		if(operacion == null || operacion.trim().isEmpty())
		{
			operacion = "mostrar";
		}
		
		try
		{
			switch(operacion) {
			case LOGIN:login(request, response);
			break;
			case LOGOUT:logout(request, response);
			break;
			default:mostrarFormulario(request, response);
			}
		}
		
		catch (Exception e) {request.setAttribute("mensaje", "Error ..." + e.getMessage()); mostrarFormulario(request, response);}
		
	}
	
	
	private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher(VISTA).forward(request, response);
	}
	
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String usuario = request.getParameter("usuario");
		String contrasena = request.getParameter("contrasena");
		
		Personal personalEncontrado = personalDAO.buscarPorUsuario(usuario);
		if(personalEncontrado != null && personalEncontrado.getContrasena().equals(contrasena))
		{
			HttpSession session = request.getSession();
			session.setAttribute("usuarioLogueado", personalEncontrado);
			session.setAttribute("rol", personalEncontrado.getRol());
			response.sendRedirect("dashboard/Dashboard.jsp");
		}
		else
		{
			request.setAttribute("mensaje", "Usuario o Contraseña Incorrectos!!!");
			mostrarFormulario(request, response);
		}
	}
	
	
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect(VISTA);
	}
	

}
