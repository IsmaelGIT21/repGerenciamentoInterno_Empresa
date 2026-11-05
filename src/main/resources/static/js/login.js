// Controle de visibilidade da senha
// Configuração de cores do Tailwind CSS para a Sperafico Incorporadora
tailwind.config = {
    theme: {
        extend: {
            colors: {
                brand: {
                    50: '#f0f4f8',   // Fundo suave de cards/alertas
                    100: '#d9e2ec',  // Bordas leves
                    800: '#1b365d',  // Tom de hover para botões
                    900: '#00205b',  // Azul Marinho Oficial da Sperafico (Header, Sidebar e Botões)
                },
                gold: {
                    400: '#d4af37',  // Dourado/Champanhe para detalhes
                    500: '#c2a15f',
                    600: '#9e7f3e',
                }
            }
        }
    }
};

document.addEventListener("DOMContentLoaded", function () {
    const passwordInput = document.getElementById("password");
    const toggleButton = document.getElementById("togglePassword");

    if (!passwordInput || !toggleButton) {
        return;
    }

    toggleButton.addEventListener("click", function () {
        const mostrarSenha = passwordInput.type === "password";

        passwordInput.type = mostrarSenha ? "text" : "password";
        toggleButton.textContent = mostrarSenha ? "Ocultar" : "Mostrar";
    });
});