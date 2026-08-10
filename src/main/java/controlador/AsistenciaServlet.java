package controlador;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.AsistenciaDAO;
import entidad.Asistencia;


@WebServlet("/AsistenciaServlet")



public class AsistenciaServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final String VISTA = "asistencia/MantAsistencia.jsp";
	private static final String LISTAR = "listar";
	private static final String MARCAR = "marcar";
	private static final String BUSCAR = "buscar";


	
	private AsistenciaDAO asistenciaDAO;
	
	

	public AsistenciaServlet(){asistenciaDAO = new AsistenciaDAO();}

	
	
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
		if (operacion == null || operacion.trim().isEmpty()) {
			operacion = LISTAR;
		}

		try
		{
			switch (operacion)
			{
			case LISTAR:
				listar(request, response);
				break;
			case MARCAR:
				marcar(request, response);
				break;
			case BUSCAR:
				buscar(request, response);
				break;
			default:
				listar(request, response);
			}
		}
		
		catch (Exception e) {request.setAttribute("mensaje", "Error: " + e.getMessage());
							 listar(request, response);}
	}

	
	
	private void cargarDatos(HttpServletRequest request)
	{
		request.setAttribute("listaAsistencia", asistenciaDAO.listar());
	}

	
	
	private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		cargarDatos(request);
		request.getRequestDispatcher(VISTA).forward(request, response);
	}

	
	
	private void buscar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int id = Integer.parseInt(request.getParameter("id"));
		Asistencia a = asistenciaDAO.buscarPorId(id);

		cargarDatos(request);
		request.setAttribute("asistencia", a);
		request.getRequestDispatcher(VISTA).forward(request, response);
	}

	
	
	private void marcar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Asistencia a = obtenerDatosFormulario(request);
		int resultado = asistenciaDAO.insertar(a);

		if (resultado > 0)
		{
			mensaje(request, "Asistencia registrada correctamente.");
		}
		
		else {mensaje(request, "No se pudo registrar la asistencia.");}
		
		listar(request, response);
	}

	
	
	private Asistencia obtenerDatosFormulario(HttpServletRequest request)
	{
		Asistencia a = new Asistencia();

		int idPersonal = Integer.parseInt(request.getParameter("idPersonal"));
		a.setIdPersonal(idPersonal);

		a.setFecha(Date.valueOf(request.getParameter("fecha")));
		a.setHoraMarcada(Time.valueOf(request.getParameter("horaMarcada") + ":00"));
		a.setClasificacion(request.getParameter("clasificacion"));

		return a;
	}

	
	
	private void mensaje(HttpServletRequest request, String texto) {
		request.setAttribute("mensaje", texto);
	}
}