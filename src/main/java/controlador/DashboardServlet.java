package controlador;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet
{
       private static final long serialVersionUID = 1L;

       private static final String VISTA = "dashboard/Dashboard.jsp";

       private static final String[] MESES_ABREV = {"ENE","FEB","MAR","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};

       private PersonalDAO personalDAO;
       private AsistenciaDAO asistenciaDAO;
       private PlanillaDAO planillaDAO;
       private DetallePlanillaDAO detalleDAO;

       public DashboardServlet()
       {
             personalDAO = new PersonalDAO();
             asistenciaDAO = new AsistenciaDAO();
             planillaDAO = new PlanillaDAO();
             detalleDAO = new DetallePlanillaDAO();
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
             try
             {
                    LocalDate hoy = LocalDate.now();
                    List<Personal> listaPersonal = personalDAO.listar();
                    List<Asistencia> listaAsistencia = asistenciaDAO.listar();

                    // ===== KPIs de HOY =====
                    int presentesHoy = 0, tardanzasHoy = 0, faltasHoy = 0;
                    for (Asistencia a : listaAsistencia)
                    {
                           if (a.getFecha().toLocalDate().equals(hoy))
                           {
                                  String clas = a.getClasificacion();
                                  if (clas.equalsIgnoreCase("Puntual")) { presentesHoy++; }
                                  else if (clas.equalsIgnoreCase("Tardanza")) { presentesHoy++; tardanzasHoy++; }
                                  else if (clas.equalsIgnoreCase("Falta")) { faltasHoy++; }
                           }
                    }

                    request.setAttribute("totalPersonal", listaPersonal.size());
                    request.setAttribute("presentesHoy", presentesHoy);
                    request.setAttribute("tardanzasHoy", tardanzasHoy);
                    request.setAttribute("faltasHoy", faltasHoy);

                    // ===== ASISTENCIA DE LA SEMANA (5 dias habiles: lunes a viernes) =====
                    LocalDate lunes = hoy.with(DayOfWeek.MONDAY);
                    List<DiaResumen> asistenciaSemana = new ArrayList<>();
                    int maxConteo = 1;

                    int[][] conteosPorDia = new int[5][3]; // [dia][0=presente,1=tardanza,2=falta]
                    String[] etiquetas = {"Lun", "Mar", "Mie", "Jue", "Vie"};

                    for (int i = 0; i < 5; i++)
                    {
                           LocalDate diaActual = lunes.plusDays(i);
                           for (Asistencia a : listaAsistencia)
                           {
                                  if (a.getFecha().toLocalDate().equals(diaActual))
                                  {
                                        String clas = a.getClasificacion();
                                        if (clas.equalsIgnoreCase("Puntual")) { conteosPorDia[i][0]++; }
                                        else if (clas.equalsIgnoreCase("Tardanza")) { conteosPorDia[i][1]++; }
                                        else if (clas.equalsIgnoreCase("Falta")) { conteosPorDia[i][2]++; }
                                  }
                           }
                           int totalDia = conteosPorDia[i][0] + conteosPorDia[i][1] + conteosPorDia[i][2];
                           if (totalDia > maxConteo) { maxConteo = totalDia; }
                    }

                    for (int i = 0; i < 5; i++)
                    {
                           DiaResumen dr = new DiaResumen();
                           dr.setEtiqueta(etiquetas[i] + " " + lunes.plusDays(i).getDayOfMonth() + "/" + lunes.plusDays(i).getMonthValue());
                           dr.setPorcentajePresentes((conteosPorDia[i][0] * 100) / maxConteo);
                           dr.setPorcentajeTardanzas((conteosPorDia[i][1] * 100) / maxConteo);
                           dr.setPorcentajeFaltas((conteosPorDia[i][2] * 100) / maxConteo);
                           asistenciaSemana.add(dr);
                    }
                    request.setAttribute("asistenciaSemana", asistenciaSemana);

                    // ===== RESUMEN DEL MES (porcentajes) =====
                    int mesActual = hoy.getMonthValue();
                    int anioActual = hoy.getYear();

                    int totalMesPresente = 0, totalMesTardanza = 0, totalMesFalta = 0;
                    for (Asistencia a : listaAsistencia)
                    {
                           LocalDate f = a.getFecha().toLocalDate();
                           if (f.getMonthValue() == mesActual && f.getYear() == anioActual)
                           {
                                  String clas = a.getClasificacion();
                                  if (clas.equalsIgnoreCase("Puntual")) { totalMesPresente++; }
                                  else if (clas.equalsIgnoreCase("Tardanza")) { totalMesTardanza++; }
                                  else if (clas.equalsIgnoreCase("Falta")) { totalMesFalta++; }
                           }
                    }
                    int totalMes = totalMesPresente + totalMesTardanza + totalMesFalta;
                    if (totalMes == 0) { totalMes = 1; } // evitar division por cero

                    request.setAttribute("pctPresentes", (totalMesPresente * 100) / totalMes);
                    request.setAttribute("pctTardanzas", (totalMesTardanza * 100) / totalMes);
                    request.setAttribute("pctFaltas", (totalMesFalta * 100) / totalMes);

                    request.setAttribute("mesActual", mesActual);
                    request.setAttribute("anioActual", anioActual);

                    // ===== FECHAS PARA "PROXIMOS PAGOS" =====
                    int diaFinMes = LocalDate.of(anioActual, mesActual, 1).lengthOfMonth();
                    String nombreMesActualAbrev = MESES_ABREV[mesActual - 1];

                    int mesSiguiente = (mesActual == 12) ? 1 : mesActual + 1;
                    int anioSiguiente = (mesActual == 12) ? anioActual + 1 : anioActual;
                    String nombreMesSiguienteAbrev = MESES_ABREV[mesSiguiente - 1];

                    request.setAttribute("diaFinMes", diaFinMes);
                    request.setAttribute("nombreMesActualAbrev", nombreMesActualAbrev);
                    request.setAttribute("mesSiguiente", mesSiguiente);
                    request.setAttribute("anioSiguiente", anioSiguiente);
                    request.setAttribute("nombreMesSiguienteAbrev", nombreMesSiguienteAbrev);

                    // ===== PLANILLA DEL MES ACTUAL =====
                    Planilla planillaMes = null;
                    for (Planilla pl : planillaDAO.listar())
                    {
                           if (pl.getMes() == mesActual && pl.getAnio() == anioActual)
                           {
                                  planillaMes = pl;
                                  break;
                           }
                    }

                    BigDecimal totalPagosMes = BigDecimal.ZERO;
                    String estadoPlanillaActual = "PRELIMINAR";

                    int pagosCompletos = 0, pagosConDescuento = 0, pagosPendientes = 0;
                    Set<Integer> personalConDetalle = new HashSet<>();

                    if (planillaMes != null)
                    {
                           estadoPlanillaActual = planillaMes.getEstado().toUpperCase();
                           for (DetallePlanilla d : detalleDAO.listar())
                           {
                                  if (d.getIdPlanilla() == planillaMes.getIdPlanilla())
                                 {
                                        totalPagosMes = totalPagosMes.add(d.getMontoTotal());
                                        personalConDetalle.add(d.getIdPersonal());

                                        if (d.getMontoDescuento() != null && d.getMontoDescuento().compareTo(BigDecimal.ZERO) > 0)
                                        {
                                               pagosConDescuento++;
                                        }
                                        else
                                        {
                                               pagosCompletos++;
                                        }
                                  }
                           }
                           pagosPendientes = listaPersonal.size() - personalConDetalle.size();
                           if (pagosPendientes < 0) { pagosPendientes = 0; }
                    }
                    else
                    {
                           pagosPendientes = listaPersonal.size();
                    }

                    request.setAttribute("totalPagosMes", totalPagosMes);
                    request.setAttribute("estadoPlanillaActual", estadoPlanillaActual);
                    request.setAttribute("pagosCompletos", pagosCompletos);
                    request.setAttribute("pagosConDescuento", pagosConDescuento);
                    request.setAttribute("pagosPendientes", pagosPendientes);

                    // ===== NOTIFICACIONES (contador para la campanita) =====
                    int totalNotificaciones = 0;
                    if (tardanzasHoy > 0) { totalNotificaciones++; }
                    if (faltasHoy > 0) { totalNotificaciones++; }
                    if (!estadoPlanillaActual.equals("PROCESADA")) { totalNotificaciones++; }
                    request.setAttribute("totalNotificaciones", totalNotificaciones);

                    // ===== PERSONAL RECIENTE (ultimos 5) =====
                    List<Personal> personalOrdenado = new ArrayList<>(listaPersonal);
                    personalOrdenado.sort(Comparator.comparing(Personal::getFechaRegistro,
                                  Comparator.nullsLast(Comparator.reverseOrder())));

                    List<Personal> personalReciente = personalOrdenado.size() > 5
                                  ? personalOrdenado.subList(0, 5)
                                  : personalOrdenado;
                    request.setAttribute("personalReciente", personalReciente);

             }

             catch (Exception e)
             {
                    e.printStackTrace();
             }

             request.getRequestDispatcher(VISTA).forward(request, response);
       }




       // Clase auxiliar para el grafico de barras de la semana
       public static class DiaResumen
       {
             private String etiqueta;
             private int porcentajePresentes;
             private int porcentajeTardanzas;
             private int porcentajeFaltas;

             public String getEtiqueta() { return etiqueta; }
             public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }

             public int getPorcentajePresentes() { return porcentajePresentes; }
             public void setPorcentajePresentes(int p) { this.porcentajePresentes = p; }

             public int getPorcentajeTardanzas() { return porcentajeTardanzas; }
             public void setPorcentajeTardanzas(int p) { this.porcentajeTardanzas = p; }

             public int getPorcentajeFaltas() { return porcentajeFaltas; }
             public void setPorcentajeFaltas(int p) { this.porcentajeFaltas = p; }
       }
}