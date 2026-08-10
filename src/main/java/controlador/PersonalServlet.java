package controlador;

import java.io.IOException;
import java.sql.Time;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.PersonalDAO;
import entidad.Personal;



@WebServlet("/PersonalServlet")


public class PersonalServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final String VISTA = "personal/MantPersonal.jsp";
	private static final String LISTAR = "listar";
	private static final String REGISTRAR = "registrar";
	private static final String ACTUALIZAR = "actualizar";
	private static final String BUSCAR = "buscar";
	private static final String ELIMINAR = "eliminar";

	private PersonalDAO personalDAO;

	

	public PersonalServlet() {personalDAO = new PersonalDAO();}
	

	
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
		if (operacion == null || operacion.trim().isEmpty())
		{
			operacion = LISTAR;
		}

		try
		{
			switch (operacion)
			{
			case LISTAR:
				listar(request, response);
				break;
			case REGISTRAR:
				registrar(request, response);
				break;
			case ACTUALIZAR:
				actualizar(request, response);
				break;
			case BUSCAR:
				buscar(request, response);
				break;
			case ELIMINAR:
				eliminar(request, response);
				break;
			default:
				listar(request, response);
			}
		}
		
		catch (Exception e) {request.setAttribute("mensaje", "Error: " + e.getMessage());
							 listar(request, response);}
	}

	
	
	private void cargarDatos(HttpServletRequest request) {
		request.setAttribute("listaPersonal", personalDAO.listar());
	}

	
	
	private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		cargarDatos(request);
		request.getRequestDispatcher(VISTA).forward(request, response);
	}

	
	
	private void buscar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int id = Integer.parseInt(request.getParameter("id"));
		Personal p = personalDAO.buscarPorId(id);

		cargarDatos(request);
		request.setAttribute("personal", p);
		request.setAttribute("editar", true);
		request.getRequestDispatcher(VISTA).forward(request, response);
	}

	
	
	private void registrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Personal p = obtenerDatosFormulario(request);
		int resultado = personalDAO.insertar(p);

		if (resultado > 0)
		{
			mensaje(request, "Personal registrado correctamente.");
		}
		
		else {mensaje(request, "No se pudo registrar el personal.");}
		
		listar(request, response);
	}

	
	
	private void actualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Personal p = obtenerDatosFormulario(request);
		int resultado = personalDAO.actualizar(p);

		if (resultado > 0)
		{
			mensaje(request, "Personal actualizado correctamente.");
		}
		
		else {mensaje(request, "No se pudo actualizar el personal.");}
		
		listar(request, response);
	}

	
	
	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int id = Integer.parseInt(request.getParameter("id"));
		int resultado = personalDAO.eliminar(id);

		if (resultado > 0)
		{
			mensaje(request, "Personal eliminado correctamente.");
		}
		else {mensaje(request, "No se pudo eliminar el personal.");}
		
		listar(request, response);
	}

	
	
	private Personal obtenerDatosFormulario(HttpServletRequest request)
	{
		Personal p = new Personal();

		String idPersonal = request.getParameter("idPersonal");
		if (idPersonal != null && !idPersonal.trim().isEmpty())
		{
			p.setIdPersonal(Integer.parseInt(idPersonal));
		}

		p.setNombre(request.getParameter("nombre"));
		p.setApellido(request.getParameter("apellido"));
		p.setDni(request.getParameter("dni"));
		p.setCargo(request.getParameter("cargo"));
		p.setTipoPersonal(request.getParameter("tipoPersonal"));
		p.setHoraEntradaEsperada(Time.valueOf(request.getParameter("horaEntradaEsperada") + ":00"));
		p.setUsuario(request.getParameter("usuario"));
		p.setContrasena(request.getParameter("contrasena"));
		p.setRol(request.getParameter("rol"));

		return p;
	}

	
	
	private void mensaje(HttpServletRequest request, String texto) {request.setAttribute("mensaje", texto);}
}
	
	
	

