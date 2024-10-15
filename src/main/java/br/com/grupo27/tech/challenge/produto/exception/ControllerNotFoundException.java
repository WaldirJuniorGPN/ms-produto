package br.com.grupo27.tech.challenge.produto.exception;

public class ControllerNotFoundException extends RuntimeException {
    public ControllerNotFoundException(String msg) {
        super(msg);
    }
}
