package br.com.exceptions;

public class IntegrityDbException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public IntegrityDbException(String msg) {
        super(msg);
    }
}