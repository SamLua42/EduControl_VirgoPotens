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
    body { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, var(--ec-azul-claro), var(--ec-azul)); font-family: 'Segoe UI', sans-serif; margin: 0; }

    .login-wrapper { display: flex; width: 900px; max-width: 95%; min-height: 560px; border-radius: 22px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.35); }

    /* ===== PANEL IZQUIERDO (marca) ===== */
    .login-brand { position: relative; overflow: hidden; flex: 1; background: linear-gradient(160deg, var(--ec-vino) 0%, var(--ec-vino-oscuro) 45%, var(--ec-azul) 100%); color: #fff; padding: 40px 36px; display: flex; flex-direction: column; justify-content: space-between; align-items: center; text-align: center; }

    .login-brand::before { content: ''; position: absolute; top: -20px; right: -20px; width: 140px; height: 140px; background-image: radial-gradient(rgba(255,255,255,0.18) 1.5px, transparent 1.5px); background-size: 14px 14px; pointer-events: none; opacity: .5; }

    .brand-circle { position: absolute; border-radius: 50%; border: 1px solid rgba(255,255,255,0.15); pointer-events: none; }
    .brand-circle.c1 { width: 46px; height: 46px; top: 42%; left: 10%; }
    .brand-circle.c2 { width: 26px; height: 26px; bottom: 22%; left: 6%; }

    .login-brand-top { position: relative; z-index: 1; display: flex; flex-direction: column; align-items: center; }

    .login-logo { width: 118px; height: 118px; border-radius: 50%; background: rgba(255,255,255,0.08); border: 2px solid rgba(201,162,75,0.5); display: flex; align-items: center; justify-content: center; margin-bottom: 16px; overflow: hidden; }
    .login-logo img { width: 100%; height: 100%; object-fit: contain; border-radius: 50%; }

    .login-brand h2 { font-weight: 800; margin-bottom: 2px; font-size: 26px; }
    .login-brand .sub-inst { font-size: 13px; opacity: .9; letter-spacing: 1px; font-weight: 600; margin-bottom: 0; }

    .brand-divider { display: flex; align-items: center; gap: 10px; width: 100%; max-width: 260px; margin-top: 26px; }
    .brand-divider .line { flex: 1; height: 1px; background: rgba(201,162,75,0.5); }
    .brand-divider i { color: #c9a24b; font-size: 14px; }

    .login-brand .frase { margin-top: 14px; font-size: 15px; letter-spacing: .5px; opacity: .9; text-transform: uppercase; font-weight: 700; line-height: 1.5; }

    .brand-building { position: relative; z-index: 1; width: 100%; opacity: .35; margin: 18px 0 -10px; }

    .login-brand-trust { position: relative; z-index: 1; display: flex; justify-content: space-around; width: 100%; padding-top: 16px; border-top: 1px solid rgba(255,255,255,0.12); }
    .login-brand-trust .item { display: flex; flex-direction: column; align-items: center; gap: 5px; font-size: 10.5px; color: rgba(255,255,255,0.85); font-weight: 600; letter-spacing: .5px; text-transform: uppercase; }
    .login-brand-trust .item i { font-size: 19px; color: #c9a24b; }

    /* ===== PANEL DERECHO (form) ===== */
    .login-form { position: relative; overflow: hidden; flex: 1; background: #fff; padding: 48px 44px; display: flex; flex-direction: column; justify-content: center; }
    .login-form h3 { font-weight: 800; margin-bottom: 4px; font-size: 26px; }
    .login-form h3 .t1 { color: var(--ec-azul); }
    .login-form h3 .t2 { color: var(--ec-vino); }
    .login-form p.subtitulo { color: #888; font-size: 13px; margin-bottom: 26px; }

    .form-label { font-weight: 600; font-size: 13px; color: #3a2233; }

    .ec-input-pill { display: flex; align-items: center; gap: 10px; border: 1.5px solid #e4e2e0; border-radius: 30px; padding: 12px 20px; transition: border-color .2s, box-shadow .2s; }
    .ec-input-pill:focus-within { border-color: var(--ec-vino); box-shadow: 0 0 0 3px rgba(163,29,29,.10); }
    .ec-input-pill i { color: var(--ec-vino); font-size: 16px; }
    .ec-input-pill i.toggle-ojo { cursor: pointer; color: #999; margin-left: auto; }
    .ec-input-pill input { border: none; outline: none; flex: 1; font-size: 14px; background: transparent; }

    .btn-login { background: linear-gradient(135deg, var(--ec-vino), var(--ec-azul)); color: #fff; border: none; padding: 14px; font-weight: 700; font-size: 15px; border-radius: 30px; }
    .btn-login:hover { opacity: .92; color: #fff; }

    .form-check-label { font-size: 13px; }
    .link-clave { color: var(--ec-vino); text-decoration: none; font-weight: 600; font-size: 13px; }
    .link-clave:hover { color: var(--ec-vino-oscuro); text-decoration: underline; }

    .login-watermark { position: absolute; bottom: -18px; right: -18px; font-size: 120px; color: var(--ec-vino); opacity: 0.05; pointer-events: none; }

    @media (max-width: 720px) {
        .login-wrapper { flex-direction: column; width: 420px; }
        .brand-building { display: none; }
    }
</style>
</head>
<body>

    <div class="login-wrapper">

        <div class="login-brand">

            <div class="brand-circle c1"></div>
            <div class="brand-circle c2"></div>

            <div class="login-brand-top">
                <div class="login-logo">
                    <img src="${pageContext.request.contextPath}/img/Insignia1.png" alt="Escudo Virgo Potens">
                </div>
                <h2>EduControl</h2>
                <p class="sub-inst">I.E. VIRGO POTENS</p>

                <div class="brand-divider">
                    <span class="line"></span>
                    <i class="bi bi-feather"></i>
                    <span class="line"></span>
                </div>

                <div class="frase">Saber más<br>para servir mejor</div>
            </div>

            <svg class="brand-building" viewBox="0 0 300 90" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="10" y="30" width="280" height="55" stroke="#c9a24b" stroke-width="1"/>
                <rect x="30" y="45" width="14" height="18" stroke="#c9a24b" stroke-width="1"/>
                <rect x="60" y="45" width="14" height="18" stroke="#c9a24b" stroke-width="1"/>
                <rect x="90" y="45" width="14" height="18" stroke="#c9a24b" stroke-width="1"/>
                <rect x="196" y="45" width="14" height="18" stroke="#c9a24b" stroke-width="1"/>
                <rect x="226" y="45" width="14" height="18" stroke="#c9a24b" stroke-width="1"/>
                <rect x="256" y="45" width="14" height="18" stroke="#c9a24b" stroke-width="1"/>
                <rect x="130" y="20" width="40" height="65" stroke="#c9a24b" stroke-width="1"/>
                <path d="M130 20 L150 5 L170 20" stroke="#c9a24b" stroke-width="1"/>
                <rect x="144" y="60" width="12" height="25" stroke="#c9a24b" stroke-width="1"/>
                <line x1="0" y1="85" x2="300" y2="85" stroke="#c9a24b" stroke-width="1"/>
            </svg>

            <div class="login-brand-trust">
                <div class="item"><i class="bi bi-shield-lock-fill"></i><span>Seguridad</span></div>
                <div class="item"><i class="bi bi-award-fill"></i><span>Confianza</span></div>
                <div class="item"><i class="bi bi-heart-fill"></i><span>Compromiso</span></div>
            </div>

        </div>

        <div class="login-form">
            <i class="bi bi-shield-fill-check login-watermark"></i>

            <h3><span class="t1">Iniciar</span> <span class="t2">sesión</span></h3>
            <p class="subtitulo">Ingresa tus credenciales para acceder</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger py-2">
                    <i class="bi bi-exclamation-circle-fill"></i> ${error}
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/LoginServlet">
                <input type="hidden" name="accion" value="login">

                <div class="mb-3">
                    <label class="form-label">Usuario</label>
                    <div class="ec-input-pill">
                        <i class="bi bi-person-fill"></i>
                        <input type="text" name="usuario" placeholder="Ingresa tu usuario" required autofocus>
                    </div>
                </div>

                <div class="mb-2">
                    <label class="form-label">Contraseña</label>
                    <div class="ec-input-pill">
                        <i class="bi bi-lock-fill"></i>
                        <input type="password" id="contrasenaInput" name="contrasena" placeholder="Ingresa tu contraseña" required>
                        <i class="bi bi-eye-fill toggle-ojo" id="iconoOjo" onclick="togglePassword()"></i>
                    </div>
                </div>

                <div class="d-flex justify-content-between align-items-center mb-4 mt-3">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="recordarme" name="recordarme">
                        <label class="form-check-label text-muted" for="recordarme">Recuérdame</label>
                    </div>
                    <a href="javascript:void(0)" class="link-clave" onclick="mostrarInfoClave()">¿Olvidaste tu contraseña?</a>
                </div>

                <button type="submit" class="btn btn-login w-100"><i class="bi bi-box-arrow-in-right"></i> Iniciar sesión</button>
            </form>

            <p class="text-center text-muted mt-4 mb-0" style="font-size: 12px;">EduControl - I.E. Virgo Potens<br>© 2026 Todos los derechos reservados.</p>
        </div>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        function togglePassword() {
            var input = document.getElementById('contrasenaInput');
            var icono = document.getElementById('iconoOjo');
            if (input.type === 'password') {
                input.type = 'text';
                icono.classList.remove('bi-eye-fill');
                icono.classList.add('bi-eye-slash-fill');
            } else {
                input.type = 'password';
                icono.classList.remove('bi-eye-slash-fill');
                icono.classList.add('bi-eye-fill');
            }
        }

        function mostrarInfoClave() {
            Swal.fire({
                icon: 'info',
                title: 'Recuperar contraseña',
                text: 'Por seguridad, la recuperación de contraseña la gestiona el Administrador del sistema. Comunícate con él para restablecerla.',
                confirmButtonColor: '#A31D1D'
            });
        }
    </script>
</body>
</html>