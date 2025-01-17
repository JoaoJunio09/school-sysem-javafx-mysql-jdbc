package br.com.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }

    private Map<String, String> errors = new HashMap<String, String>();

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(String nameFieldError, String messageError) {
        errors.put(nameFieldError, messageError);
    }
}
