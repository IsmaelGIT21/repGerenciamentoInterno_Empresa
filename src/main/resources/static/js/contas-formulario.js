// Configuração personalizada do Tailwind CSS
tailwind.config = {
    theme: {
        extend: {
            colors: {
                brand: {
                    50: '#f0f3f8',   // Fundo muito suave/áreas destacadas de tabelas
                    100: '#dce3ef',  // Bordas leves e fundos de badges
                    800: '#2C4268',  // Azul Aço Escuro (hover de botões e destaques secundários)
                    900: '#1A2B4C',  // Navy Real / Azul Marinho Clássico (Header, Sidebar e Botões Principais)
                },
                gold: {
                    400: '#d4b36e',  // Dourado Iluminado (hover de detalhes)
                    500: '#C5A059',  // Dourado Champagne Clássico (Ícones, badges e accents)
                    600: '#9e7d3b',  // Dourado Escuro (textos pequenos e detalhes de contraste)
                }
            }
        }
    }
}

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
