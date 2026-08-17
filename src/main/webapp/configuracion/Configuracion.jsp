<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<%
    dao.ConfiguracionInstitucionDAO configInstDAO = new dao.ConfiguracionInstitucionDAO();
    entidad.ConfiguracionInstitucion configInst = configInstDAO.obtener();
    request.setAttribute("configInst", configInst);
%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EduControl - Configuracion</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

    <div class="d-flex" style="min-height: 100vh;">

        <jsp:include page="/includes/sidebar.jsp"/>

        <div class="d-flex flex-column flex-grow-1">

            <jsp:include page="/includes/header.jsp"/>

            <main class="p-4 contenido flex-grow-1">

                <c:set var="miRol" value="${fn:toUpperCase(sessionScope.usuarioLogueado.rol)}"/>

                <div class="mb-4">
                    <h2 class="fw-bold"><i class="bi bi-gear-fill"></i> Configuracion</h2>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/DashboardServlet">Inicio</a>
                            </li>
                            <li class="breadcrumb-item active">Configuracion</li>
                        </ol>
                    </nav>
                </div>

                <div class="card shadow">
                    <div class="card-header bg-white">
                        <ul class="nav nav-tabs card-header-tabs" id="configTabs" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active" id="btn-perfil" data-bs-toggle="tab" data-bs-target="#tab-perfil" type="button" role="tab">
                                    <i class="bi bi-person-fill"></i> Mi Perfil
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="btn-clave" data-bs-toggle="tab" data-bs-target="#tab-clave" type="button" role="tab">
                                    <i class="bi bi-key-fill"></i> Cambiar Contraseña
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="btn-preferencias" data-bs-toggle="tab" data-bs-target="#tab-preferencias" type="button" role="tab">
                                    <i class="bi bi-sliders"></i> Preferencias
                                </button>
                            </li>
                            <c:if test="${miRol == 'ADMINISTRADOR'}">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="btn-sistema" data-bs-toggle="tab" data-bs-target="#tab-sistema" type="button" role="tab">
                                    <i class="bi bi-building-gear"></i> Administración del Sistema
                                </button>
                            </li>
                            </c:if>
                        </ul>
                    </div>

                    <div class="card-body">
                        <div class="tab-content" id="configTabsContent">

                            <%-- MI PERFIL --%>
                            <div class="tab-pane fade show active" id="tab-perfil" role="tabpanel">
                                <table class="table table-borderless mb-0">
                                    <tr>
                                        <td class="text-muted" style="width: 40%;">Nombre completo</td>
                                        <td class="fw-bold">${sessionScope.usuarioLogueado.nombre} ${sessionScope.usuarioLogueado.apellido}</td>
                                    </tr>
                                    <tr>
                                        <td class="text-muted">DNI</td>
                                        <td>${sessionScope.usuarioLogueado.dni}</td>
                                    </tr>
                                    <tr>
                                        <td class="text-muted">Cargo</td>
                                        <td>${sessionScope.usuarioLogueado.cargo}</td>
                                    </tr>
                                    <tr>
                                        <td class="text-muted">Tipo de Personal</td>
                                        <td>${sessionScope.usuarioLogueado.tipoPersonal}</td>
                                    </tr>
                                    <tr>
                                        <td class="text-muted">Usuario</td>
                                        <td>${sessionScope.usuarioLogueado.usuario}</td>
                                    </tr>
                                    <tr>
                                        <td class="text-muted">Rol en el sistema</td>
                                        <td>
                                            <span class="badge" style="background: var(--ec-vino);">${sessionScope.usuarioLogueado.rol}</span>
                                        </td>
                                    </tr>
                                </table>
                                <p class="text-muted mt-3 mb-0" style="font-size: 12px;">
                                    <i class="bi bi-info-circle"></i> Para modificar estos datos, contacta al Administrador del sistema.
                                </p>
                            </div>

                            <%-- CAMBIAR CONTRASEÑA --%>
                            <div class="tab-pane fade" id="tab-clave" role="tabpanel">
                                <form method="post" action="${pageContext.request.contextPath}/PersonalServlet" style="max-width: 400px;">
                                    <input type="hidden" name="accion" value="cambiarClave">

                                    <div class="mb-3">
                                        <label class="form-label">Contraseña actual</label>
                                        <input type="password" class="form-control" name="claveActual" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">Contraseña nueva</label>
                                        <input type="password" class="form-control" name="claveNueva" required minlength="4">
                                    </div>

                                    <button type="submit" class="btn btn-vino w-100">
                                        <i class="bi bi-check-circle"></i> Actualizar Contraseña
                                    </button>
                                </form>
                            </div>

                            <%-- PREFERENCIAS --%>
                            <div class="tab-pane fade" id="tab-preferencias" role="tabpanel">
                                <p class="text-muted mb-0" style="font-size: 13px;">
                                    Las preferencias de apariencia (modo claro/oscuro) y notificaciones estaran disponibles en una proxima actualizacion del sistema.
                                </p>
                            </div>

                            <%-- ADMINISTRACION DEL SISTEMA: SOLO ADMINISTRADOR --%>
                            <c:if test="${miRol == 'ADMINISTRADOR'}">
                            <div class="tab-pane fade" id="tab-sistema" role="tabpanel">

                                <h6 class="fw-bold mb-3" style="color: var(--ec-vino);">Datos informativos de la IE</h6>
                                <table class="table table-borderless mb-4" style="font-size: 14px;">
                                    <tr><td class="text-muted" style="width:35%;">Nombre de la IE</td><td class="fw-bold">VIRGO POTENS</td></tr>
                                    <tr><td class="text-muted">Código de la IE</td><td>25791428</td></tr>
                                    <tr><td class="text-muted">Nombre de la DRE o UGEL</td><td>UGEL 03 Cercado</td></tr>
                                    <tr><td class="text-muted">Tipo de Gestión</td><td>Pública de gestión privada</td></tr>
                                    <tr><td class="text-muted">Código modular</td><td>0336545</td></tr>
                                    <tr><td class="text-muted">Anexo</td><td>0</td></tr>
                                    <tr><td class="text-muted">Estado</td><td><span class="badge bg-success">Activo</span></td></tr>
                                    <tr><td class="text-muted">Dirección completa</td><td>Jirón Puno 1731, Barrios Altos, Lima Cercado — Distrito de Lima, Provincia de Lima, Departamento de Lima</td></tr>
                                </table>
                                <p class="text-muted mb-4" style="font-size:12px;">
                                    <i class="bi bi-info-circle"></i> Estos datos son oficiales (ficha ESCALE) y no se pueden editar desde el sistema.
                                </p>

                                <hr>

                                <h6 class="fw-bold mb-3 mt-4" style="color: var(--ec-vino);">Datos editables</h6>
                                <form method="post" action="${pageContext.request.contextPath}/ConfiguracionInstitucionServlet">
                                    <div class="row g-3">
                                        <div class="col-md-6">
                                            <label class="form-label">Logo (ruta del archivo)</label>
                                            <input type="text" class="form-control" name="logoRuta" value="${configInst.logoRuta}">
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Dependencia</label>
                                            <input type="text" class="form-control" name="dependencia" value="${configInst.dependencia}">
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Teléfono</label>
                                            <input type="text" class="form-control" name="telefono" value="${configInst.telefono}">
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Página web</label>
                                            <input type="text" class="form-control" name="paginaWeb" value="${configInst.paginaWeb}" placeholder="Aun no se tiene">
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Forma</label>
                                            <input type="text" class="form-control" name="forma" value="${configInst.forma}">
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Director(a)</label>
                                            <input type="text" class="form-control" name="director" value="${configInst.director}">
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Nivel/Modalidad</label>
                                            <input type="text" class="form-control" name="nivelModalidad" value="${configInst.nivelModalidad}">
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Género</label>
                                            <input type="text" class="form-control" name="genero" value="${configInst.genero}">
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Turno</label>
                                            <input type="text" class="form-control" name="turno" value="${configInst.turno}">
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Tolerancia de tardanza (minutos)</label>
                                            <input type="number" class="form-control" name="toleranciaTardanzaMinutos" value="${configInst.toleranciaTardanzaMinutos}" min="0" max="120" required>
                                            <small class="text-muted" style="font-size:11px;">Minutos despues de la hora esperada que aun cuentan como "Puntual".</small>
                                        </div>
                                    </div>
                                    <button type="submit" class="btn btn-vino mt-4">
                                        <i class="bi bi-check-circle"></i> Guardar cambios
                                    </button>
                                </form>
                            </div>
                            </c:if>

                        </div>
                    </div>
                </div>

            </main>

            <jsp:include page="/includes/footer.jsp"/>

        </div>

    </div>

    <jsp:include page="/includes/chatbot.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script>
    document.addEventListener('DOMContentLoaded', function () {
        var mapaHash = {
            '#perfil': 'btn-perfil',
            '#clave': 'btn-clave',
            '#preferencias': 'btn-preferencias',
            '#sistema': 'btn-sistema'
        };
        var idBoton = mapaHash[window.location.hash];
        if (idBoton) {
            var boton = document.getElementById(idBoton);
            if (boton) {
                new bootstrap.Tab(boton).show();
            }
        }
    });
    </script>

    <c:if test="${not empty mensajeConfig}">
    <script>
    Swal.fire({
        icon: 'success',
        title: 'Correcto',
        text: '${mensajeConfig}',
        timer: 2500,
        showConfirmButton: false
    });
    </script>
    </c:if>

    <c:if test="${not empty errorConfig}">
    <script>
    Swal.fire({
        icon: 'error',
        title: 'Ocurrio un error',
        text: '${errorConfig}'
    });
    </script>
    </c:if>

</body>
</html>