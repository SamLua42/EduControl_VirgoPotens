package controlador;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.AsistenciaDAO;
import dao.ConceptoPagoDAO;
import dao.DetallePlanillaDAO;
import dao.PersonalDAO;
import dao.PlanillaDAO;
import entidad.Asistencia;
import entidad.ConceptoPago;
import entidad.DetallePlanilla;
import entidad.Personal;
import entidad.Planilla;


@WebServlet("/PlanillaServlet")



public class PlanillaServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final String VISTA = "pagos/MantPlanilla.jsp";
	private static final String LISTAR = "listar";
	private static final String CALCULAR = "calcular";
	private static final String PROCESAR = "procesar";
	private static final String BUSCAR = "buscar";


	private PlanillaDAO planillaDAO;
	private DetallePlanillaDAO detalleDAO;
	private PersonalDAO personalDAO;
	private AsistenciaDAO asistenciaDAO;
	private ConceptoPagoDAO conceptoPagoDAO;


	
	public PlanillaServlet()
	{
		planillaDAO = new PlanillaDAO();
		detalleDAO = new DetallePlanillaDAO();
		personalDAO = new PersonalDAO();
		asistenciaDAO = new AsistenciaDAO();
		conceptoPagoDAO = new ConceptoPagoDAO();
	}

	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		procesarPeticion(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		procesarPeticion(request, response);
	}

	
	
	private void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
			case CALCULAR:
				calcular(request, response);
				break;
			case PROCESAR:
				procesarPlanilla(request, response);
				break;
			case BUSCAR:
				buscar(request, response);
				break;
			default:
				listar(request, response);
			}
		}
		
		catch (Exception e){request.setAttribute("mensaje", "Error: " + e.getMessage());
							listar(request, response);}
	}

	
	
	private void cargarDatos(HttpServletRequest request)
	{
		request.setAttribute("listaPlanillas", planillaDAO.listar());
	}

	
	
	private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		cargarDatos(request);
		request.getRequestDispatcher(VISTA).forward(request, response);
	}

	
	
	private void buscar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int id = Integer.parseInt(request.getParameter("id"));
		Planilla pl = planillaDAO.buscarPorId(id);
		List<DetallePlanilla> detalles = detalleDAO.listar();

		cargarDatos(request);
		request.setAttribute("planilla", pl);
		request.setAttribute("listaDetalles", detalles);
		request.getRequestDispatcher(VISTA).forward(request, response);
	}



	private void calcular(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int mes = Integer.parseInt(request.getParameter("mes"));
		int anio = Integer.parseInt(request.getParameter("anio"));

		Planilla nuevaPlanilla = new Planilla();
		nuevaPlanilla.setMes(mes);
		nuevaPlanilla.setAnio(anio);
		nuevaPlanilla.setEstado("Preliminar");

		int resultado = planillaDAO.insertar(nuevaPlanilla);

		if (resultado > 0)
		{
			
			List<Planilla> todas = planillaDAO.listar();
			Planilla planillaCreada = todas.get(todas.size() - 1);

			generarDetallePorTrabajador(planillaCreada, mes, anio);
			mensaje(request, "Planilla preliminar calculada correctamente.");
		}
		
		else
		{
			mensaje(request, "No se pudo calcular la planilla.");
		}

		listar(request, response);
	}


	
	private void generarDetallePorTrabajador(Planilla planilla, int mes, int anio)
	{
		List<Personal> listaPersonal = personalDAO.listar();
		List<Asistencia> listaAsistencia = asistenciaDAO.listar();

		for (Personal p : listaPersonal)
		{

			int diasTrabajados = 0;
			int diasTardanza = 0;
			int diasFalta = 0;

			for (Asistencia a : listaAsistencia)
			{
				if (a.getIdPersonal() == p.getIdPersonal()
				        && a.getFecha().toLocalDate().getMonthValue() == mes
				        && a.getFecha().toLocalDate().getYear() == anio)
				{
					if (a.getClasificacion().equals("Puntual"))
					{
						diasTrabajados++;
					}
					
					else if (a.getClasificacion().equals("Tardanza"))
					{
						diasTrabajados++;
						diasTardanza++;
					}
					
					else if (a.getClasificacion().equals("Falta"))
					{
						diasFalta++;
					}
				}
			}

			ConceptoPago concepto = buscarConceptoPorTipo(p.getTipoPersonal());

			if (concepto != null)
			{
				BigDecimal montoBase = concepto.getTarifaDiaria().multiply(new BigDecimal(diasTrabajados));
				BigDecimal descuentoTardanza = concepto.getDescuentoTardanza().multiply(new BigDecimal(diasTardanza));
				BigDecimal descuentoFalta = concepto.getDescuentoFalta().multiply(new BigDecimal(diasFalta));
				BigDecimal totalDescuentos = descuentoTardanza.add(descuentoFalta);
				BigDecimal montoTotal = montoBase.subtract(totalDescuentos);

				DetallePlanilla detalle = new DetallePlanilla();
				detalle.setIdPlanilla(planilla.getIdPlanilla());
				detalle.setIdPersonal(p.getIdPersonal());
				detalle.setDiasTrabajados(diasTrabajados);
				detalle.setDiasTardanza(diasTardanza);
				detalle.setDiasFalta(diasFalta);
				detalle.setMontoDescuento(totalDescuentos);
				detalle.setMontoTotal(montoTotal);

				detalleDAO.insertar(detalle);
			}
		}
	}

	
	
	private ConceptoPago buscarConceptoPorTipo(String tipoPersonal)
	{
		List<ConceptoPago> lista = conceptoPagoDAO.listar();
		for (ConceptoPago c : lista) {
			if (c.getTipoPersonal().equals(tipoPersonal))
			{
				return c;
			}
		}
		
		return null;
	}


	
	private void procesarPlanilla(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int idPlanilla = Integer.parseInt(request.getParameter("id"));

		Planilla pl = planillaDAO.buscarPorId(idPlanilla);
		pl.setEstado("Procesada");
		pl.setFechaProcesado(new java.sql.Timestamp(System.currentTimeMillis()));

		int resultado = planillaDAO.actualizar(pl);

		if (resultado > 0)
		{
			mensaje(request, "Planilla procesada correctamente.");
		}
		
		else
		{
			mensaje(request, "No se pudo procesar la planilla.");
		}

		listar(request, response);
	}

	
	
	private void mensaje(HttpServletRequest request, String texto)
	{
		request.setAttribute("mensaje", texto);
	}
}