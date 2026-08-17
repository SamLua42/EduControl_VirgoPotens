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