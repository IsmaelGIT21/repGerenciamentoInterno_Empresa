package br.com.gestao.empresas.config;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    public String tratarRecursoNaoEncontrado(
            NoResourceFoundException exception,
            HttpServletRequest request,
            Model model) {

        model.addAttribute("codigo", 404);
        model.addAttribute("titulo", "Página não encontrada");
        model.addAttribute(
                "mensagem",
                "O recurso solicitado não foi encontrado.");
        model.addAttribute("caminho", request.getRequestURI());

        return "erro/erro";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String tratarErroDeStatus(
            ResponseStatusException exception,
            HttpServletRequest request,
            Model model) {

        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());

        model.addAttribute("codigo", status.value());
        model.addAttribute("titulo", status.getReasonPhrase());
        model.addAttribute("mensagem", obterMensagem(status));
        model.addAttribute("caminho", request.getRequestURI());

        return "erro/erro";
    }

    @ExceptionHandler(Exception.class)
    public String tratarErroInterno(
            Exception exception,
            HttpServletRequest request,
            Model model) {

        model.addAttribute("codigo", 500);
        model.addAttribute("titulo", "Erro interno");
        model.addAttribute(
                "mensagem",
                "Ocorreu um problema inesperado. Tente novamente mais tarde.");
        model.addAttribute("caminho", request.getRequestURI());

        return "erro/erro";
    }

    private String obterMensagem(HttpStatus status) {

        return switch (status) {
            case BAD_REQUEST ->
                "A solicitação contém informações inválidas.";
            case NOT_FOUND ->
                "O recurso solicitado não foi encontrado.";
            case FORBIDDEN ->
                "Você não possui permissão para acessar este recurso.";
            case UNAUTHORIZED ->
                "É necessário realizar a autenticação.";
            case CONFLICT ->
                "A operação não pode ser realizada devido a um conflito.";
            default ->
                "Não foi possível concluir sua solicitação.";
        };
    }
}
