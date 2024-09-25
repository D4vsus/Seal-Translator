package exceptions;

/**
 * <h1>NullRelation</h1>
 * <p>throw when the name of the dataset is null in DataTable</p>
 *
 * @author D4vsus
 */
public class NullRelation extends Exception{
    @Override
    public String toString() {
        return "Error: no relation name added";
    }
}
