package controlador;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import java.awt.Color;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.AsistenciaDAO;
import dao.DetallePlanillaDAO;
import dao.PersonalDAO;
import dao.PlanillaDAO;
import entidad.Asistencia;
import entidad.DetallePlanilla;
import entidad.Personal;
import entidad.Planilla;

@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//COLORES INSTITUCIONALES
	private static final Color AZUL_OSCURO = new Color(30, 27, 75);
	private static final Color DORADO = new Color(212, 160, 40);
	private static final Color VERDE = new Color(34, 139, 60);
	private static final Color NARANJA = new Color(230, 150, 30);
	private static final Color ROJO = new Color(200, 40, 40);
	private static final Color GRIS_CLARO = new Color(240, 240, 245);

	//declarando constantes de accion
	private static final String REPORTE_ASISTENCIA = "reporteAsistencia";
	private static final String REPORTE_BOLETA = "reporteBoleta";

	//DECLARAR OBJETOS dao
	private AsistenciaDAO asistenciaDAO;
	private PersonalDAO personalDAO;
	private DetallePlanillaDAO detalleDAO;
	private PlanillaDAO planillaDAO;

	//constructor sin parametros para crear los objetos dao
	public ReporteServlet() {
		asistenciaDAO = new AsistenciaDAO();
		personalDAO = new PersonalDAO();
		detalleDAO = new DetallePlanillaDAO();
		planillaDAO = new PlanillaDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesar(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesar(request, response);
	}

	private void procesar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operacion = request.getParameter("accion");
		if (operacion == null || operacion.trim().isEmpty()) {
			operacion = REPORTE_ASISTENCIA;
		}

		try {
			switch (operacion) {
			case REPORTE_ASISTENCIA:
				generarReporteAsistenciaPDF(request, response);
				break;
			case REPORTE_BOLETA:
				generarBoletaPagoPDF(request, response);
				break;
			default:
				generarReporteAsistenciaPDF(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//======================================================================
	// CU06 - REPORTE DE ASISTENCIA
	//======================================================================
	private void generarReporteAsistenciaPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=ReporteAsistencia.pdf");

		Document documento = new Document(PageSize.A4, 36, 36, 120, 60);

		try {
			PdfWriter writer = PdfWriter.getInstance(documento, response.getOutputStream());
			writer.setPageEvent(new PiePagina());

			documento.open();

			agregarEncabezado(documento);
			agregarTitulo(documento, "REPORTE DE ASISTENCIA", "Registro de asistencia del personal");

			List<Asistencia> lista = asistenciaDAO.listar();
			agregarTarjetasResumen(documento, lista);
			agregarTablaDetalle(documento, lista);

			documento.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private void agregarTarjetasResumen(Document documento, List<Asistencia> lista) throws DocumentException {

		int total = lista.size();
		int puntuales = 0, tardanzas = 0, faltas = 0;

		for (Asistencia a : lista) {
			if (a.getClasificacion().equals("Puntual")) puntuales++;
			else if (a.getClasificacion().equals("Tardanza")) tardanzas++;
			else if (a.getClasificacion().equals("Falta")) faltas++;
		}

		PdfPTable tarjetas = new PdfPTable(4);
		tarjetas.setWidthPercentage(100);
		tarjetas.setSpacingBefore(10);
		tarjetas.setSpacingAfter(15);

		tarjetas.addCell(crearTarjeta("TOTAL DE REGISTROS", String.valueOf(total), GRIS_CLARO, AZUL_OSCURO));
		tarjetas.addCell(crearTarjeta("PUNTUALES", String.valueOf(puntuales), new Color(230,245,235), VERDE));
		tarjetas.addCell(crearTarjeta("TARDANZAS", String.valueOf(tardanzas), new Color(255,245,225), NARANJA));
		tarjetas.addCell(crearTarjeta("FALTAS", String.valueOf(faltas), new Color(255,230,230), ROJO));

		documento.add(tarjetas);
	}

	private PdfPCell crearTarjeta(String etiqueta, String valor, Color fondo, Color colorTexto) {
		PdfPCell celda = new PdfPCell();
		celda.setBackgroundColor(fondo);
		celda.setPadding(12);
		celda.setBorderColor(Color.LIGHT_GRAY);

		Font fuenteEtiqueta = new Font(Font.HELVETICA, 8, Font.BOLD, Color.DARK_GRAY);
		Font fuenteValor = new Font(Font.HELVETICA, 22, Font.BOLD, colorTexto);

		Paragraph etiquetaP = new Paragraph(etiqueta, fuenteEtiqueta);
		etiquetaP.setAlignment(Element.ALIGN_CENTER);

		Paragraph valorP = new Paragraph(valor, fuenteValor);
		valorP.setAlignment(Element.ALIGN_CENTER);
		valorP.setSpacingBefore(4);

		celda.addElement(etiquetaP);
		celda.addElement(valorP);

		return celda;
	}

	private void agregarTablaDetalle(Document documento, List<Asistencia> lista) throws DocumentException {

		Font fuenteSeccion = new Font(Font.HELVETICA, 12, Font.BOLD, AZUL_OSCURO);
		Paragraph seccion = new Paragraph("DETALLE DE ASISTENCIA", fuenteSeccion);
		seccion.setSpacingAfter(8);
		documento.add(seccion);

		PdfPTable tabla = new PdfPTable(5);
		tabla.setWidthPercentage(100);
		tabla.setWidths(new float[]{1, 3, 2, 2, 2});

		Font fuenteHeader = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
		String[] encabezados = {"N°", "PERSONAL", "FECHA", "HORA MARCADA", "ESTADO"};

		for (String texto : encabezados) {
			PdfPCell celda = new PdfPCell(new Phrase(texto, fuenteHeader));
			celda.setBackgroundColor(AZUL_OSCURO);
			celda.setPadding(6);
			celda.setHorizontalAlignment(Element.ALIGN_CENTER);
			tabla.addCell(celda);
		}

		Font fuenteFila = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
		int contador = 1;

		for (Asistencia a : lista) {
			Personal p = personalDAO.buscarPorId(a.getIdPersonal());
			String nombreCompleto = (p != null) ? p.getNombre() + " " + p.getApellido() : "N/D";

			tabla.addCell(celdaSimple(String.valueOf(contador++), fuenteFila, Element.ALIGN_CENTER));
			tabla.addCell(celdaSimple(nombreCompleto, fuenteFila, Element.ALIGN_LEFT));
			tabla.addCell(celdaSimple(a.getFecha().toString(), fuenteFila, Element.ALIGN_CENTER));
			tabla.addCell(celdaSimple(a.getHoraMarcada() != null ? a.getHoraMarcada().toString() : "-", fuenteFila, Element.ALIGN_CENTER));

			Color colorEstado = a.getClasificacion().equals("Puntual") ? VERDE
					: a.getClasificacion().equals("Tardanza") ? NARANJA : ROJO;
			Font fuenteEstado = new Font(Font.HELVETICA, 9, Font.BOLD, colorEstado);
			PdfPCell celdaEstado = new PdfPCell(new Phrase(a.getClasificacion(), fuenteEstado));
			celdaEstado.setHorizontalAlignment(Element.ALIGN_CENTER);
			celdaEstado.setPadding(5);
			tabla.addCell(celdaEstado);
		}

		documento.add(tabla);
	}

	//======================================================================
	// CU07 - BOLETA DE PAGO
	//======================================================================
	private void generarBoletaPagoPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {

		int idDetalle = Integer.parseInt(request.getParameter("idDetalle"));

		DetallePlanilla detalle = buscarDetallePorId(idDetalle);
		if (detalle == null) {
			return;
		}

		Personal p = personalDAO.buscarPorId(detalle.getIdPersonal());
		Planilla pl = planillaDAO.buscarPorId(detalle.getIdPlanilla());

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=BoletaPago.pdf");

		Document documento = new Document(PageSize.A4, 36, 36, 120, 60);

		try {
			PdfWriter writer = PdfWriter.getInstance(documento, response.getOutputStream());
			writer.setPageEvent(new PiePagina());

			documento.open();

			agregarEncabezado(documento);

			String periodo = pl.getMes() + "/" + pl.getAnio();
			agregarTitulo(documento, "BOLETA DE PAGO", "Periodo: " + periodo);

			agregarDatosTrabajador(documento, p);
			agregarDesgloseDias(documento, detalle);
			agregarTotales(documento, detalle, p);

			documento.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private DetallePlanilla buscarDetallePorId(int idDetalle) {
		List<DetallePlanilla> lista = detalleDAO.listar();
		for (DetallePlanilla d : lista) {
			if (d.getIdDetalle() == idDetalle) {
				return d;
			}
		}
		return null;
	}

	private void agregarDatosTrabajador(Document documento, Personal p) throws DocumentException {
		Font fuenteEtiqueta = new Font(Font.HELVETICA, 9, Font.BOLD, Color.DARK_GRAY);
		Font fuenteValor = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);

		PdfPTable tabla = new PdfPTable(2);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(10);
		tabla.setSpacingAfter(15);

		tabla.addCell(celdaDato("Nombre completo:", p.getNombre() + " " + p.getApellido(), fuenteEtiqueta, fuenteValor));
		tabla.addCell(celdaDato("DNI:", p.getDni(), fuenteEtiqueta, fuenteValor));
		tabla.addCell(celdaDato("Cargo:", p.getCargo(), fuenteEtiqueta, fuenteValor));
		tabla.addCell(celdaDato("Tipo de personal:", p.getTipoPersonal(), fuenteEtiqueta, fuenteValor));

		documento.add(tabla);
	}

	private PdfPCell celdaDato(String etiqueta, String valor, Font fuenteEtiqueta, Font fuenteValor) {
		PdfPCell celda = new PdfPCell();
		celda.setBorder(Rectangle.NO_BORDER);
		celda.setPadding(4);
		Paragraph p = new Paragraph();
		p.add(new Chunk(etiqueta + " ", fuenteEtiqueta));
		p.add(new Chunk(valor, fuenteValor));
		celda.addElement(p);
		return celda;
	}

	private void agregarDesgloseDias(Document documento, DetallePlanilla detalle) throws DocumentException {
		Font fuenteSeccion = new Font(Font.HELVETICA, 12, Font.BOLD, AZUL_OSCURO);
		Paragraph seccion = new Paragraph("DETALLE DE ASISTENCIA DEL PERIODO", fuenteSeccion);
		seccion.setSpacingAfter(8);
		documento.add(seccion);

		PdfPTable tabla = new PdfPTable(3);
		tabla.setWidthPercentage(100);
		tabla.setSpacingAfter(15);

		Font fuenteHeader = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
		String[] encabezados = {"DIAS TRABAJADOS", "DIAS TARDANZA", "DIAS FALTA"};

		for (String texto : encabezados) {
			PdfPCell celda = new PdfPCell(new Phrase(texto, fuenteHeader));
			celda.setBackgroundColor(AZUL_OSCURO);
			celda.setPadding(6);
			celda.setHorizontalAlignment(Element.ALIGN_CENTER);
			tabla.addCell(celda);
		}

		Font fuenteValor = new Font(Font.HELVETICA, 14, Font.BOLD, AZUL_OSCURO);
		tabla.addCell(celdaCentrada(String.valueOf(detalle.getDiasTrabajados()), fuenteValor));
		tabla.addCell(celdaCentrada(String.valueOf(detalle.getDiasTardanza()), fuenteValor));
		tabla.addCell(celdaCentrada(String.valueOf(detalle.getDiasFalta()), fuenteValor));

		documento.add(tabla);
	}

	private PdfPCell celdaCentrada(String texto, Font fuente) {
		PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
		celda.setPadding(10);
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		return celda;
	}

	private void agregarTotales(Document documento, DetallePlanilla detalle, Personal p) throws DocumentException {
		Font fuenteEtiqueta = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.DARK_GRAY);
		Font fuenteMonto = new Font(Font.HELVETICA, 11, Font.BOLD, Color.BLACK);
		Font fuenteInformativoEtiqueta = new Font(Font.HELVETICA, 9, Font.ITALIC, Color.GRAY);
		Font fuenteInformativoMonto = new Font(Font.HELVETICA, 9, Font.ITALIC, Color.GRAY);
		Font fuenteTotalEtiqueta = new Font(Font.HELVETICA, 13, Font.BOLD, Color.WHITE);
		Font fuenteTotalValor = new Font(Font.HELVETICA, 16, Font.BOLD, Color.WHITE);

		PdfPTable tabla = new PdfPTable(2);
		tabla.setWidthPercentage(60);
		tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla.setSpacingBefore(10);

		BigDecimal bruto = detalle.getMontoBruto();
		BigDecimal descuentoAsistencia = detalle.getMontoDescuento();
		BigDecimal descuentoPension = detalle.getMontoDescuentoPension();
		BigDecimal essalud = detalle.getMontoEssalud();
		BigDecimal neto = detalle.getMontoNeto();

		boolean esAfp = p != null && "AFP".equalsIgnoreCase(p.getSistemaPension());
		String etiquetaPension = esAfp ? "Descuento AFP (11.37%):" : "Descuento ONP (13%):";

		agregarFilaTotal(tabla, "Monto Bruto:", "S/ " + bruto.toString(), fuenteEtiqueta, fuenteMonto);
		agregarFilaTotal(tabla, "Descuento por Tardanza/Falta:", "S/ " + descuentoAsistencia.toString(), fuenteEtiqueta, fuenteMonto);
		agregarFilaTotal(tabla, etiquetaPension, "S/ " + descuentoPension.toString(), fuenteEtiqueta, fuenteMonto);
		agregarFilaTotal(tabla, "EsSalud (aporte del colegio, informativo):", "S/ " + essalud.toString(), fuenteInformativoEtiqueta, fuenteInformativoMonto);

		PdfPCell etiquetaTotal = new PdfPCell(new Phrase("NETO A PAGAR", fuenteTotalEtiqueta));
		etiquetaTotal.setBackgroundColor(AZUL_OSCURO);
		etiquetaTotal.setPadding(10);
		tabla.addCell(etiquetaTotal);

		PdfPCell valorTotal = new PdfPCell(new Phrase("S/ " + neto.toString(), fuenteTotalValor));
		valorTotal.setBackgroundColor(AZUL_OSCURO);
		valorTotal.setPadding(10);
		valorTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla.addCell(valorTotal);

		documento.add(tabla);
	}

	private void agregarFilaTotal(PdfPTable tabla, String etiqueta, String valor, Font fuenteEtiqueta, Font fuenteValor) {
		PdfPCell celdaEtiqueta = new PdfPCell(new Phrase(etiqueta, fuenteEtiqueta));
		celdaEtiqueta.setBorder(Rectangle.NO_BORDER);
		celdaEtiqueta.setPadding(6);
		tabla.addCell(celdaEtiqueta);

		PdfPCell celdaValor = new PdfPCell(new Phrase(valor, fuenteValor));
		celdaValor.setBorder(Rectangle.NO_BORDER);
		celdaValor.setPadding(6);
		celdaValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla.addCell(celdaValor);
	}

	//======================================================================
	// COMPONENTES COMPARTIDOS (usados por ambos reportes)
	//======================================================================
	private void agregarEncabezado(Document documento) throws DocumentException {
		PdfPTable tablaEncabezado = new PdfPTable(2);
		tablaEncabezado.setWidthPercentage(100);
		tablaEncabezado.setWidths(new float[]{3, 1});

		PdfPCell celdaInstitucion = new PdfPCell();
		celdaInstitucion.setBorder(Rectangle.NO_BORDER);

		Font fuenteInstitucion = new Font(Font.HELVETICA, 10, Font.BOLD, AZUL_OSCURO);
		Font fuenteVirgoPotens = new Font(Font.HELVETICA, 20, Font.BOLD, AZUL_OSCURO);
		Font fuenteSistema = new Font(Font.HELVETICA, 13, Font.BOLD, DORADO);
		Font fuenteSubtitulo = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.GRAY);

		celdaInstitucion.addElement(new Paragraph("INSTITUCION EDUCATIVA", fuenteInstitucion));
		celdaInstitucion.addElement(new Paragraph("VIRGO POTENS", fuenteVirgoPotens));
		celdaInstitucion.addElement(new Paragraph("SISTEMA EDUCONTROL", fuenteSistema));
		celdaInstitucion.addElement(new Paragraph("Gestion y Control Institucional", fuenteSubtitulo));

		tablaEncabezado.addCell(celdaInstitucion);

		PdfPCell celdaReporte = new PdfPCell();
		celdaReporte.setBorder(Rectangle.NO_BORDER);
		celdaReporte.setHorizontalAlignment(Element.ALIGN_RIGHT);

		PdfPTable cajaReporte = new PdfPTable(1);
		cajaReporte.setWidthPercentage(100);

		PdfPCell caja = new PdfPCell();
		caja.setBackgroundColor(AZUL_OSCURO);
		caja.setPadding(8);
		Font fuenteBlanca = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
		Paragraph numeroReporte = new Paragraph("SISTEMA EDUCONTROL", fuenteBlanca);
		numeroReporte.setAlignment(Element.ALIGN_CENTER);
		caja.addElement(numeroReporte);
		cajaReporte.addCell(caja);

		celdaReporte.addElement(cajaReporte);
		tablaEncabezado.addCell(celdaReporte);

		documento.add(tablaEncabezado);

		LineSeparator linea = new LineSeparator();
		linea.setLineColor(AZUL_OSCURO);
		linea.setLineWidth(2);
		documento.add(new Chunk(linea));
		documento.add(Chunk.NEWLINE);
	}

	private void agregarTitulo(Document documento, String titulo, String subtitulo) throws DocumentException {
		Font fuenteTitulo = new Font(Font.HELVETICA, 18, Font.BOLD, AZUL_OSCURO);
		Font fuenteSubtitulo = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.DARK_GRAY);

		Paragraph tituloP = new Paragraph(titulo, fuenteTitulo);
		tituloP.setAlignment(Element.ALIGN_CENTER);
		documento.add(tituloP);

		Paragraph subtituloP = new Paragraph(subtitulo, fuenteSubtitulo);
		subtituloP.setAlignment(Element.ALIGN_CENTER);
		subtituloP.setSpacingAfter(15);
		documento.add(subtituloP);
	}

	private PdfPCell celdaSimple(String texto, Font fuente, int alineacion) {
		PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
		celda.setPadding(5);
		celda.setHorizontalAlignment(alineacion);
		return celda;
	}

	//===================== PIE DE PAGINA =====================
	class PiePagina extends PdfPageEventHelper {
		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			PdfContentByte cb = writer.getDirectContent();
			Font fuentePie = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.GRAY);

			Phrase pie = new Phrase("I.E. VIRGO POTENS  |  Pagina " + writer.getPageNumber(), fuentePie);
			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, pie,
					(document.right() - document.left()) / 2 + document.leftMargin(),
					document.bottom() - 20, 0);
		}
	}
}