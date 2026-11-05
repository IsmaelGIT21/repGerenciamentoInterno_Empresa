tailwind.config = {
    theme: {
        extend: {
            colors: {
                brand: {
                    50: '#f0f4f8',   // Fundo suave para destaques e cartões
                    100: '#d9e2ec',  // Bordas e badges suaves
                    800: '#1b365d',  // Tom de passagem/hover
                    900: '#00205b',  // Azul Marinho Oficial da Sperafico (Torres e Moldura)
                },
                gold: {
                    400: '#d4af37',
                    500: '#c2a15f',  // Tom dourado/champanhe de contraste para ícones e detalhes
                    600: '#9e7f3e',
                }
            }
        }
    }
}

// Controle do menu mobile
document.addEventListener("DOMContentLoaded", function () {

    const btn = document.getElementById("mobile-menu-btn");
    const sidebar = document.getElementById("sidebar");

    btn.addEventListener("click", function () {
        sidebar.classList.toggle("hidden");
    });

});
