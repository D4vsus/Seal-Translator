package exceptions;

public class FileFormatNotRecognisedException extends Exception {
    @Override
    public String toString() {
        return "Error: File format not recognised";
    }
}
