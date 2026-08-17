<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EduControl - Notificaciones</title>

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

                <div class="mb-4">
                    <h2 class="fw-bold"><i class="bi bi-bell-fill"></i> Notificaciones</h2>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/DashboardServlet">Inicio</a>
                            </li>
                            <li class="breadcrumb-item active">Notificaciones</li>
                        </ol>
                    </nav>
                </div>

                <div class="card shadow">
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty notificaciones}">
                                <c:forEach var="n" items="${notificaciones}">
                                    <div class="d-flex align-items-start gap-3 py-3" style="border-bottom: 1px solid #eee;">
                                        <i class="bi bi-exclamation-circle-fill" style="color: var(--ec-dorado); font-size: 20px;"></i>
                                        <div>${n}</div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted text-center py-4 mb-0">
                                    <i class="bi bi-check-circle"></i> No hay notificaciones pendientes por ahora.
                                </p>
                            </c:otherwise>
                        </c:choose>
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