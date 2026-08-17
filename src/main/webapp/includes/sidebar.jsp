<%-- sidebar del sistema --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<aside id="sidebar" class="sidebar">
    <div style="padding: 20px 16px; text-align: center; border-bottom: 1px solid rgba(255,255,255,0.1); margin-bottom: 12px;">
        <div style="display:flex; align-items:center; justify-content:center; gap:12px;">
            <img src="${pageContext.request.contextPath}/img/Insignia1.png" alt="Escudo I.E. Virgo Potens" style="width:70px;height:70px;object-fit:contain;">
            <div style="text-align: left;">
                <div style="color:#fff; font-size:19px; font-weight:700; line-height:1.1;">EduControl</div>
                <div style="color:#c9a24b; font-size:10px; margin-top:3px; line-height:1.3;">I.E. VIRGO POTENS<br>VIRGEN PODEROSA</div>
            </div>
        </div>
    </div>

    <c:set var="miRol" value="${fn:toUpperCase(sessionScope.usuarioLogueado.rol)}"/>

    <ul class="sidebar-menu">
        <li>
            <a href="${pageContext.request.contextPath}/DashboardServlet" class="active">
                <i class="bi bi-house-door-fill"></i>
                <span>Dashboard</span>
            </a>
        </li>

        <%-- Personal: Administrador (todo) o Director (solo lectura) --%>
        <c:if test="${miRol == 'ADMINISTRADOR' || miRol == 'DIRECTOR'}">
        <li class="menu-title">MANTENIMIENTOS</li>
        <li>
            <a href="${pageContext.request.contextPath}/PersonalServlet?accion=listar">
                <i class="bi bi-people-fill"></i>
                <span>Personal</span>
            </a>
        </li>
        </c:if>

        <%-- Asistencia: Trabajador y Administrador, NO Director --%>
        <c:if test="${miRol != 'DIRECTOR'}">
        <li class="menu-title">PROCESOS</li>
        <li>
            <a href="${pageContext.request.contextPath}/AsistenciaServlet?accion=listar">
                <i class="bi bi-calendar-check-fill"></i>
                <span>Asistencia</span>
            </a>
        </li>
        </c:if>

        <%-- Conceptos y Planilla: Administrador (todo) o Director (solo lectura) --%>
        <c:if test="${miRol == 'ADMINISTRADOR' || miRol == 'DIRECTOR'}">
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
        </c:if>

        <li class="menu-title">REPORTES</li>
        <li>
            <a href="${pageContext.request.contextPath}/ReporteServlet?accion=reporteAsistencia">
                <i class="bi bi-file-earmark-pdf-fill"></i>
                <span>Reporte Asistencia</span>
            </a>
        </li>

        <%-- Configuracion del sistema: SOLO Administrador --%>
        <c:if test="${miRol == 'ADMINISTRADOR'}">
        <li class="menu-title">SISTEMA</li>
        <li>
            <a href="${pageContext.request.contextPath}/configuracion/Configuracion.jsp">
                <i class="bi bi-gear-fill"></i>
                <span>Configuración</span>
            </a>
        </li>
        </c:if>
    </ul>
</aside>