package exceptions;

public class ImportException extends Exception{
    @Override
    public String toString() {
        return "Error: The file cant be imported";
    }
}
