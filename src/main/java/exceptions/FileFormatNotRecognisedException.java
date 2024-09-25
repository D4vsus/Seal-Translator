package exceptions;

/**
 * <h1>FileFormatNotRecognisedException</h1>
 * <p>throw when file format is not recognized in the input</p>
 *
 * @author D4vsus
 */
public class FileFormatNotRecognisedException extends Exception {
    @Override
    public String toString() {
        return "Error: File format not recognised";
    }
}
