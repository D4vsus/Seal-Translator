package exceptions;

public class TableOverflow extends Exception{
    @Override
    public String toString() {
        return "Error: table overflow, make sure the size correspond to the number of columns";
    }
}
