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
import entidad.Personal;
import dao.PersonalDAO;


@WebServlet("/AsistenciaServlet")



public class AsistenciaServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final String VISTA = "asistencia/MantAsistencia.jsp";
	private static final String LISTAR = "listar";
	private static final String MARCAR = "marcar";
	private static final String BUSCAR = "buscar";


	
	private AsistenciaDAO asistenciaDAO;
	private PersonalDAO personalDAO;

	
	public AsistenciaServlet(){asistenciaDAO = new AsistenciaDAO();personalDAO = new PersonalDAO();}

	
	
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

	    java.util.Map<Integer, Personal> mapaPersonal = new java.util.HashMap<>();
	    for (Personal p : personalDAO.listar())
	    {
	        mapaPersonal.put(p.getIdPersonal(), p);
	    }
	    request.setAttribute("mapaPersonal", mapaPersonal);
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
	    jakarta.servlet.http.HttpSession session = request.getSession();
	    Personal p = (Personal) session.getAttribute("usuarioLogueado");

	    if (p == null)
	    {
	        mensaje(request, "Debe iniciar sesión para marcar asistencia.");
	        listar(request, response);
	        
	        return;
	    }

	    java.sql.Date fechaHoy = new java.sql.Date(System.currentTimeMillis());
	    java.sql.Time horaActual = new java.sql.Time(System.currentTimeMillis());

	    // Verificar si ya marcó hoy (evita doble marca)
	    boolean yaMarco = false;
	    for (Asistencia existente : asistenciaDAO.listar())
	    {
	        if (existente.getIdPersonal() == p.getIdPersonal() && existente.getFecha().equals(fechaHoy))
	        {
	            yaMarco = true;
	            break;
	        }
	    }

	    if (yaMarco)
	    {
	        mensaje(request, "Ya registraste tu asistencia el día de hoy.");
	        listar(request, response);
	       
	        return;
	    }

	    String clasificacion = calcularClasificacion(p.getHoraEntradaEsperada(), horaActual);
	    Asistencia a = new Asistencia();
	    a.setIdPersonal(p.getIdPersonal());
	    a.setFecha(fechaHoy);
	    a.setHoraMarcada(horaActual);
	    a.setClasificacion(clasificacion);

	    int resultado = asistenciaDAO.insertar(a);

	    if (resultado > 0)
	    {
	        mensaje(request, "Asistencia registrada como: " + clasificacion + ".");
	    } else {
	        mensaje(request, "No se pudo registrar la asistencia.");
	    }
	    
	    listar(request, response);
	}

	private String calcularClasificacion(Time horaEsperada, Time horaMarcada)
	{
	    java.time.LocalTime esperada = horaEsperada.toLocalTime();
	    java.time.LocalTime marcada = horaMarcada.toLocalTime();

	    long minutosEsperada = esperada.getHour() * 60L + esperada.getMinute();
	    long minutosMarcada = marcada.getHour() * 60L + marcada.getMinute();

	    long diferenciaMinutos = minutosMarcada - minutosEsperada;

	    // Si llega antes o dentro de los 10 min de tolerancia, es puntual
	    return (diferenciaMinutos <= 10) ? "PUNTUAL" : "TARDANZA";
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