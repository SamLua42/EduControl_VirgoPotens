<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EduControl - Conceptos de Pago</title>

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
                    <h2 class="fw-bold"><i class="bi bi-cash-coin"></i> Conceptos de Pago</h2>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/dashboard/Dashboard.jsp">Inicio</a>
                            </li>
                            <li class="breadcrumb-item">Mantenimientos</li>
                            <li class="breadcrumb-item active">Conceptos de Pago</li>
                        </ol>
                    </nav>
                    <p class="text-muted" style="font-size: 13px;">
                        Configura la tarifa diaria y los descuentos aplicables por tipo de personal. Solo el Administrador puede modificar estos valores.
                    </p>
                </div>

                <div class="card shadow">
                    <div class="card-header">
                        <div class="row align-items-center">
                            <div class="col-md-6">
                                <h5 class="mb-0"><i class="bi bi-list-ul"></i> Tarifas Configuradas</h5>
                            </div>
                            <div class="col-md-6 text-end">
                                <button type="button" class="btn btn-vino" data-bs-toggle="modal" data-bs-target="#modalConcepto">
                                    <i class="bi bi-plus-circle"></i> Nuevo Concepto
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="tablaConcepto" class="table table-hover table-bordered align-middle nowrap" style="width:100%">
                                <thead class="table-dark">
                                    <tr>
                                        <th class="text-center">ID</th>
                                        <th>Tipo de Personal</th>
                                        <th class="text-end">Tarifa Diaria</th>
                                        <th class="text-end">Desc. Tardanza</th>
                                        <th class="text-end">Desc. Falta</th>
                                        <th>Última Actualización</th>
                                        <th class="text-center">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="cp" items="${listaConceptoPago}">
                                    <tr>
                                        <td class="text-center">${cp.idConcepto}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${fn:toUpperCase(cp.tipoPersonal) == 'DOCENTE'}">
                                                    <span class="badge" style="background: var(--ec-vino);">Docente</span>
                                                </c:when>
                                                <c:when test="${fn:toUpperCase(cp.tipoPersonal) == 'ADMINISTRATIVO'}">
                                                    <span class="badge" style="background: var(--ec-azul);">Administrativo</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge" style="background: var(--ec-dorado);">Técnico</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-end">S/ <fmt:formatNumber value="${cp.tarifaDiaria}" pattern="#,##0.00"/></td>
                                        <td class="text-end">S/ <fmt:formatNumber value="${cp.descuentoTardanza}" pattern="#,##0.00"/></td>
                                        <td class="text-end">S/ <fmt:formatNumber value="${cp.descuentoFalta}" pattern="#,##0.00"/></td>
                                        <td><fmt:formatDate value="${cp.fechaActualizacion}" pattern="dd/MM/yyyy HH:mm"/></td>
                                        <td class="text-center">
                                            <a href="${pageContext.request.contextPath}/ConceptoPagoServlet?accion=buscar&id=${cp.idConcepto}"
                                               class="btn btn-warning btn-sm" title="Editar">
                                                <i class="bi bi-pencil-square"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <%-- MODAL CONCEPTO DE PAGO --%>
                <div class="modal fade" id="modalConcepto" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-lg modal-dialog-scrollable">
                        <div class="modal-content">

                            <div class="modal-header" style="background: linear-gradient(135deg, var(--ec-vino), var(--ec-vino-oscuro)); color:#fff;">
                                <h5 class="modal-title">
                                    <c:choose>
                                        <c:when test="${editar}"><i class="bi bi-pencil-square"></i> Actualizar Concepto</c:when>
                                        <c:otherwise><i class="bi bi-plus-circle"></i> Nuevo Concepto de Pago</c:otherwise>
                                    </c:choose>
                                </h5>
                                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                            </div>

                            <form method="post" action="${pageContext.request.contextPath}/ConceptoPagoServlet">
                                <c:choose>
                                    <c:when test="${editar}">
                                        <input type="hidden" name="accion" value="actualizar">
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="accion" value="registrar">
                                    </c:otherwise>
                                </c:choose>
                                <input type="hidden" name="idConcepto" value="${conceptoPago.idConcepto}">

                                <div class="modal-body">

                                    <div class="mb-3">
                                        <label class="form-label">Tipo de Personal</label>
                                        <select class="form-select" name="tipoPersonal" required ${editar ? 'disabled' : ''}>
                                            <option value="">Seleccione...</option>
                                            <option value="Docente" ${fn:toUpperCase(conceptoPago.tipoPersonal) == 'DOCENTE' ? 'selected' : ''}>Docente</option>
                                            <option value="Administrativo" ${fn:toUpperCase(conceptoPago.tipoPersonal) == 'ADMINISTRATIVO' ? 'selected' : ''}>Administrativo</option>
                                            <option value="Tecnico" ${fn:toUpperCase(conceptoPago.tipoPersonal) == 'TECNICO' ? 'selected' : ''}>Técnico</option>
                                        </select>
                                        <c:if test="${editar}">
                                            <input type="hidden" name="tipoPersonal" value="${conceptoPago.tipoPersonal}">
                                            <small class="text-muted">El tipo de personal no se puede modificar una vez creado.</small>
                                        </c:if>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-4 mb-3">
                                            <label class="form-label">Tarifa Diaria (S/)</label>
                                            <input type="number" step="0.01" min="0" class="form-control" name="tarifaDiaria" value="${conceptoPago.tarifaDiaria}" required>
                                        </div>
                                        <div class="col-md-4 mb-3">
                                            <label class="form-label">Descuento por Tardanza (S/)</label>
                                            <input type="number" step="0.01" min="0" class="form-control" name="descuentoTardanza" value="${conceptoPago.descuentoTardanza}" required>
                                        </div>
                                        <div class="col-md-4 mb-3">
                                            <label class="form-label">Descuento por Falta (S/)</label>
                                            <input type="number" step="0.01" min="0" class="form-control" name="descuentoFalta" value="${conceptoPago.descuentoFalta}" required>
                                        </div>
                                    </div>

                                </div>

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                        <i class="bi bi-x-circle"></i> Cancelar
                                    </button>
                                    <c:choose>
                                        <c:when test="${editar}">
                                            <button type="submit" class="btn btn-warning"><i class="bi bi-pencil-square"></i> Actualizar</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" class="btn btn-vino"><i class="bi bi-floppy"></i> Guardar</button>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                            </form>
                        </div>
                    </div>
                </div>

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
        $('#tablaConcepto').DataTable({
            responsive: true,
            language: { url: 'https://cdn.datatables.net/plug-ins/2.3.3/i18n/es-ES.json' }
        });
    });
    </script>

    <c:if test="${editar}">
    <script>
    document.addEventListener("DOMContentLoaded", function(){
        var modal = new bootstrap.Modal(document.getElementById("modalConcepto"));
        modal.show();
    });
    </script>
    </c:if>

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

    <c:if test="${not empty error}">
    <script>
    Swal.fire({
        icon: 'error',
        title: 'Ocurrió un error',
        text: '${error}'
    });
    </script>
    </c:if>

</body>
</html>