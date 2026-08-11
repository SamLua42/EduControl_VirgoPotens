package util;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PruebaPDF {

    public static void main(String[] args) {
        try {
            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream("C:/Users/BEA19/Desktop/reportePrueba.pdf"));

            documento.open();

            documento.add(new Paragraph("Reporte de Asistencia - EduControl"));
            documento.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(3);

            tabla.addCell(new PdfPCell(new Paragraph("Nombre")));
            tabla.addCell(new PdfPCell(new Paragraph("Fecha")));
            tabla.addCell(new PdfPCell(new Paragraph("Clasificacion")));

            tabla.addCell("Juana Perez");
            tabla.addCell("2026-08-10");
            tabla.addCell("Puntual");

            tabla.addCell("Carlos Ramos");
            tabla.addCell("2026-08-10");
            tabla.addCell("Tardanza");

            documento.add(tabla);

            documento.close();

            System.out.println("PDF generado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}