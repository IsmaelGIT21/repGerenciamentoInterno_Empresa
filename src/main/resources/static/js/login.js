// Controle de visibilidade da senha
document.addEventListener("DOMContentLoaded", function () {
    const passwordInput = document.getElementById("password");
    const toggleButton = document.getElementById("togglePassword");

    toggleButton.addEventListener("click", function () {
        const mostrarSenha = passwordInput.type === "password";
        passwordInput.type = mostrarSenha ? "text" : "password";
        toggleButton.textContent = mostrarSenha ? "Ocultar" : "Mostrar";
    });
});
