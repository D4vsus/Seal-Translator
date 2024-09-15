package exceptions;

public class NoDatasetNameException extends Exception{
    @Override
    public String toString() {
        return "Error: you haven't added the name of the dataset";
    }
}
