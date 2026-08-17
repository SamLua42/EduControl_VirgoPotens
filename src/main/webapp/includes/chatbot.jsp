<%-- Chatbot de ayuda - EduControl --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="ecChatToggle" class="ec-chat-toggle">
    <i class="bi bi-chat-dots-fill"></i>
</div>

<div id="ecChatWindow" class="ec-chat-window d-none">
    <div class="ec-chat-header">
        <div class="d-flex align-items-center">
            <i class="bi bi-mortarboard-fill ec-chat-logo"></i>
            <div class="ms-2">
                <div class="ec-chat-title">Asistente EduControl</div>
                <div class="ec-chat-subtitle"><span class="ec-dot-online"></span> En línea</div>
            </div>
        </div>
        <button id="ecChatClose" class="ec-chat-close"><i class="bi bi-x-lg"></i></button>
    </div>

    <div id="ecChatBody" class="ec-chat-body">
        <div class="ec-msg ec-msg-bot">
            ¡Hola, ${sessionScope.usuarioLogueado.nombre != null ? sessionScope.usuarioLogueado.nombre : "bienvenido(a)"}! 👋
            Soy el asistente de EduControl. ¿En qué te puedo ayudar hoy?
        </div>
    </div>

    <div id="ecChatQuickReplies" class="ec-chat-quick">
        <button class="ec-quick-btn" data-key="marcar">¿Cómo marco mi asistencia?</button>
        <button class="ec-quick-btn" data-key="boleta">¿Cómo veo mi boleta de pago?</button>
        <button class="ec-quick-btn" data-key="planilla">¿Cuándo se procesa la planilla?</button>
        <button class="ec-quick-btn" data-key="tardanza">¿Qué pasa si llego tarde?</button>
        <button class="ec-quick-btn" data-key="clave">Olvidé mi contraseña</button>
        <button class="ec-quick-btn" data-key="roles">¿Qué puede hacer cada rol?</button>
    </div>

    <div class="ec-chat-input">
        <input type="text" id="ecChatInput" placeholder="Escribe tu pregunta...">
        <button id="ecChatSend"><i class="bi bi-send-fill"></i></button>
    </div>
</div>

<style>
.ec-chat-toggle {
    position: fixed; bottom: 24px; right: 24px; width: 58px; height: 58px;
    border-radius: 50%;
    background: linear-gradient(135deg, var(--ec-vino), var(--ec-vino-oscuro));
    color: #fff; display: flex; align-items: center; justify-content: center;
    font-size: 24px; box-shadow: 0 6px 18px rgba(122, 31, 61, 0.45);
    cursor: pointer; z-index: 1050; transition: transform .2s ease;
}
.ec-chat-toggle:hover { transform: scale(1.08); }

.ec-chat-window {
    position: fixed; bottom: 96px; right: 24px; width: 340px; max-height: 480px;
    background: #fff; border-radius: 16px; box-shadow: 0 10px 40px rgba(0,0,0,0.25);
    display: flex; flex-direction: column; overflow: hidden; z-index: 1050; font-family: inherit;
}

.ec-chat-header {
    background: linear-gradient(135deg, var(--ec-azul), var(--ec-azul-claro));
    color: #fff; padding: 14px 16px; display: flex; justify-content: space-between; align-items: center;
}
.ec-chat-logo { font-size: 22px; color: var(--ec-dorado); }
.ec-chat-title { font-weight: 600; font-size: 14px; }
.ec-chat-subtitle { font-size: 11px; opacity: .85; }
.ec-dot-online { display: inline-block; width: 7px; height: 7px; border-radius: 50%; background: #4ade80; margin-right: 4px; }
.ec-chat-close { background: none; border: none; color: #fff; opacity: .8; cursor: pointer; }
.ec-chat-close:hover { opacity: 1; }

.ec-chat-body {
    flex: 1; overflow-y: auto; padding: 14px; background: #f7f5f2;
    display: flex; flex-direction: column; gap: 10px; max-height: 260px;
}
.ec-msg { max-width: 85%; padding: 9px 12px; border-radius: 12px; font-size: 13px; line-height: 1.4; }
.ec-msg-bot { background: #fff; border: 1px solid #eee; border-bottom-left-radius: 2px; align-self: flex-start; color: #333; }
.ec-msg-user { background: linear-gradient(135deg, var(--ec-vino), var(--ec-vino-oscuro)); color: #fff; border-bottom-right-radius: 2px; align-self: flex-end; }

.ec-chat-quick { padding: 8px 12px; display: flex; flex-wrap: wrap; gap: 6px; border-top: 1px solid #eee; background: #fff; }
.ec-quick-btn {
    border: 1px solid var(--ec-dorado); color: var(--ec-vino-oscuro); background: #fdf8ec;
    border-radius: 20px; padding: 5px 10px; font-size: 11.5px; cursor: pointer; transition: all .15s ease;
}
.ec-quick-btn:hover { background: var(--ec-dorado); color: #fff; }

.ec-chat-input { display: flex; border-top: 1px solid #eee; padding: 8px; gap: 6px; background: #fff; }
.ec-chat-input input { flex: 1; border: 1px solid #ddd; border-radius: 20px; padding: 7px 12px; font-size: 13px; outline: none; }
.ec-chat-input input:focus { border-color: var(--ec-vino); }
.ec-chat-input button { background: var(--ec-vino); color: #fff; border: none; width: 34px; height: 34px; border-radius: 50%; cursor: pointer; }
.ec-chat-input button:hover { background: var(--ec-vino-oscuro); }
</style>

<script>
(function () {
    const respuestas = {
        marcar: "Para marcar tu asistencia: entra a <b>Asistencia</b> en el menú lateral y presiona <b>Marcar Asistencia</b>. El sistema registra la hora automáticamente y la clasifica como Puntual, Tardanza o Falta según tu horario.",
        boleta: "Ve a <b>Reportes</b>, elige el mes y año, y presiona <b>Generar PDF</b>. Solo puedes ver la boleta de un mes cuya planilla ya fue <b>procesada</b> por el Administrador.",
        planilla: "La planilla se procesa una vez al mes desde el módulo <b>Planilla</b> (solo el Administrador puede hacerlo). Ahí se calculan los pagos de todo el personal según la asistencia registrada.",
        tardanza: "Si marcas tu entrada más de <b>10 minutos</b> después de tu horario esperado, el sistema lo clasifica automáticamente como <b>Tardanza</b>, y se aplica el descuento configurado para tu tipo de personal.",
        clave: "Si olvidaste tu contraseña, comunícate con el <b>Administrador</b> (Secretaría) para que te la restablezca desde el módulo de Personal.",
        roles: "EduControl tiene 3 roles: <br>• <b>Trabajador</b>: marca su asistencia y ve su propia información.<br>• <b>Administrador</b>: gestiona personal, pagos y procesa planilla.<br>• <b>Director</b>: consulta todo, solo lectura.",
        default: "Todavía no tengo una respuesta exacta para eso 🤔. Intenta con una de las preguntas rápidas de abajo."
    };

    const keywordMap = [
        [/marcar|asisten|entrada|salida/i, "marcar"],
        [/boleta|recibo|pago.*ver|ver.*pago/i, "boleta"],
        [/planilla|procesa/i, "planilla"],
        [/tarde|tardanza|falta/i, "tardanza"],
        [/contrase|clave|password|olvid/i, "clave"],
        [/rol|permiso|puede hacer|administrador|director|trabajador/i, "roles"],
    ];

    const toggle = document.getElementById("ecChatToggle");
    const win = document.getElementById("ecChatWindow");
    const closeBtn = document.getElementById("ecChatClose");
    const body = document.getElementById("ecChatBody");
    const input = document.getElementById("ecChatInput");
    const sendBtn = document.getElementById("ecChatSend");
    const quickBtns = document.querySelectorAll(".ec-quick-btn");

    toggle.addEventListener("click", () => win.classList.toggle("d-none"));
    closeBtn.addEventListener("click", () => win.classList.add("d-none"));

    function addMsg(text, tipo) {
        const div = document.createElement("div");
        div.className = "ec-msg " + (tipo === "user" ? "ec-msg-user" : "ec-msg-bot");
        div.innerHTML = text;
        body.appendChild(div);
        body.scrollTop = body.scrollHeight;
    }

    function responder(pregunta) {
        addMsg(pregunta, "user");
        let key = "default";
        for (const [regex, k] of keywordMap) {
            if (regex.test(pregunta)) { key = k; break; }
        }
        setTimeout(() => addMsg(respuestas[key], "bot"), 400);
    }

    quickBtns.forEach(btn => btn.addEventListener("click", () => responder(btn.textContent)));

    sendBtn.addEventListener("click", () => {
        const val = input.value.trim();
        if (!val) return;
        responder(val);
        input.value = "";
    });

    input.addEventListener("keypress", (e) => { if (e.key === "Enter") sendBtn.click(); });
})();
</script>