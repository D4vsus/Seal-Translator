package exceptions;

/**
 * <h1>TableOverflow</h1>
 * <p>throw when trying to access to a not added value to the table</p>
 *
 * @author D4vsus
 */
public class TableOverflow extends Exception{
    @Override
    public String toString() {
        return "Error: table overflow, make sure the size correspond to the number of columns";
    }
}
