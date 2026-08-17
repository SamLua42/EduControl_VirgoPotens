package controlador;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
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

	private static final String RUTA_LOGO = "/img/Insignia2.png";

	private static final String[] MESES = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
			"Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

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

		Document documento = new Document(PageSize.A4, 36, 36, 120, 70);

		try {
			PdfWriter writer = PdfWriter.getInstance(documento, response.getOutputStream());
			writer.setPageEvent(new PiePagina());

			documento.open();

			String numeroReporte = generarNumeroReporte("ASIS");
			String periodo = obtenerPeriodoActual();

			agregarEncabezado(documento, numeroReporte, periodo);
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

		Font fuenteSeccion = new Font(Font.HELVETICA, 12, Font.BOLD, AZUL_OSCURO);
		Paragraph seccion = new Paragraph("RESUMEN GENERAL", fuenteSeccion);
		seccion.setSpacingAfter(8);
		documento.add(seccion);

		int total = lista.size();
		int puntuales = 0, tardanzas = 0, faltas = 0;

		for (Asistencia a : lista) {
			if (a.getClasificacion().equals("Puntual")) puntuales++;
			else if (a.getClasificacion().equals("Tardanza")) tardanzas++;
			else if (a.getClasificacion().equals("Falta")) faltas++;
		}

		PdfPTable tarjetas = new PdfPTable(4);
		tarjetas.setWidthPercentage(100);
		tarjetas.setSpacingBefore(4);
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

		Font fuenteIcono = new Font(Font.HELVETICA, 20, Font.BOLD, colorTexto);
		Font fuenteEtiqueta = new Font(Font.HELVETICA, 8, Font.BOLD, Color.DARK_GRAY);
		Font fuenteValor = new Font(Font.HELVETICA, 22, Font.BOLD, colorTexto);

		Paragraph iconoP = new Paragraph("•", fuenteIcono);
		iconoP.setAlignment(Element.ALIGN_CENTER);

		Paragraph etiquetaP = new Paragraph(etiqueta, fuenteEtiqueta);
		etiquetaP.setAlignment(Element.ALIGN_CENTER);
		etiquetaP.setSpacingBefore(2);

		Paragraph valorP = new Paragraph(valor, fuenteValor);
		valorP.setAlignment(Element.ALIGN_CENTER);
		valorP.setSpacingBefore(4);

		celda.addElement(iconoP);
		celda.addElement(etiquetaP);
		celda.addElement(valorP);

		return celda;
	}

	private void agregarTablaDetalle(Document documento, List<Asistencia> lista) throws DocumentException {

		Font fuenteSeccion = new Font(Font.HELVETICA, 12, Font.BOLD, AZUL_OSCURO);
		Paragraph seccion = new Paragraph("DETALLE DE ASISTENCIA", fuenteSeccion);
		seccion.setSpacingAfter(8);
		documento.add(seccion);

		PdfPTable tabla = new PdfPTable(6);
		tabla.setWidthPercentage(100);
		tabla.setWidths(new float[]{0.6f, 2.6f, 1.3f, 1.5f, 1.6f, 1.4f});

		Font fuenteHeader = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
		String[] encabezados = {"N°", "PERSONAL", "FECHA", "HORA MARCADA", "CLASIFICACION", "ESTADO"};

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
			tabla.addCell(celdaSimple(a.getClasificacion().toUpperCase(), fuenteFila, Element.ALIGN_CENTER));

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

		Document documento = new Document(PageSize.A4, 36, 36, 120, 70);

		try {
			PdfWriter writer = PdfWriter.getInstance(documento, response.getOutputStream());
			writer.setPageEvent(new PiePagina());

			documento.open();

			String periodoTexto = obtenerNombreMes(pl.getMes()) + " " + pl.getAnio();
			String numeroReporte = "BOL-" + pl.getAnio() + "-" + String.format("%05d", detalle.getIdDetalle());

			agregarEncabezado(documento, numeroReporte, periodoTexto);
			agregarTitulo(documento, "BOLETA DE PAGO", "Periodo: " + periodoTexto);

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
	private void agregarEncabezado(Document documento, String numeroReporte, String periodo) throws DocumentException {

		PdfPTable tablaEncabezado = new PdfPTable(2);
		tablaEncabezado.setWidthPercentage(100);
		tablaEncabezado.setWidths(new float[]{3, 1.7f});

		// ---- BLOQUE IZQUIERDO: escudo + nombre institucional ----
		PdfPTable bloqueInstitucion = new PdfPTable(2);
		bloqueInstitucion.setWidths(new float[]{1, 4});

		PdfPCell celdaLogo = new PdfPCell();
		celdaLogo.setBorder(Rectangle.NO_BORDER);
		celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
		try {
			String rutaLogo = getServletContext().getRealPath(RUTA_LOGO);
			if (rutaLogo != null) {
				Image logo = Image.getInstance(rutaLogo);
				logo.scaleToFit(58, 58);
				celdaLogo.addElement(logo);
			}
		} catch (Exception e) {
			// si la imagen no existe, el reporte se genera igual sin el logo
		}
		bloqueInstitucion.addCell(celdaLogo);

		PdfPCell celdaTextoInstitucion = new PdfPCell();
		celdaTextoInstitucion.setBorder(Rectangle.NO_BORDER);
		celdaTextoInstitucion.setVerticalAlignment(Element.ALIGN_MIDDLE);

		Font fuenteInstitucion = new Font(Font.HELVETICA, 9, Font.BOLD, AZUL_OSCURO);
		Font fuenteVirgoPotens = new Font(Font.HELVETICA, 19, Font.BOLD, AZUL_OSCURO);
		Font fuenteSistema = new Font(Font.HELVETICA, 12, Font.BOLD, DORADO);
		Font fuenteSubtitulo = new Font(Font.HELVETICA, 8.5f, Font.NORMAL, Color.GRAY);

		celdaTextoInstitucion.addElement(new Paragraph("INSTITUCION EDUCATIVA", fuenteInstitucion));
		celdaTextoInstitucion.addElement(new Paragraph("VIRGO POTENS", fuenteVirgoPotens));
		celdaTextoInstitucion.addElement(new Paragraph("SISTEMA EDUCONTROL", fuenteSistema));
		celdaTextoInstitucion.addElement(new Paragraph("Gestion y Control Institucional", fuenteSubtitulo));
		bloqueInstitucion.addCell(celdaTextoInstitucion);

		PdfPCell celdaIzquierda = new PdfPCell(bloqueInstitucion);
		celdaIzquierda.setBorder(Rectangle.NO_BORDER);
		tablaEncabezado.addCell(celdaIzquierda);

		// ---- BLOQUE DERECHO: numero de reporte + fecha + periodo ----
		PdfPTable bloqueReporte = new PdfPTable(1);
		bloqueReporte.setWidthPercentage(100);

		PdfPCell cajaNumero = new PdfPCell();
		cajaNumero.setBackgroundColor(AZUL_OSCURO);
		cajaNumero.setPadding(8);
		cajaNumero.setBorder(Rectangle.NO_BORDER);
		Font fuenteEtiquetaNum = new Font(Font.HELVETICA, 7.5f, Font.BOLD, new Color(200, 205, 220));
		Font fuenteValorNum = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);
		Paragraph etiquetaNum = new Paragraph("N° REPORTE", fuenteEtiquetaNum);
		etiquetaNum.setAlignment(Element.ALIGN_RIGHT);
		Paragraph valorNum = new Paragraph(numeroReporte, fuenteValorNum);
		valorNum.setAlignment(Element.ALIGN_RIGHT);
		cajaNumero.addElement(etiquetaNum);
		cajaNumero.addElement(valorNum);
		bloqueReporte.addCell(cajaNumero);

		bloqueReporte.addCell(celdaInfoMini("Fecha de generacion:", obtenerFechaGeneracionFormateada()));
		bloqueReporte.addCell(celdaInfoMini("Periodo:", periodo));

		PdfPCell celdaDerecha = new PdfPCell(bloqueReporte);
		celdaDerecha.setBorder(Rectangle.NO_BORDER);
		tablaEncabezado.addCell(celdaDerecha);

		documento.add(tablaEncabezado);
		documento.add(Chunk.NEWLINE);

		LineSeparator linea = new LineSeparator();
		linea.setLineColor(AZUL_OSCURO);
		linea.setLineWidth(2);
		documento.add(new Chunk(linea));
		documento.add(Chunk.NEWLINE);
	}

	private PdfPCell celdaInfoMini(String etiqueta, String valor) {
		PdfPCell celda = new PdfPCell();
		celda.setBackgroundColor(GRIS_CLARO);
		celda.setBorder(Rectangle.NO_BORDER);
		celda.setPadding(6);
		celda.setPaddingTop(4);

		Font fuenteEtiqueta = new Font(Font.HELVETICA, 7.5f, Font.BOLD, Color.DARK_GRAY);
		Font fuenteValor = new Font(Font.HELVETICA, 9, Font.NORMAL, AZUL_OSCURO);

		Paragraph etiquetaP = new Paragraph(etiqueta, fuenteEtiqueta);
		etiquetaP.setAlignment(Element.ALIGN_RIGHT);

		Paragraph valorP = new Paragraph(valor, fuenteValor);
		valorP.setAlignment(Element.ALIGN_RIGHT);
		valorP.setSpacingBefore(1);

		celda.addElement(etiquetaP);
		celda.addElement(valorP);
		return celda;
	}

	private void agregarTitulo(Document documento, String titulo, String subtitulo) throws DocumentException {

		PdfPTable filaTitulo = new PdfPTable(3);
		filaTitulo.setWidthPercentage(75);
		filaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
		filaTitulo.setWidths(new float[]{1, 2.6f, 1});

		PdfPCell lineaIzq = new PdfPCell();
		lineaIzq.setBorder(Rectangle.NO_BORDER);
		lineaIzq.setVerticalAlignment(Element.ALIGN_MIDDLE);
		LineSeparator lIzq = new LineSeparator();
		lIzq.setLineColor(DORADO);
		lIzq.setLineWidth(1.2f);
		lineaIzq.addElement(new Chunk(lIzq));
		filaTitulo.addCell(lineaIzq);

		Font fuenteTitulo = new Font(Font.HELVETICA, 18, Font.BOLD, AZUL_OSCURO);
		PdfPCell celdaTitulo = new PdfPCell(new Phrase(titulo, fuenteTitulo));
		celdaTitulo.setBorder(Rectangle.NO_BORDER);
		celdaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
		filaTitulo.addCell(celdaTitulo);

		PdfPCell lineaDer = new PdfPCell();
		lineaDer.setBorder(Rectangle.NO_BORDER);
		lineaDer.setVerticalAlignment(Element.ALIGN_MIDDLE);
		LineSeparator lDer = new LineSeparator();
		lDer.setLineColor(DORADO);
		lDer.setLineWidth(1.2f);
		lineaDer.addElement(new Chunk(lDer));
		filaTitulo.addCell(lineaDer);

		documento.add(filaTitulo);

		Font fuenteSubtitulo = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.DARK_GRAY);
		Paragraph subtituloP = new Paragraph(subtitulo, fuenteSubtitulo);
		subtituloP.setAlignment(Element.ALIGN_CENTER);
		subtituloP.setSpacingBefore(4);
		subtituloP.setSpacingAfter(15);
		documento.add(subtituloP);
	}

	private PdfPCell celdaSimple(String texto, Font fuente, int alineacion) {
		PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
		celda.setPadding(5);
		celda.setHorizontalAlignment(alineacion);
		return celda;
	}

	//======================================================================
	// HELPERS DE FECHA / NUMERACION
	//======================================================================
	private String obtenerNombreMes(int mes) {
		if (mes < 1 || mes > 12) return "";
		return MESES[mes];
	}

	private String obtenerPeriodoActual() {
		LocalDate hoy = LocalDate.now();
		return obtenerNombreMes(hoy.getMonthValue()) + " " + hoy.getYear();
	}

	private String obtenerFechaGeneracionFormateada() {
		LocalDate hoy = LocalDate.now();
		return hoy.getDayOfMonth() + " de " + obtenerNombreMes(hoy.getMonthValue()).toLowerCase() + " de " + hoy.getYear();
	}

	private String generarNumeroReporte(String prefijo) {
		LocalDate hoy = LocalDate.now();
		int secuencia = hoy.getDayOfYear();
		return prefijo + "-" + hoy.getYear() + "-" + String.format("%05d", secuencia);
	}

	//===================== PIE DE PAGINA =====================
	class PiePagina extends PdfPageEventHelper {
		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			PdfContentByte cb = writer.getDirectContent();

			float izquierda = document.left();
			float derecha = document.right();
			float yLinea = document.bottom() - 8;

			cb.setColorStroke(new Color(220, 220, 225));
			cb.setLineWidth(0.8f);
			cb.moveTo(izquierda, yLinea);
			cb.lineTo(derecha, yLinea);
			cb.stroke();

			Font fuenteNegrita = new Font(Font.HELVETICA, 8, Font.BOLD, AZUL_OSCURO);
			Font fuenteNormal = new Font(Font.HELVETICA, 7.5f, Font.NORMAL, Color.GRAY);

			float yTexto = yLinea - 12;
			float centroX = (izquierda + derecha) / 2;

			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("I.E. VIRGO POTENS", fuenteNegrita), izquierda, yTexto, 0);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("Jr. Puno 1731 - Lima", fuenteNormal), izquierda, yTexto - 10, 0);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("UGEL 03 - Lima Metropolitana", fuenteNormal), izquierda, yTexto - 20, 0);

			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase("EduControl", fuenteNegrita), centroX, yTexto, 0);
			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase("Sistema de Gestion Institucional", fuenteNormal), centroX, yTexto - 10, 0);
			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase("Reporte generado automaticamente", fuenteNormal), centroX, yTexto - 20, 0);

			String textoPagina = "Pagina " + writer.getPageNumber();
			Font fuentePagina = new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE);
			BaseFont bf = fuentePagina.getCalculatedBaseFont(false);
			float anchoTexto = bf.getWidthPoint(textoPagina, 8);
			float anchoPildora = anchoTexto + 16;
			float altoPildora = 16;
			float xPildora = derecha - anchoPildora;
			float yPildora = yTexto - 14;

			cb.setColorFill(AZUL_OSCURO);
			cb.roundRectangle(xPildora, yPildora, anchoPildora, altoPildora, 8);
			cb.fill();

			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(textoPagina, fuentePagina),
					xPildora + anchoPildora / 2, yPildora + 5, 0);
		}
	}
}