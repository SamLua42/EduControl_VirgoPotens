<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EduControl - Personal</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
<link href="https://cdn.datatables.net/2.3.3/css/dataTables.bootstrap5.css" rel="stylesheet">
<link href="https://cdn.datatables.net/responsive/3.0.7/css/responsive.bootstrap5.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

    <div class="d-flex" style="min-height: 100vh;">

        <%-- ===================== SIDEBAR (piso a techo, con escudo) ===================== --%>
        <jsp:include page="/includes/sidebar.jsp"/>

        <%-- ===================== COLUMNA DERECHA ===================== --%>
        <div class="d-flex flex-column flex-grow-1">

            <jsp:include page="/includes/header.jsp"/>

            <main class="p-4 contenido flex-grow-1">

                <div class="mb-4">
                    <h2 class="fw-bold"><i class="bi bi-people-fill"></i> Mantenimiento de Personal</h2>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/dashboard/Dashboard.jsp">Inicio</a>
                            </li>
                            <li class="breadcrumb-item">Mantenimientos</li>
                            <li class="breadcrumb-item active">Personal</li>
                        </ol>
                    </nav>
                </div>

                <div class="card shadow">
                    <div class="card-header">
                        <div class="row align-items-center">
                            <div class="col-md-6">
                                <h5 class="mb-0"><i class="bi bi-list-ul"></i> Lista de Personal</h5>
                            </div>
							<div class="col-md-6 text-end">
							    <c:if test="${puedeEditar}">
							    <button type="button" class="btn btn-vino" data-bs-toggle="modal" data-bs-target="#modalPersonal">
							        <i class="bi bi-plus-circle"></i> Nuevo Personal
							    </button>
							    </c:if>
							</div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="tablaPersonal" class="table table-hover table-bordered align-middle nowrap" style="width:100%">
                                <thead class="table-dark">
                                    <tr>
                                        <th class="text-center">ID</th>
                                        <th>Nombre completo</th>
                                        <th>DNI</th>
                                        <th>Cargo</th>
                                        <th>Tipo</th>
                                        <th>Usuario</th>
                                        <th>Rol</th>
                                        <th class="text-center">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="p" items="${listaPersonal}">
                                    <tr>
                                        <td class="text-center">${p.idPersonal}</td>
                                        <td>${p.nombre} ${p.apellido}</td>
                                        <td>${p.dni}</td>
                                        <td>${p.cargo}</td>
                                        <td>${p.tipoPersonal}</td>
                                        <td>${p.usuario}</td>
                                        <td>${p.rol}</td>
										<td class="text-center">
										    <c:if test="${puedeEditar}">
										    <a href="${pageContext.request.contextPath}/PersonalServlet?accion=buscar&id=${p.idPersonal}"
										       class="btn btn-warning btn-sm" title="Editar">
										        <i class="bi bi-pencil-square"></i>
										    </a>
										    <a href="${pageContext.request.contextPath}/PersonalServlet?accion=eliminar&id=${p.idPersonal}"
										       class="btn btn-danger btn-sm btnEliminar" title="Eliminar">
										        <i class="bi bi-trash"></i>
										    </a>
										    </c:if>
										    <c:if test="${!puedeEditar}">
										        <span class="text-muted" style="font-size: 11px;">Solo lectura</span>
										    </c:if>
										</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <%-- MODAL PERSONAL --%>
                <div class="modal fade" id="modalPersonal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-lg modal-dialog-scrollable">
                        <div class="modal-content">

                            <div class="modal-header" style="background: linear-gradient(135deg, var(--ec-vino), var(--ec-vino-oscuro)); color:#fff;">
                                <h5 class="modal-title">
                                    <c:choose>
                                        <c:when test="${editar}"><i class="bi bi-pencil-square"></i> Actualizar Personal</c:when>
                                        <c:otherwise><i class="bi bi-plus-circle"></i> Registrar Personal</c:otherwise>
                                    </c:choose>
                                </h5>
                                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                            </div>

                            <form method="post" action="${pageContext.request.contextPath}/PersonalServlet">
                                <c:choose>
                                    <c:when test="${editar}">
                                        <input type="hidden" name="accion" value="actualizar">
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="accion" value="registrar">
                                    </c:otherwise>
                                </c:choose>
                                <input type="hidden" name="idPersonal" value="${personal.idPersonal}">

                                <div class="modal-body">

                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label class="form-label">Nombre</label>
                                            <input type="text" class="form-control" name="nombre" value="${personal.nombre}" required>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label class="form-label">Apellido</label>
                                            <input type="text" class="form-control" name="apellido" value="${personal.apellido}" required>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-4 mb-3">
                                            <label class="form-label">DNI</label>
                                            <input type="text" class="form-control" name="dni" maxlength="8" value="${personal.dni}" required>
                                        </div>
                                        <div class="col-md-4 mb-3">
                                            <label class="form-label">Cargo</label>
                                            <input type="text" class="form-control" name="cargo" value="${personal.cargo}" required>
                                        </div>
                                        <div class="col-md-4 mb-3">
                                            <label class="form-label">Tipo de Personal</label>
                                            <select class="form-select" name="tipoPersonal" required>
											    <option value="">Seleccione...</option>
											    <option value="DOCENTE" ${fn:toUpperCase(personal.tipoPersonal) == 'DOCENTE' ? 'selected' : ''}>Docente</option>
											    <option value="ADMINISTRATIVO" ${fn:toUpperCase(personal.tipoPersonal) == 'ADMINISTRATIVO' ? 'selected' : ''}>Administrativo</option>
											    <option value="TECNICO" ${fn:toUpperCase(personal.tipoPersonal) == 'TECNICO' ? 'selected' : ''}>Técnico</option>
											</select>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-4 mb-3">
                                            <label class="form-label">Hora de entrada esperada</label>
                                            <input type="time" class="form-control" name="horaEntradaEsperada"
                                                   value="${personal.horaEntradaEsperada}" required>
                                        </div>
                                        <div class="col-md-4 mb-3">
                                            <label class="form-label">Rol en el sistema</label>
												<select class="form-select" name="rol" required>
												    <option value="">Seleccione...</option>
												    <option value="TRABAJADOR" ${fn:toUpperCase(personal.rol) == 'TRABAJADOR' ? 'selected' : ''}>Trabajador</option>
												    <option value="ADMINISTRADOR" ${fn:toUpperCase(personal.rol) == 'ADMINISTRADOR' ? 'selected' : ''}>Administrador</option>
												    <option value="DIRECTOR" ${fn:toUpperCase(personal.rol) == 'DIRECTOR' ? 'selected' : ''}>Director</option>
												</select>
                                        </div>
                                        <div class="col-md-4 mb-3">
                                            <label class="form-label">Usuario</label>
                                            <input type="text" class="form-control" name="usuario" value="${personal.usuario}" required>
                                        </div>
                                    </div>

                                    <div class="row">
										<div class="row">
										    <div class="col-md-6 mb-3">
										        <label class="form-label">Contraseña</label>
										        <input type="password" class="form-control" name="contrasena"
										               placeholder="${editar ? 'No se puede modificar aquí' : ''}"
										               ${editar ? 'disabled' : 'required'}>
										        <c:if test="${editar}">
										            <small class="text-muted">Para cambiar la contraseña, usa "Cambiar contraseña" en Configuración.</small>
										        </c:if>
										    </div>
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
        var parametrosUrl = new URLSearchParams(window.location.search);
        var terminoBusqueda = parametrosUrl.get('buscar');

        $('#tablaPersonal').DataTable({
            responsive: true,
            language: { url: 'https://cdn.datatables.net/plug-ins/2.3.3/i18n/es-ES.json' },
            initComplete: function () {
                if (terminoBusqueda) {
                    this.api().search(terminoBusqueda).draw();
                }
            }
        });
    });

    $(document).ready(function(){
        $(".btnEliminar").click(function(e){
            e.preventDefault();
            let url = $(this).attr("href");
            Swal.fire({
                title: "¿Eliminar registro?",
                text: "Esta acción no se puede deshacer.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Sí, eliminar",
                cancelButtonText: "Cancelar",
                confirmButtonColor: "#7a1f3d"
            }).then((result)=>{
                if(result.isConfirmed){ window.location = url; }
            });
        });
    });
    </script>

    <c:if test="${editar}">
    <script>
    document.addEventListener("DOMContentLoaded", function(){
        var modal = new bootstrap.Modal(document.getElementById("modalPersonal"));
        modal.show();
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
    <c:if test="${not empty mensaje}">
    <script>
    Swal.fire({
        icon: 'success',
        title: 'Correcto',
        text: '${mensaje}',
        timer: 2000,
        showConfirmButton: false
    });
    </script>
    </c:if>

</body>
</html>