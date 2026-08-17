<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EduControl - Dashboard</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">

</head>
<body>

    <div class="d-flex" style="min-height: 100vh;">

        <%-- ===================== SIDEBAR (columna completa, piso a techo) ===================== --%>
        <jsp:include page="/includes/sidebar.jsp"/>

        <%-- ===================== COLUMNA DERECHA: header + contenido + footer ===================== --%>
        <div class="d-flex flex-column flex-grow-1">

            <header class="dash-header">
                <div>
                    <span style="font-size: 17px; font-weight: 700; color: var(--ec-vino-oscuro);">
                        Buenos días, ${sessionScope.usuarioLogueado.nombre}
                    </span>
                    <div style="font-size: 12px; color: #888;">
                        <fmt:formatDate value="<%= new java.util.Date() %>" pattern="EEEE, dd 'de' MMMM 'de' yyyy" var="fechaHoy"/>
                        ${fechaHoy}
                    </div>
                </div>
                <div class="d-flex align-items-center">
                    <i class="bi bi-search icon-btn" title="Búsqueda (próximamente)"></i>
                    <div class="icon-btn" title="Notificaciones (próximamente)">
                        <i class="bi bi-bell"></i>
                        <span class="badge-count">3</span>
                    </div>
                    <i class="bi bi-moon icon-btn" id="btnDarkMode" title="Modo oscuro" style="cursor:pointer;"></i>
                    <div class="d-flex align-items-center ms-3">
                        <div class="avatar-circle me-2">
                            ${fn:substring(sessionScope.usuarioLogueado.nombre, 0, 1)}${fn:substring(sessionScope.usuarioLogueado.apellido, 0, 1)}
                        </div>
                        <div>
                            <div style="font-size: 13px; font-weight: 600;">${sessionScope.usuarioLogueado.nombre} ${sessionScope.usuarioLogueado.apellido}</div>
                            <div style="font-size: 11px; color: #888;">${sessionScope.usuarioLogueado.rol}</div>
                        </div>
                        <a href="${pageContext.request.contextPath}/LoginServlet?accion=logout" class="ms-3" title="Cerrar sesión">
                            <i class="bi bi-box-arrow-right icon-btn" style="margin-left:0;"></i>
                        </a>
                    </div>
                </div>
            </header>

            <main class="p-4 contenido flex-grow-1">

                <%-- KPIs --%>
                <div class="row g-3 mb-4">
                    <div class="col-md-3">
                        <div class="kpi-card kpi-vino">
                            <div>
                                <p class="kpi-num">${not empty totalPersonal ? totalPersonal : 0}</p>
                                <p class="kpi-lbl">Personal registrado</p>
                            </div>
                            <i class="bi bi-people-fill"></i>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="kpi-card kpi-azul">
                            <div>
                                <p class="kpi-num">${not empty presentesHoy ? presentesHoy : 0}</p>
                                <p class="kpi-lbl">Presentes hoy</p>
                            </div>
                            <i class="bi bi-check-circle-fill"></i>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="kpi-card kpi-dorado">
                            <div>
                                <p class="kpi-num">${not empty tardanzasHoy ? tardanzasHoy : 0}</p>
                                <p class="kpi-lbl">Tardanzas hoy</p>
                            </div>
                            <i class="bi bi-clock-fill"></i>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="kpi-card kpi-vino-oscuro">
                            <div>
                                <p class="kpi-num">${not empty faltasHoy ? faltasHoy : 0}</p>
                                <p class="kpi-lbl">Faltas hoy</p>
                            </div>
                            <i class="bi bi-x-circle-fill"></i>
                        </div>
                    </div>
                </div>

                <%-- ZONA CENTRAL --%>
                <div class="row g-3 mb-4">
                    <div class="col-md-8">
                        <div class="card shadow-sm h-100">
                            <div class="card-body">
                                <h6 class="fw-bold mb-3"><i class="bi bi-bar-chart-fill" style="color: var(--ec-vino);"></i> Asistencia de la semana</h6>
                                <c:choose>
                                    <c:when test="${not empty asistenciaSemana}">
                                        <div class="bar-chart">
                                            <c:forEach var="dia" items="${asistenciaSemana}">
                                                <div class="bar-col">
                                                    <div class="bar-track">
                                                        <div class="bar" style="height:${dia.porcentajePresentes}%; background: var(--ec-azul);"></div>
                                                        <div class="bar" style="height:${dia.porcentajeTardanzas}%; background: var(--ec-vino);"></div>
                                                        <div class="bar" style="height:${dia.porcentajeFaltas}%; background: var(--ec-dorado);"></div>
                                                    </div>
                                                    <small style="font-size: 11px; color: #888;">${dia.etiqueta}</small>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted text-center py-4 mb-0">Aún no hay datos de la semana para graficar.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card shadow-sm h-100">
                            <div class="card-body">
                                <h6 class="fw-bold mb-2"><i class="bi bi-wallet-fill" style="color: var(--ec-vino);"></i> Pagos del mes</h6>
                                <p class="mb-0 text-muted" style="font-size: 12px;">Total a pagar</p>
                                <h3 class="fw-bold" style="color: var(--ec-vino-oscuro);">
                                    S/ <fmt:formatNumber value="${not empty totalPagosMes ? totalPagosMes : 0}" pattern="#,##0.00"/>
                                </h3>
                                <a href="${pageContext.request.contextPath}/PlanillaServlet?accion=listar" class="btn btn-vino btn-sm w-100 mt-2">
                                    Ver pagos
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- ZONA INFERIOR --%>
                <div class="row g-3 mb-4">
                    <div class="col-md-4">
                        <div class="card shadow-sm h-100">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <h6 class="fw-bold mb-0"><i class="bi bi-person-lines-fill" style="color: var(--ec-vino);"></i> Personal reciente</h6>
                                    <a href="${pageContext.request.contextPath}/PersonalServlet?accion=listar" style="font-size: 12px;">Ver todos</a>
                                </div>
                                <c:choose>
                                    <c:when test="${not empty personalReciente}">
                                        <c:forEach var="p" items="${personalReciente}">
                                            <div class="mini-row">
                                                <span>${p.nombre} ${p.apellido}</span>
                                                <span class="text-muted" style="font-size:11px;">${p.tipoPersonal}</span>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted text-center py-3 mb-0" style="font-size: 13px;">Sin registros recientes.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card shadow-sm h-100">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <h6 class="fw-bold mb-0"><i class="bi bi-calendar-event-fill" style="color: var(--ec-vino);"></i> Próximos pagos</h6>
                                    <a href="${pageContext.request.contextPath}/PlanillaServlet?accion=listar" style="font-size: 12px;">Ver calendario</a>
                                </div>
                                <div class="mini-row">
                                    <span>Planilla ${mesActual}/${anioActual}</span>
                                    <c:choose>
                                        <c:when test="${estadoPlanillaActual == 'PROCESADA'}">
                                            <span class="estado-badge estado-procesada">Procesada</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="estado-badge estado-preliminar">Pendiente</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <p class="text-muted mt-2 mb-0" style="font-size: 11px;">
                                    La planilla mensual se calcula desde el módulo Planilla una vez que se cierra el periodo.
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card shadow-sm h-100">
                            <div class="card-body">
                                <h6 class="fw-bold mb-3"><i class="bi bi-pie-chart-fill" style="color: var(--ec-vino);"></i> Resumen del mes</h6>
                                <div class="donut-legend">
                                    <div class="mini-row">
                                        <span><span class="dot" style="background: var(--ec-azul);"></span>Presentes</span>
                                        <span>${not empty pctPresentes ? pctPresentes : 0}%</span>
                                    </div>
                                    <div class="mini-row">
                                        <span><span class="dot" style="background: var(--ec-vino);"></span>Tardanzas</span>
                                        <span>${not empty pctTardanzas ? pctTardanzas : 0}%</span>
                                    </div>
                                    <div class="mini-row">
                                        <span><span class="dot" style="background: var(--ec-dorado);"></span>Faltas</span>
                                        <span>${not empty pctFaltas ? pctFaltas : 0}%</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- CIERRE --%>
                <div class="row g-3">
                    <div class="col-md-8">
                        <div class="card shadow-sm">
                            <div class="card-body d-flex flex-wrap gap-4 align-items-center">
                                <h6 class="fw-bold mb-0 me-3"><i class="bi bi-gear-fill" style="color: var(--ec-vino);"></i> Configuración rápida</h6>
                                <a href="${pageContext.request.contextPath}/configuracion/Configuracion.jsp#perfil" class="text-decoration-none text-muted" style="font-size: 13px;">
                                    <i class="bi bi-person"></i> Mi perfil
                                </a>
                                <a href="${pageContext.request.contextPath}/configuracion/Configuracion.jsp#clave" class="text-decoration-none text-muted" style="font-size: 13px;">
                                    <i class="bi bi-key"></i> Cambiar contraseña
                                </a>
                                <a href="${pageContext.request.contextPath}/configuracion/Configuracion.jsp#preferencias" class="text-decoration-none text-muted" style="font-size: 13px;">
                                    <i class="bi bi-sliders"></i> Preferencias
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card shadow-sm">
                            <div class="card-body d-flex justify-content-between align-items-center">
                                <div>
                                    <p class="fw-bold mb-0" style="font-size: 13px;">EduBot</p>
                                    <p class="text-muted mb-0" style="font-size: 11px;">Asistente virtual</p>
                                </div>
                                <button class="btn btn-vino btn-sm" onclick="document.getElementById('ecChatToggle').click();">
                                    Abrir chat
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

            </main>

            <jsp:include page="/includes/footer.jsp"/>

        </div>

    </div>

    <jsp:include page="/includes/chatbot.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>