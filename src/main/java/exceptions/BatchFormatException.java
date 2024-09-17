package exceptions;

public class BatchFormatException extends Exception{
    @Override
    public String toString() {
        return "Error: Batch must be a natural number, or the word 'max'";
    }
}
