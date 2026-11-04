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

// Controle do campo de data de pagamento
document.addEventListener("DOMContentLoaded", function () {

    const statusConta = document.getElementById("status");
    const campoDataPagamento = document.getElementById("campoDataPagamento");
    const dataPagamento = document.getElementById("dataPagamento");

    function atualizarCampoDataPagamento() {
        const contaPaga = statusConta.value === "PAGA";

        campoDataPagamento.style.display = contaPaga ? "block" : "none";

        if (!contaPaga) {
            dataPagamento.value = "";
        }
    }

    statusConta.addEventListener("change", atualizarCampoDataPagamento);

    atualizarCampoDataPagamento();

});
