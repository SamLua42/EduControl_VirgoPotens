<%-- header del sistema --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header class="header">
    <div class="header-logo">
        <a href="${pageContext.request.contextPath}/dashboard/Dashboard.jsp">
            <i class="bi bi-mortarboard-fill"></i>
            <span>EduControl</span>
        </a>
    </div>
    <div class="header-user">
        <i class="bi bi-moon" id="btnDarkMode" title="Modo oscuro" style="cursor:pointer; font-size:18px; margin-right:16px; color: var(--ec-vino-oscuro);"></i>
        <span class="me-3">
            <i class="bi bi-person-circle"></i>
            ${sessionScope.usuarioLogueado.nombre}
        </span>
        <a href="${pageContext.request.contextPath}/LoginServlet?accion=logout" class="logout">
            <i class="bi bi-box-arrow-right"></i>
            Salir
        </a>
    </div>
</header>