package exceptions;

public class NotMatchSizeMetadata extends Exception{

    //variables and objects
    private int row;

    //methods
    public NotMatchSizeMetadata(int row){
        this.row = row;
    }

    @Override
    public String toString() {
        return "Error: not matching size in between the columns and the rows, at row: "+ row;
    }
}
