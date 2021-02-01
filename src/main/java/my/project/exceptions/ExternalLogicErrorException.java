package my.project.exceptions;

public class ExternalLogicErrorException extends RuntimeException {
    private Object objectProcessed;

    public ExternalLogicErrorException(Object objectProcessed) {
        super();
        this.objectProcessed = objectProcessed;
    }

    public Object getObjectProcessed() {
        return objectProcessed;
    }
}
