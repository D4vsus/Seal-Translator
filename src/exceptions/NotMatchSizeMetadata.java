package exceptions;

public class NotMatchSizeMetadata extends Exception{
    @Override
    public String toString() {
        return "Error: not matching size in between the columns and the rows";
    }
}
