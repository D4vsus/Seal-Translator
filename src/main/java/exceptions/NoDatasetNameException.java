package exceptions;

/**
 * <h1>NoDatasetNameException</h1>
 * <p>throw when user don't write the name of the dataset</p>
 *
 * @author D4vsus
 */
public class NoDatasetNameException extends Exception{
    @Override
    public String toString() {
        return "Error: you haven't added the name of the dataset";
    }
}
