<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EduControl - Iniciar Sesión</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
<style>
    body { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #16213e, #1e2d52); font-family: 'Segoe UI', sans-serif; }
    .login-card { display: flex; width: 780px; max-width: 95%; border-radius: 18px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.35); }
    .login-brand { flex: 1; background: linear-gradient(160deg, #7a1f3d, #4a1030); color: #fff; padding: 40px 32px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center; }
    .login-brand i { font-size: 56px; color: #c9a24b; margin-bottom: 12px; }
    .login-brand h2 { font-weight: 700; margin-bottom: 4px; }
    .login-brand p { font-size: 13px; opacity: .85; letter-spacing: 1px; }
    .login-brand .frase { margin-top: 28px; font-style: italic; font-size: 13px; border-top: 1px solid rgba(255,255,255,0.25); padding-top: 16px; opacity: .9; }
    .login-form { flex: 1; background: #fff; padding: 48px 40px; display: flex; flex-direction: column; justify-content: center; }
    .login-form h3 { font-weight: 700; color: #4a1030; margin-bottom: 4px; }
    .login-form p.subtitulo { color: #888; font-size: 13px; margin-bottom: 24px; }
    .form-control:focus { border-color: #7a1f3d; box-shadow: 0 0 0 .2rem rgba(122,31,61,.15); }
    .btn-login { background: linear-gradient(135deg, #7a1f3d, #4a1030); color: #fff; border: none; padding: 10px; font-weight: 600; border-radius: 8px; }
    .btn-login:hover { opacity: .92; color: #fff; }
    .input-group-text { background: #f7f5f2; border-right: none; }
    .form-control { border-left: none; }
</style>
</head>
<body>
    <div class="login-card">
        <div class="login-brand">
            <i class="bi bi-shield-fill-check"></i>
            <h2>EduControl</h2>
            <p>I.E. VIRGO POTENS<br>VIRGEN PODEROSA</p>
            <div class="frase">"Educamos no para el éxito,<br>sino para la vida con propósito."</div>
        </div>
        <div class="login-form">
            <h3>Bienvenido(a)</h3>
            <p class="subtitulo">Ingresa tus credenciales para continuar</p>
            <c:if test="${not empty error}">
                <div class="alert alert-danger py-2">
                    <i class="bi bi-exclamation-circle-fill"></i> ${error}
                </div>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/LoginServlet">
            <form method="post" action="${pageContext.request.contextPath}/LoginServlet">
    			<input type="hidden" name="accion" value="login">
                <div class="mb-3">
                    <label class="form-label">Usuario</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
                        <input type="text" class="form-control" name="usuario" placeholder="Ingresa tu usuario" required autofocus>
                    </div>
                </div>
                <div class="mb-4">
                    <label class="form-label">Contraseña</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                        <input type="password" class="form-control" name="contrasena" placeholder="Ingresa tu contraseña" required>
                    </div>
                </div>
                <button type="submit" class="btn btn-login w-100"><i class="bi bi-box-arrow-in-right"></i> Ingresar</button>
            </form>
            <p class="text-center text-muted mt-4" style="font-size: 12px;">© 2026 EduControl — I.E. Virgo Potens</p>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>