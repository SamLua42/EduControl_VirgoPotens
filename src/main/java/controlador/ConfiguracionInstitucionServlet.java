package controlador;

import java.io.IOException;
import java.math.BigDecimal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ConfiguracionInstitucionDAO;
import entidad.ConfiguracionInstitucion;
import entidad.Personal;

@WebServlet("/ConfiguracionInstitucionServlet")
public class ConfiguracionInstitucionServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private ConfiguracionInstitucionDAO configDAO;

    public ConfiguracionInstitucionServlet() { configDAO = new ConfiguracionInstitucionDAO(); }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.sendRedirect(request.getContextPath() + "/configuracion/Configuracion.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Personal usuarioSesion = (Personal) session.getAttribute("usuarioLogueado");

        if (usuarioSesion == null)
        {
            response.sendRedirect(request.getContextPath() + "/login/login.jsp");
            return;
        }

        if (!usuarioSesion.getRol().equalsIgnoreCase("Administrador"))
        {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet");
            return;
        }

        ConfiguracionInstitucion c = new ConfiguracionInstitucion();
        c.setId(1);
        c.setLogoRuta(request.getParameter("logoRuta"));
        c.setDependencia(request.getParameter("dependencia"));
        c.setTelefono(request.getParameter("telefono"));
        c.setPaginaWeb(request.getParameter("paginaWeb"));
        c.setForma(request.getParameter("forma"));
        c.setDirector(request.getParameter("director"));
        c.setNivelModalidad(request.getParameter("nivelModalidad"));
        c.setGenero(request.getParameter("genero"));
        c.setTurno(request.getParameter("turno"));

        int tolerancia = 10;
        try { tolerancia = Integer.parseInt(request.getParameter("toleranciaTardanzaMinutos")); }
        catch (NumberFormatException e) { /* deja el valor por defecto si viene vacio o invalido */ }
        c.setToleranciaTardanzaMinutos(tolerancia);

        BigDecimal porcentajeOnp = new BigDecimal("13.00");
        try { porcentajeOnp = new BigDecimal(request.getParameter("porcentajeOnp")); }
        catch (Exception e) { /* deja el valor por defecto si viene vacio o invalido */ }
        c.setPorcentajeOnp(porcentajeOnp);

        BigDecimal porcentajeAfp = new BigDecimal("11.37");
        try { porcentajeAfp = new BigDecimal(request.getParameter("porcentajeAfp")); }
        catch (Exception e) { /* deja el valor por defecto si viene vacio o invalido */ }
        c.setPorcentajeAfp(porcentajeAfp);

        BigDecimal porcentajeEssalud = new BigDecimal("9.00");
        try { porcentajeEssalud = new BigDecimal(request.getParameter("porcentajeEssalud")); }
        catch (Exception e) { /* deja el valor por defecto si viene vacio o invalido */ }
        c.setPorcentajeEssalud(porcentajeEssalud);

        int resultado = configDAO.actualizar(c);

        if (resultado > 0)
        {
            request.setAttribute("mensajeConfig", "Configuracion del sistema actualizada correctamente.");
        }
        else
        {
            request.setAttribute("errorConfig", "No se pudo actualizar la configuracion del sistema.");
        }

        request.getRequestDispatcher("configuracion/Configuracion.jsp").forward(request, response);
    }
}