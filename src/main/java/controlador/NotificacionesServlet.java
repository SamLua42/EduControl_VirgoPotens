package controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.AsistenciaDAO;
import dao.PlanillaDAO;
import entidad.Asistencia;
import entidad.Personal;
import entidad.Planilla;

@WebServlet("/NotificacionesServlet")
public class NotificacionesServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private static final String VISTA = "notificaciones/Notificaciones.jsp";

    private AsistenciaDAO asistenciaDAO;
    private PlanillaDAO planillaDAO;

    public NotificacionesServlet()
    {
        asistenciaDAO = new AsistenciaDAO();
        planillaDAO = new PlanillaDAO();
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
        HttpSession session = request.getSession();
        Personal usuarioSesion = (Personal) session.getAttribute("usuarioLogueado");

        if (usuarioSesion == null)
        {
            response.sendRedirect(request.getContextPath() + "/login/login.jsp");
            return;
        }

        boolean esAdministrador = usuarioSesion.getRol().equalsIgnoreCase("Administrador");
        boolean esDirector = usuarioSesion.getRol().equalsIgnoreCase("Director");

        if (!esAdministrador && !esDirector)
        {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet");
            return;
        }

        try
        {
            LocalDate hoy = LocalDate.now();
            List<Asistencia> listaAsistencia = asistenciaDAO.listar();

            int tardanzasHoy = 0, faltasHoy = 0;
            for (Asistencia a : listaAsistencia)
            {
                if (a.getFecha().toLocalDate().equals(hoy))
                {
                    String clas = a.getClasificacion();
                    if (clas.equalsIgnoreCase("Tardanza")) { tardanzasHoy++; }
                    else if (clas.equalsIgnoreCase("Falta")) { faltasHoy++; }
                }
            }

            int mesActual = hoy.getMonthValue();
            int anioActual = hoy.getYear();

            String estadoPlanillaActual = "PRELIMINAR";
            for (Planilla pl : planillaDAO.listar())
            {
                if (pl.getMes() == mesActual && pl.getAnio() == anioActual)
                {
                    estadoPlanillaActual = pl.getEstado().toUpperCase();
                    break;
                }
            }

            List<String> notificaciones = new ArrayList<>();

            if (tardanzasHoy > 0)
            {
                notificaciones.add(tardanzasHoy + " persona(s) registraron tardanza hoy.");
            }
            if (faltasHoy > 0)
            {
                notificaciones.add(faltasHoy + " persona(s) registraron falta hoy.");
            }
            if (!estadoPlanillaActual.equals("PROCESADA"))
            {
                notificaciones.add("La planilla de " + mesActual + "/" + anioActual + " aun esta pendiente de procesar.");
            }

            request.setAttribute("notificaciones", notificaciones);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            request.setAttribute("notificaciones", new ArrayList<String>());
        }

        request.getRequestDispatcher(VISTA).forward(request, response);
    }
}