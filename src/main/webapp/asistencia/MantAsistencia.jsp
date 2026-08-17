<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EduControl - Asistencia</title>

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
                    <h2 class="fw-bold"><i class="bi bi-calendar-check-fill"></i> Control de Asistencia</h2>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/dashboard/Dashboard.jsp">Inicio</a>
                            </li>
                            <li class="breadcrumb-item">Procesos</li>
                            <li class="breadcrumb-item active">Asistencia</li>
                        </ol>
                    </nav>
                </div>

                <%-- TARJETA DE MARCAR ASISTENCIA (CU02) --%>
                <div class="card shadow mb-4">
                    <div class="card-body d-flex justify-content-between align-items-center flex-wrap gap-3">
                        <div>
                            <h5 class="mb-1"><i class="bi bi-clock-fill" style="color: var(--ec-vino);"></i> Marcar mi asistencia</h5>
                            <p class="text-muted mb-0" style="font-size: 13px;">
                                Se registrará con la hora actual del servidor. El sistema clasifica automáticamente si es Puntual o Tardanza.
                            </p>
                        </div>
                        <form method="post" action="${pageContext.request.contextPath}/AsistenciaServlet">
                            <input type="hidden" name="accion" value="marcar">
                            <button type="submit" class="btn btn-vino btn-lg">
                                <i class="bi bi-fingerprint"></i> Marcar Asistencia
                            </button>
                        </form>
                    </div>
                </div>

                <%-- TABLA DE HISTORIAL (CU04) --%>
                <div class="card shadow">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="bi bi-list-ul"></i> Historial de Asistencia</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="tablaAsistencia" class="table table-hover table-bordered align-middle nowrap" style="width:100%">
                                <thead class="table-dark">
                                    <tr>
                                        <th class="text-center">ID</th>
                                        <th>Personal</th>
                                        <th>Fecha</th>
                                        <th>Hora marcada</th>
                                        <th class="text-center">Estado</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="a" items="${listaAsistencia}">
                                    <c:if test="${sessionScope.usuarioLogueado.rol != 'TRABAJADOR' || a.idPersonal == sessionScope.usuarioLogueado.idPersonal}">
                                        <tr>
                                            <td class="text-center">${a.idAsistencia}</td>
                                            <td>${mapaPersonal[a.idPersonal].nombre} ${mapaPersonal[a.idPersonal].apellido}</td>
                                            <td><fmt:formatDate value="${a.fecha}" pattern="dd/MM/yyyy"/></td>
                                            <td>${a.horaMarcada}</td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${fn:toUpperCase(a.clasificacion) == 'PUNTUAL'}">
                                                        <span class="badge badge-puntual">Puntual</span>
                                                    </c:when>
                                                    <c:when test="${fn:toUpperCase(a.clasificacion) == 'TARDANZA'}">
                                                        <span class="badge badge-tardanza">Tardanza</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-falta">Falta</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                </tbody>
                            </table>
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
        $('#tablaAsistencia').DataTable({
            responsive: true,
            language: { url: 'https://cdn.datatables.net/plug-ins/2.3.3/i18n/es-ES.json' }
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