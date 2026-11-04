// Configuração personalizada do Tailwind CSS
tailwind.config = {
    theme: {
        extend: {
            colors: {
                brand: {
                    50: '#f4f6f4',
                    100: '#e5ebe6',
                    800: '#193629',
                    900: '#244638',
                },
                gold: {
                    400: '#c5a059',
                    500: '#a58b50',
                    600: '#8c733e',
                }
            }
        }
    }
};

// Controle do menu mobile
document.addEventListener("DOMContentLoaded", function () {
    const btn = document.getElementById("mobile-menu-btn");
    const sidebar = document.getElementById("sidebar");

    btn.addEventListener("click", function () {
        sidebar.classList.toggle("hidden");
    });
});
