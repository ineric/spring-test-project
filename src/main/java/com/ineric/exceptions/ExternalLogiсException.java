package com.ineric.exceptions;

public class ExternalLogiсException extends RuntimeException {
    private Object objectProcessed;

    public ExternalLogiсException(Object objectProcessed) {
        super();
        this.objectProcessed = objectProcessed;
    }

    public Object getObjectProcessed() {
        return objectProcessed;
    }
}
