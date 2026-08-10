package controlador;

import java.io.IOException;
import java.math.BigDecimal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ConceptoPagoDAO;
import entidad.ConceptoPago;


@WebServlet("/ConceptoPagoServlet")



public class ConceptoPagoServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final String VISTA = "pagos/MantConceptoPago.jsp";
	private static final String LISTAR = "listar";
	private static final String REGISTRAR = "registrar";
	private static final String ACTUALIZAR = "actualizar";
	private static final String BUSCAR = "buscar";
	private static final String ELIMINAR = "eliminar";



	private ConceptoPagoDAO conceptoPagoDAO;

	
	
	public ConceptoPagoServlet() {conceptoPagoDAO = new ConceptoPagoDAO();}

	
	
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
		
		catch (Exception e)
		{
			request.setAttribute("mensaje", "Error: " + e.getMessage());
			listar(request, response);
		}
	}

	
	
	private void cargarDatos(HttpServletRequest request)
	{
		request.setAttribute("listaConceptos", conceptoPagoDAO.listar());
	}

	
	
	private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		cargarDatos(request);
		request.getRequestDispatcher(VISTA).forward(request, response);
	}

	
	
	private void buscar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int id = Integer.parseInt(request.getParameter("id"));
		ConceptoPago c = conceptoPagoDAO.buscarPorId(id);

		cargarDatos(request);
		request.setAttribute("concepto", c);
		request.setAttribute("editar", true);
		request.getRequestDispatcher(VISTA).forward(request, response);
	}

	
	
	private void registrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		ConceptoPago c = obtenerDatosFormulario(request);
		int resultado = conceptoPagoDAO.insertar(c);

		if (resultado > 0)
		{
			mensaje(request, "Concepto de pago registrado correctamente.");
		}
		
		else
		{
			mensaje(request, "No se pudo registrar el concepto de pago.");
		}
		
		listar(request, response);
	}

	
	
	private void actualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		ConceptoPago c = obtenerDatosFormulario(request);
		int resultado = conceptoPagoDAO.actualizar(c);

		if (resultado > 0)
		{
			mensaje(request, "Concepto de pago actualizado correctamente.");
		}
		
		else
		{
			mensaje(request, "No se pudo actualizar el concepto de pago.");
		}
		
		listar(request, response);
	}

	
	
	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int id = Integer.parseInt(request.getParameter("id"));
		int resultado = conceptoPagoDAO.eliminar(id);

		if (resultado > 0)
		{
			mensaje(request, "Concepto de pago eliminado correctamente.");
		}
		
		else {mensaje(request, "No se pudo eliminar el concepto de pago.");}
		
		listar(request, response);
	}

	
	
	private ConceptoPago obtenerDatosFormulario(HttpServletRequest request)
	{
		ConceptoPago c = new ConceptoPago();

		String idConcepto = request.getParameter("idConcepto");
		if (idConcepto != null && !idConcepto.trim().isEmpty())
		{
			c.setIdConcepto(Integer.parseInt(idConcepto));
		}

		c.setTipoPersonal(request.getParameter("tipoPersonal"));
		c.setTarifaDiaria(new BigDecimal(request.getParameter("tarifaDiaria")));
		c.setDescuentoTardanza(new BigDecimal(request.getParameter("descuentoTardanza")));
		c.setDescuentoFalta(new BigDecimal(request.getParameter("descuentoFalta")));

		return c;
	}

	
	
	private void mensaje(HttpServletRequest request, String texto)
	{
		request.setAttribute("mensaje", texto);
	}
	
}
