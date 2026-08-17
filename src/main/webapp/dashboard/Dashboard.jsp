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

<style>
.dark-toggle-pill {
    position: relative;
    width: 52px;
    height: 26px;
    background: var(--ec-azul);
    border-radius: 999px;
    display: inline-flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 6px;
    box-sizing: border-box;
    cursor: pointer;
}
.dark-toggle-pill .toggle-icon-sun,
.dark-toggle-pill .toggle-icon-moon {
    font-size: 12px;
    color: #fff;
    position: relative;
    z-index: 1;
}
.dark-toggle-pill .toggle-thumb {
    position: absolute;
    top: 3px;
    left: 3px;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: var(--ec-dorado);
    transition: left .25s ease;
    z-index: 0;
}
.dark-toggle-pill.activo .toggle-thumb {
    left: 29px;
}

.donut-hole {
    background: #fff;
}
body.dark-mode .donut-hole {
    background: #181c27;
}

@media (max-width: 1300px) {
    .dash-header {
        flex-wrap: wrap;
        height: auto;
        min-height: 64px;
        padding: 12px 20px;
        row-gap: 10px;
    }
    .dash-header > div:last-of-type {
        flex-wrap: wrap;
        row-gap: 10px;
    }
}
@media (max-width: 950px) {
    .fecha-hora-block {
        display: none;
    }
}
</style>

</head>
<body>

    <div class="d-flex" style="min-height: 100vh;">

        <%-- ===================== SIDEBAR (columna completa, piso a techo) ===================== --%>
        <jsp:include page="/includes/sidebar.jsp"/>

        <%-- ===================== COLUMNA DERECHA: header + contenido + footer ===================== --%>
        <div class="d-flex flex-column flex-grow-1">

            <c:set var="miRolHeader" value="${fn:toUpperCase(sessionScope.usuarioLogueado.rol)}"/>

            <%
                int horaActual = java.time.LocalTime.now().getHour();
                String saludoDinamico;
                if (horaActual < 12) { saludoDinamico = "Buenos días"; }
                else if (horaActual < 19) { saludoDinamico = "Buenas tardes"; }
                else { saludoDinamico = "Buenas noches"; }
                pageContext.setAttribute("saludoDinamico", saludoDinamico);
            %>

            <header class="dash-header">
                <div>
                    <span style="font-size: 19px; font-weight: 700; color: var(--ec-vino-oscuro);">
                        ¡${saludoDinamico}, ${sessionScope.usuarioLogueado.nombre}! &#128075;
                    </span>
                    <div style="font-size: 12px; color: var(--ec-azul); font-weight: 700; margin-top: 2px;">
                        Bienvenido(a) al sistema EduControl
                    </div>
                </div>
                <div class="d-flex align-items-center">
                    <div class="d-flex align-items-center me-4 fecha-hora-block" style="font-size: 12px; color: var(--ec-azul); font-weight: 700;">
                        <i class="bi bi-calendar3 me-1"></i>
                        <fmt:formatDate value="<%= new java.util.Date() %>" pattern="EEEE, dd 'de' MMMM 'de' yyyy" var="fechaHoy"/>
                        <span class="me-3">${fechaHoy}</span>
                        <i class="bi bi-clock me-1"></i>
                        <span id="relojEnVivo" class="me-2">--:--:--</span>
                        <span class="badge" style="background:#4ade80; color:#0a2e12; font-size:10px; font-weight:600;">
                            <i class="bi bi-arrow-repeat"></i> Actualizado en tiempo real
                        </span>
                    </div>

                    <div class="d-flex align-items-center" style="gap: 18px; margin-right: 18px;">
                        <c:if test="${miRolHeader == 'ADMINISTRADOR' || miRolHeader == 'DIRECTOR'}">
                        <form action="${pageContext.request.contextPath}/PersonalServlet" method="get" id="formBusquedaRapida" class="d-flex align-items-center" style="margin:0;">
                            <input type="hidden" name="accion" value="listar">
                            <input type="text" name="buscar" id="inputBusquedaRapida" class="form-control form-control-sm"
                                   placeholder="Buscar personal..." autocomplete="off"
                                   style="display:none; width:160px; margin-right:8px;">
                            <i class="bi bi-search icon-btn" id="btnBusquedaRapida" title="Buscar personal" style="cursor:pointer; margin:0;"></i>
                        </form>
                        </c:if>
                        <c:if test="${miRolHeader == 'ADMINISTRADOR' || miRolHeader == 'DIRECTOR'}">
                        <a href="${pageContext.request.contextPath}/NotificacionesServlet" class="icon-btn" title="Notificaciones" style="text-decoration:none; margin:0; position:relative;">
                            <i class="bi bi-bell"></i>
                            <c:if test="${not empty totalNotificaciones and totalNotificaciones > 0}">
                                <span class="badge-count">${totalNotificaciones}</span>
                            </c:if>
                        </a>
                        </c:if>
                        <div class="dark-toggle-pill" id="btnDarkMode" title="Modo oscuro">
                            <i class="bi bi-sun-fill toggle-icon-sun"></i>
                            <i class="bi bi-moon-fill toggle-icon-moon"></i>
                            <span class="toggle-thumb"></span>
                        </div>
                    </div>

                    <div class="d-flex align-items-center ms-3">
                        <c:choose>
                            <c:when test="${not empty sessionScope.usuarioLogueado.fotoPerfil}">
                                <img src="${pageContext.request.contextPath}/img/perfiles/${sessionScope.usuarioLogueado.fotoPerfil}" alt="Foto de perfil" class="me-2" style="width:36px; height:36px; border-radius:50%; object-fit:cover; border:2px solid var(--ec-dorado);">
                            </c:when>
                            <c:otherwise>
                                <div class="avatar-circle me-2">
                                    ${fn:substring(sessionScope.usuarioLogueado.nombre, 0, 1)}${fn:substring(sessionScope.usuarioLogueado.apellido, 0, 1)}
                                </div>
                            </c:otherwise>
                        </c:choose>
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

                <c:set var="miRol" value="${fn:toUpperCase(sessionScope.usuarioLogueado.rol)}"/>

                <%-- KPIs --%>
                <div class="row g-3 mb-4">
                    <div class="col-md-3">
                        <div class="kpi-card kpi-vino">
                            <div class="kpi-icon"><i class="bi bi-people-fill"></i></div>
                            <div>
                                <p class="kpi-num">${not empty totalPersonal ? totalPersonal : 0}</p>
                                <p class="kpi-lbl">Personal registrado</p>
                            </div>
                            <a href="${pageContext.request.contextPath}/PersonalServlet?accion=listar" class="kpi-link">Ver detalle <i class="bi bi-arrow-right"></i></a>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="kpi-card kpi-azul">
                            <div class="kpi-icon"><i class="bi bi-check-circle-fill"></i></div>
                            <div>
                                <p class="kpi-num">${not empty presentesHoy ? presentesHoy : 0}</p>
                                <p class="kpi-lbl">Presentes hoy</p>
                            </div>
                            <a href="${pageContext.request.contextPath}/AsistenciaServlet?accion=listar" class="kpi-link">Ver detalle <i class="bi bi-arrow-right"></i></a>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="kpi-card kpi-dorado">
                            <div class="kpi-icon"><i class="bi bi-clock-fill"></i></div>
                            <div>
                                <p class="kpi-num">${not empty tardanzasHoy ? tardanzasHoy : 0}</p>
                                <p class="kpi-lbl">Tardanzas hoy</p>
                            </div>
                            <a href="${pageContext.request.contextPath}/AsistenciaServlet?accion=listar" class="kpi-link">Ver detalle <i class="bi bi-arrow-right"></i></a>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="kpi-card kpi-vino-oscuro">
                            <div class="kpi-icon"><i class="bi bi-x-circle-fill"></i></div>
                            <div>
                                <p class="kpi-num">${not empty faltasHoy ? faltasHoy : 0}</p>
                                <p class="kpi-lbl">Faltas hoy</p>
                            </div>
                            <a href="${pageContext.request.contextPath}/AsistenciaServlet?accion=listar" class="kpi-link">Ver detalle <i class="bi bi-arrow-right"></i></a>
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
                                <h3 class="fw-bold mb-0" style="color: var(--ec-vino-oscuro);">
                                    S/ <fmt:formatNumber value="${not empty totalPagosMes ? totalPagosMes : 0}" pattern="#,##0.00"/>
                                </h3>
                                <p class="text-muted mb-3" style="font-size: 11px;">Periodo ${mesActual}/${anioActual}</p>

                                <div class="mini-row">
                                    <span><i class="bi bi-check-circle-fill" style="color:#4ade80; font-size:11px;"></i> Pagos completos</span>
                                    <span class="fw-bold">${not empty pagosCompletos ? pagosCompletos : 0}</span>
                                </div>
                                <div class="mini-row">
                                    <span><i class="bi bi-exclamation-circle-fill" style="color: var(--ec-dorado); font-size:11px;"></i> Con descuentos</span>
                                    <span class="fw-bold">${not empty pagosConDescuento ? pagosConDescuento : 0}</span>
                                </div>
                                <div class="mini-row">
                                    <span><i class="bi bi-clock-fill" style="color: var(--ec-vino); font-size:11px;"></i> Pendientes</span>
                                    <span class="fw-bold">${not empty pagosPendientes ? pagosPendientes : 0}</span>
                                </div>

                                <a href="${pageContext.request.contextPath}/PlanillaServlet?accion=listar" class="btn btn-vino btn-sm w-100 mt-3">
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
                                    <a href="${pageContext.request.contextPath}/PersonalServlet?accion=listar" style="font-size: 12px; color: var(--ec-vino); font-weight: 600; text-decoration: none;">Ver todos</a>
                                </div>
                                <c:choose>
                                    <c:when test="${not empty personalReciente}">
                                        <c:forEach var="p" items="${personalReciente}">
                                            <div class="d-flex align-items-center justify-content-between py-2" style="border-bottom: 1px solid #f0f0f0;">
                                                <div class="d-flex align-items-center">
                                                    <c:choose>
                                                        <c:when test="${not empty p.fotoPerfil}">
                                                            <img src="${pageContext.request.contextPath}/img/perfiles/${p.fotoPerfil}" alt="Foto" style="width:34px;height:34px;border-radius:50%;object-fit:cover;" class="me-2">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="d-inline-flex align-items-center justify-content-center rounded-circle me-2" style="width:34px;height:34px;background:var(--ec-vino); color:#fff; font-size:11px; font-weight:600;">
                                                                ${fn:substring(p.nombre,0,1)}${fn:substring(p.apellido,0,1)}
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <div>
                                                        <div style="font-size:13px; font-weight:600;">${p.nombre} ${p.apellido}</div>
                                                        <div style="font-size:11px; color:#888;">${p.tipoPersonal} &middot; ${p.dni}</div>
                                                    </div>
                                                </div>
                                                <c:choose>
                                                    <c:when test="${p.estado}">
                                                        <span class="badge" style="background:#dcfce7; color:#16a34a; font-size:10.5px; font-weight:600; border-radius:20px; padding:4px 10px;">Activo</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge" style="background:#fee2e2; color:#dc2626; font-size:10.5px; font-weight:600; border-radius:20px; padding:4px 10px;">Inactivo</span>
                                                    </c:otherwise>
                                                </c:choose>
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
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h6 class="fw-bold mb-0"><i class="bi bi-calendar-event-fill" style="color: var(--ec-vino);"></i> Próximos pagos</h6>
                                    <a href="${pageContext.request.contextPath}/PlanillaServlet?accion=listar" style="font-size: 12px; color: var(--ec-vino); font-weight: 600; text-decoration: none;">Ver calendario</a>
                                </div>

                                <div class="d-flex align-items-start py-2" style="border-bottom: 1px solid #f0f0f0;">
                                    <div class="text-center me-3" style="min-width:44px;">
                                        <div style="font-size:16px; font-weight:700; color: var(--ec-vino-oscuro); line-height:1;">${diaFinMes}</div>
                                        <div style="font-size:10px; color:#888; letter-spacing:0.5px;">${nombreMesActualAbrev}</div>
                                    </div>
                                    <div class="flex-grow-1">
                                        <div style="font-size:13px; font-weight:600;">Planilla mensual</div>
                                        <div style="font-size:11px; color:#888;">Todo el personal &middot; ${mesActual}/${anioActual}</div>
                                    </div>
                                    <div class="text-end">
                                        <c:choose>
                                            <c:when test="${estadoPlanillaActual == 'PROCESADA'}">
                                                <div style="font-size:13px; font-weight:700; color: var(--ec-vino-oscuro);">
                                                    S/ <fmt:formatNumber value="${totalPagosMes}" pattern="#,##0.00"/>
                                                </div>
                                                <span class="estado-badge estado-procesada" style="font-size:9.5px;">Procesada</span>
                                            </c:when>
                                            <c:otherwise>
                                                <div style="font-size:12px; color:#888;">Por calcular</div>
                                                <span class="estado-badge estado-preliminar" style="font-size:9.5px;">Pendiente</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <div class="d-flex align-items-start py-2">
                                    <div class="text-center me-3" style="min-width:44px;">
                                        <div style="font-size:16px; font-weight:700; color: var(--ec-azul); line-height:1;">01</div>
                                        <div style="font-size:10px; color:#888; letter-spacing:0.5px;">${nombreMesSiguienteAbrev}</div>
                                    </div>
                                    <div class="flex-grow-1">
                                        <div style="font-size:13px; font-weight:600;">Próxima planilla</div>
                                        <div style="font-size:11px; color:#888;">Todo el personal &middot; ${mesSiguiente}/${anioSiguiente}</div>
                                    </div>
                                    <div class="text-end">
                                        <div style="font-size:12px; color:#888;">Por calcular</div>
                                        <span class="estado-badge estado-preliminar" style="font-size:9.5px;">Pendiente</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card shadow-sm h-100">
                            <div class="card-body">
                                <h6 class="fw-bold mb-3"><i class="bi bi-pie-chart-fill" style="color: var(--ec-vino);"></i> Resumen del mes</h6>

                                <c:set var="pctP" value="${not empty pctPresentes ? pctPresentes : 0}"/>
                                <c:set var="pctT" value="${not empty pctTardanzas ? pctTardanzas : 0}"/>
                                <c:set var="pctF" value="${not empty pctFaltas ? pctFaltas : 0}"/>
                                <c:set var="acumPT" value="${pctP + pctT}"/>

                                <div style="position:relative; width:130px; height:130px; margin:0 auto 16px;">
                                    <div style="width:100%; height:100%; border-radius:50%; background: conic-gradient(
                                        var(--ec-azul) 0% ${pctP}%,
                                        var(--ec-vino) ${pctP}% ${acumPT}%,
                                        var(--ec-dorado) ${acumPT}% 100%
                                    );"></div>
                                    <div class="donut-hole" style="position:absolute; inset:16px; border-radius:50%; display:flex; align-items:center; justify-content:center;">
                                        <span style="font-size:12px; font-weight:700; color: var(--ec-vino-oscuro);">${mesActual}/${anioActual}</span>
                                    </div>
                                </div>

                                <div class="donut-legend">
                                    <div class="mini-row">
                                        <span><span class="dot" style="background: var(--ec-azul);"></span>Presentes</span>
                                        <span>${pctP}%</span>
                                    </div>
                                    <div class="mini-row">
                                        <span><span class="dot" style="background: var(--ec-vino);"></span>Tardanzas</span>
                                        <span>${pctT}%</span>
                                    </div>
                                    <div class="mini-row">
                                        <span><span class="dot" style="background: var(--ec-dorado);"></span>Faltas</span>
                                        <span>${pctF}%</span>
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
                </div>

            </main>

            <jsp:include page="/includes/footer.jsp"/>

        </div>

    </div>

    <jsp:include page="/includes/chatbot.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

    <script>
    document.addEventListener('DOMContentLoaded', function () {
        var btnBuscar = document.getElementById('btnBusquedaRapida');
        var inputBuscar = document.getElementById('inputBusquedaRapida');
        if (btnBuscar && inputBuscar) {
            btnBuscar.addEventListener('click', function () {
                if (inputBuscar.style.display === 'none') {
                    inputBuscar.style.display = 'inline-block';
                    inputBuscar.focus();
                } else if (inputBuscar.value.trim() !== '') {
                    document.getElementById('formBusquedaRapida').submit();
                } else {
                    inputBuscar.focus();
                }
            });
        }

        function actualizarRelojEnVivo() {
            var ahora = new Date();
            var horas = ahora.getHours();
            var minutos = ahora.getMinutes().toString().padStart(2, '0');
            var segundos = ahora.getSeconds().toString().padStart(2, '0');
            var ampm = horas >= 12 ? 'PM' : 'AM';
            var horas12 = horas % 12;
            horas12 = horas12 ? horas12 : 12;
            var horasStr = horas12.toString().padStart(2, '0');
            var elementoReloj = document.getElementById('relojEnVivo');
            if (elementoReloj) {
                elementoReloj.textContent = horasStr + ':' + minutos + ':' + segundos + ' ' + ampm;
            }
        }
        actualizarRelojEnVivo();
        setInterval(actualizarRelojEnVivo, 1000);
    });
    </script>

</body>
</html>