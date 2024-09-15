package exceptions;

public class NullRelation extends Exception{
    @Override
    public String toString() {
        return "Error: no relation name added";
    }
}
