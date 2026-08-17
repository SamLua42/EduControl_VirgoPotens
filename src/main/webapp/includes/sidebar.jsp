<%-- sidebar del sistema --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<aside id="sidebar" class="sidebar">
    <div class="sidebar-header">
        <i class="bi bi-list"></i>
        <span>MENÚ</span>
    </div>

    <ul class="sidebar-menu">
        <li>
            <a href="${pageContext.request.contextPath}/dashboard/Dashboard.jsp" class="active">
                <i class="bi bi-house-door-fill"></i>
                <span>Dashboard</span>
            </a>
        </li>

        <li class="menu-title">MANTENIMIENTOS</li>
        <li>
            <a href="${pageContext.request.contextPath}/PersonalServlet?accion=listar">
                <i class="bi bi-people-fill"></i>
                <span>Personal</span>
            </a>
        </li>

        <li class="menu-title">PROCESOS</li>
        <li>
            <a href="${pageContext.request.contextPath}/AsistenciaServlet?accion=listar">
                <i class="bi bi-calendar-check-fill"></i>
                <span>Asistencia</span>
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/ConceptoPagoServlet?accion=listar">
                <i class="bi bi-cash-coin"></i>
                <span>Conceptos de Pago</span>
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/PlanillaServlet?accion=listar">
                <i class="bi bi-file-earmark-spreadsheet-fill"></i>
                <span>Planilla</span>
            </a>
        </li>

        <li class="menu-title">REPORTES</li>
        <li>
            <a href="${pageContext.request.contextPath}/ReporteServlet?accion=reporteAsistencia">
                <i class="bi bi-file-earmark-pdf-fill"></i>
                <span>Reporte Asistencia</span>
            </a>
        </li>
    </ul>
</aside>