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
	private static final String CAMBIAR_CLAVE = "cambiarClave";

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

		jakarta.servlet.http.HttpSession session = request.getSession();
		Object usuarioSesion = session.getAttribute("usuarioLogueado");

		if (usuarioSesion == null)
		{
		    response.sendRedirect(request.getContextPath() + "/login/login.jsp");
		    return;
		}

		entidad.Personal usuarioActual = (entidad.Personal) usuarioSesion;
		String rolActual = usuarioActual.getRol();
		boolean esAdministrador = rolActual.equalsIgnoreCase("Administrador");
		boolean esDirector = rolActual.equalsIgnoreCase("Director");

		String operacion = request.getParameter("accion");
		if (operacion == null || operacion.trim().isEmpty())
		{
		    operacion = LISTAR;
		}

		boolean esCambiarClave = CAMBIAR_CLAVE.equals(operacion);

		if (!esAdministrador && !esDirector && !esCambiarClave)
		{
		    response.sendRedirect(request.getContextPath() + "/DashboardServlet");
		    return;
		}

		boolean esAccionDeEscritura = operacion.equals(REGISTRAR) || operacion.equals(ACTUALIZAR) || operacion.equals(ELIMINAR);
		if (esDirector && esAccionDeEscritura)
		{
		    response.sendRedirect(request.getContextPath() + "/PersonalServlet?accion=listar");
		    return;
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
			case CAMBIAR_CLAVE:
			    cambiarClave(request, response);
			    break;
			default:
			    listar(request, response);
			}
		}
		
		catch (Exception e)
		{
			request.setAttribute("error", "Ocurrió un error: " + e.getMessage());
		    try
		    {
		        listar(request, response);
		    }
		    
		    catch (Exception ex) {ex.printStackTrace();}
		}
	}

	
	
	private void cargarDatos(HttpServletRequest request)
	{
	    request.setAttribute("listaPersonal", personalDAO.listar());

	    Personal u = (Personal) request.getSession().getAttribute("usuarioLogueado");
	    boolean puedeEditar = u != null && u.getRol().equalsIgnoreCase("Administrador");
	    request.setAttribute("puedeEditar", puedeEditar);
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
		String horaParam = request.getParameter("horaEntradaEsperada");
		if (horaParam != null && !horaParam.trim().isEmpty()) {
		    // Si ya viene con segundos (HH:mm:ss), úsalo tal cual; si no, agrégalos
		    if (horaParam.length() == 5)
		    {
		        horaParam = horaParam + ":00";
		    }
		    p.setHoraEntradaEsperada(Time.valueOf(horaParam));
		}
		p.setUsuario(request.getParameter("usuario"));
		String contrasenaParam = request.getParameter("contrasena");
		if (contrasenaParam != null && !contrasenaParam.trim().isEmpty())
		{
		    p.setContrasena(contrasenaParam);
		}
		
		else if (p.getIdPersonal() > 0)
		{
		    Personal existente = personalDAO.buscarPorId(p.getIdPersonal());
		    if (existente != null)
		    {
		        p.setContrasena(existente.getContrasena());
		    }
		}
		p.setRol(request.getParameter("rol"));

		return p;
	}

	
	
	private void cambiarClave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	    jakarta.servlet.http.HttpSession session = request.getSession();
	    Personal usuarioSesion = (Personal) session.getAttribute("usuarioLogueado");

	    if (usuarioSesion == null)
	    {
	        response.sendRedirect(request.getContextPath() + "/login/login.jsp");
	        return;
	    }

	    String claveActual = request.getParameter("claveActual");
	    String claveNueva = request.getParameter("claveNueva");

	    Personal p = personalDAO.buscarPorId(usuarioSesion.getIdPersonal());

	    if (p != null && p.getContrasena().equals(claveActual))
	    {
	        p.setContrasena(claveNueva);
	        int resultado = personalDAO.actualizar(p);

	        if (resultado > 0)
	        {
	            session.setAttribute("usuarioLogueado", p);
	            request.setAttribute("mensajeConfig", "Contrasena actualizada correctamente.");
	        }
	        else
	        {
	            request.setAttribute("errorConfig", "No se pudo actualizar la contrasena.");
	        }
	    }
	    else
	    {
	        request.setAttribute("errorConfig", "La contrasena actual no es correcta.");
	    }

	    request.getRequestDispatcher("configuracion/Configuracion.jsp").forward(request, response);
	}
	
	
	private void mensaje(HttpServletRequest request, String texto) {request.setAttribute("mensaje", texto);}
}
	
	
	

