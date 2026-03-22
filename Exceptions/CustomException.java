package Exceptions;

public class CustomException extends Exception{
    private String message;

    public CustomException(String msg) {
        super(msg);
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }
}
