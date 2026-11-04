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

// Lógica da tela de importação
document.addEventListener("DOMContentLoaded", function () {

    const formulario = document.getElementById("formImportacao");
    const tipo = document.getElementById("tipo");
    const avisoTipo = document.getElementById("avisoTipo");
    const textoAvisoTipo = document.getElementById("textoAvisoTipo");

    function atualizarDestino() {
        if (tipo.value === "apolices") {
            formulario.action = formulario.dataset.apolicesAction;
        } else {
            formulario.action = formulario.dataset.contasAction;
        }

        avisoTipo.classList.add("hidden");
    }

    tipo.addEventListener("change", atualizarDestino);

    formulario.addEventListener("submit", function (evento) {
        if (!tipo.value) {
            evento.preventDefault();
            textoAvisoTipo.textContent = "Selecione o tipo de importação.";
            avisoTipo.classList.remove("hidden");
            return;
        }

        if (tipo.value !== "contas" && tipo.value !== "apolices") {
            evento.preventDefault();
            textoAvisoTipo.textContent = "Tipo de importação inválido.";
            avisoTipo.classList.remove("hidden");
            return;
        }

        atualizarDestino();
    });

    // Toggle do menu mobile
    const btn = document.getElementById("mobile-menu-btn");
    const sidebar = document.getElementById("sidebar");

    btn.addEventListener("click", function () {
        sidebar.classList.toggle("hidden");
    });

});

