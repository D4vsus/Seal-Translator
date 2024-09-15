package exceptions;

public class FileNotCSVException extends Exception{
    @Override
    public String toString() {
        return "Error: The File is not a CSV";
    }
}
