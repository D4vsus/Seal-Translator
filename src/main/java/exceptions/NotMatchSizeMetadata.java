package exceptions;

/**
 * <h1>NotMatchSizeMetadata</h1>
 * <p>throw when a record size don't match with the size of the metadata</p>
 *
 * @author D4vsus
 */
public class NotMatchSizeMetadata extends Exception{

    //variables and objects
    private final int row;

    //methods
    public NotMatchSizeMetadata(int row){
        this.row = row;
    }

    @Override
    public String toString() {
        return "Error: not matching size in between the columns and the rows, at row: "+ row;
    }
}
