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

