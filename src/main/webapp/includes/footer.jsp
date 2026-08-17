<%-- footer del sistema --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<footer class="footer">
    <div class="footer-left">
        2026 EduControl — I.E. Virgo Potens
    </div>
    <div class="footer-right">
        Versión 1.0
    </div>
</footer>

<script>
(function () {
    if (localStorage.getItem('ecTema') === 'oscuro') {
        document.body.classList.add('dark-mode');
    }
    document.addEventListener('DOMContentLoaded', function () {
        var btn = document.getElementById('btnDarkMode');
        if (!btn) return;
        function actualizarIcono() {
            var esOscuro = document.body.classList.contains('dark-mode');
            if (btn.classList.contains('dark-toggle-pill')) {
                btn.classList.toggle('activo', esOscuro);
                btn.title = esOscuro ? 'Modo claro' : 'Modo oscuro';
            } else {
                btn.classList.toggle('bi-moon', !esOscuro);
                btn.classList.toggle('bi-sun', esOscuro);
                btn.title = esOscuro ? 'Modo claro' : 'Modo oscuro';
            }
        }
        btn.addEventListener('click', function () {
            document.body.classList.toggle('dark-mode');
            localStorage.setItem('ecTema', document.body.classList.contains('dark-mode') ? 'oscuro' : 'claro');
            actualizarIcono();
        });
        actualizarIcono();
    });
})();
</script>