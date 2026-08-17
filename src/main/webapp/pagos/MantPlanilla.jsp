<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EduControl - Planilla</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
<link href="https://cdn.datatables.net/2.3.3/css/dataTables.bootstrap5.css" rel="stylesheet">
<link href="https://cdn.datatables.net/responsive/3.0.7/css/responsive.bootstrap5.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

    <div class="d-flex" style="min-height: 100vh;">

        <jsp:include page="/includes/sidebar.jsp"/>

        <div class="d-flex flex-column flex-grow-1">

            <jsp:include page="/includes/header.jsp"/>

            <main class="p-4 contenido flex-grow-1">

                <div class="mb-4">
                    <h2 class="fw-bold"><i class="bi bi-file-earmark-spreadsheet-fill"></i> Planilla Mensual</h2>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/dashboard/Dashboard.jsp">Inicio</a>
                            </li>
                            <li class="breadcrumb-item">Procesos</li>
                            <li class="breadcrumb-item active">Planilla</li>
                        </ol>
                    </nav>
                </div>

                <div class="card shadow mb-4">
                    <div class="card-body">
                        <h5 class="mb-1"><i class="bi bi-calculator-fill" style="color: var(--ec-vino);"></i> Calcular Planilla Preliminar</h5>
                        <p class="text-muted mb-3" style="font-size: 13px;">
                            Selecciona el mes y año para calcular el pago preliminar de todo el personal, según su asistencia registrada.
                        </p>
                        <c:if test="${puedeEditar}">
                        <form method="post" action="${pageContext.request.contextPath}/PlanillaServlet" class="row g-3 align-items-end">
                            <input type="hidden" name="accion" value="calcular">
                            <div class="col-auto">
                                <label class="form-label">Mes</label>
                                <select class="form-select" name="mes" required>
                                    <option value="1">Enero</option>
                                    <option value="2">Febrero</option>
                                    <option value="3">Marzo</option>
                                    <option value="4">Abril</option>
                                    <option value="5">Mayo</option>
                                    <option value="6">Junio</option>
                                    <option value="7">Julio</option>
                                    <option value="8" selected>Agosto</option>
                                    <option value="9">Septiembre</option>
                                    <option value="10">Octubre</option>
                                    <option value="11">Noviembre</option>
                                    <option value="12">Diciembre</option>
                                </select>
                            </div>
                            <div class="col-auto">
                                <label class="form-label">Año</label>
                                <input type="number" class="form-control" name="anio" value="2026" min="2020" max="2100" required style="width: 110px;">
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="btn btn-vino">
                                    <i class="bi bi-calculator"></i> Calcular Planilla
                                </button>
                            </div>
                        </form>
						</c:if>
						<c:if test="${!puedeEditar}">
						<p class="text-muted mb-0" style="font-size: 13px;">Solo el Administrador puede calcular planillas.</p>
						</c:if>                        
                    </div>
                </div>

                <div class="card shadow mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="bi bi-list-ul"></i> Planillas Registradas</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="tablaPlanilla" class="table table-hover table-bordered align-middle nowrap" style="width:100%">
                                <thead class="table-dark">
                                    <tr>
                                        <th class="text-center">ID</th>
                                        <th>Periodo</th>
                                        <th class="text-center">Estado</th>
                                        <th>Fecha Procesado</th>
                                        <th class="text-center">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="pl" items="${listaPlanillas}">
                                    <tr>
                                        <td class="text-center">${pl.idPlanilla}</td>
                                        <td>${pl.mes}/${pl.anio}</td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${fn:toUpperCase(pl.estado) == 'PROCESADA'}">
                                                    <span class="badge" style="background:#4ade80;">Procesada</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge" style="background: var(--ec-dorado);">Preliminar</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${not empty pl.fechaProcesado}">
                                                <fmt:formatDate value="${pl.fechaProcesado}" pattern="dd/MM/yyyy HH:mm"/>
                                            </c:if>
                                            <c:if test="${empty pl.fechaProcesado}">-</c:if>
                                        </td>
                                        <td class="text-center">
                                            <a href="${pageContext.request.contextPath}/PlanillaServlet?accion=buscar&id=${pl.idPlanilla}"
                                               class="btn btn-vino btn-sm" title="Ver Detalle">
                                                <i class="bi bi-eye-fill"></i> Ver Detalle
                                            </a>
											<c:if test="${puedeEditar && fn:toUpperCase(pl.estado) != 'PROCESADA'}">
											    <a href="${pageContext.request.contextPath}/PlanillaServlet?accion=procesar&id=${pl.idPlanilla}"
											       class="btn btn-warning btn-sm btnProcesar" title="Procesar Planilla">
											        <i class="bi bi-check-circle-fill"></i> Procesar
											    </a>
											</c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <c:if test="${not empty planilla}">
                <div class="card shadow">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="bi bi-file-earmark-text-fill" style="color: var(--ec-vino);"></i>
                            Detalle - Planilla ${planilla.mes}/${planilla.anio}
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover table-bordered align-middle">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Personal</th>
                                        <th class="text-center">Dias Trabajados</th>
                                        <th class="text-center">Tardanzas</th>
                                        <th class="text-center">Faltas</th>
                                        <th class="text-end">Descuento</th>
                                        <th class="text-end">Total a Pagar</th>
                                        <th class="text-center">Boleta</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:set var="hayDetalles" value="false"/>
                                <c:forEach var="d" items="${listaDetalles}">
                                    <c:if test="${d.idPlanilla == planilla.idPlanilla}">
                                        <c:set var="hayDetalles" value="true"/>
                                        <tr>
                                            <td>${mapaPersonal[d.idPersonal].nombre} ${mapaPersonal[d.idPersonal].apellido}</td>
                                            <td class="text-center">${d.diasTrabajados}</td>
                                            <td class="text-center">${d.diasTardanza}</td>
                                            <td class="text-center">${d.diasFalta}</td>
                                            <td class="text-end">S/ <fmt:formatNumber value="${d.montoDescuento}" pattern="#,##0.00"/></td>
                                            <td class="text-end fw-bold">S/ <fmt:formatNumber value="${d.montoTotal}" pattern="#,##0.00"/></td>
                                            <td class="text-center">
                                                <c:if test="${fn:toUpperCase(planilla.estado) == 'PROCESADA'}">
                                                    <a href="${pageContext.request.contextPath}/ReporteServlet?accion=reporteBoleta&idDetalle=${d.idDetalle}"
                                                       class="btn btn-vino btn-sm" target="_blank" title="Ver Boleta PDF">
                                                        <i class="bi bi-file-earmark-pdf-fill"></i>
                                                    </a>
                                                </c:if>
                                                <c:if test="${fn:toUpperCase(planilla.estado) != 'PROCESADA'}">
                                                    <span class="text-muted" style="font-size: 11px;">Procesa primero</span>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${hayDetalles == 'false'}">
                                    <tr>
                                        <td colspan="7" class="text-center text-muted py-3">
                                            No hay detalles calculados para esta planilla.
                                        </td>
                                    </tr>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                </c:if>

            </main>

            <jsp:include page="/includes/footer.jsp"/>

        </div>

    </div>

    <jsp:include page="/includes/chatbot.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.datatables.net/2.3.3/js/dataTables.js"></script>
    <script src="https://cdn.datatables.net/2.3.3/js/dataTables.bootstrap5.js"></script>
    <script src="https://cdn.datatables.net/responsive/3.0.7/js/dataTables.responsive.js"></script>
    <script src="https://cdn.datatables.net/responsive/3.0.7/js/responsive.bootstrap5.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script>
    $(document).ready(function () {
        $('#tablaPlanilla').DataTable({
            responsive: true,
            language: { url: 'https://cdn.datatables.net/plug-ins/2.3.3/i18n/es-ES.json' }
        });
    });

    $(document).ready(function(){
        $(".btnProcesar").click(function(e){
            e.preventDefault();
            let url = $(this).attr("href");
            Swal.fire({
                title: "Procesar esta planilla?",
                text: "Una vez procesada, se generaran las boletas de pago. Esta accion no se puede deshacer.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Si, procesar",
                cancelButtonText: "Cancelar",
                confirmButtonColor: "#7a1f3d"
            }).then((result)=>{
                if(result.isConfirmed){ window.location = url; }
            });
        });
    });
    </script>

    <c:if test="${not empty mensaje}">
    <script>
    Swal.fire({
        icon: 'success',
        title: 'Correcto',
        text: '${mensaje}',
        timer: 2500,
        showConfirmButton: false
    });
    </script>
    </c:if>

</body>
</html>