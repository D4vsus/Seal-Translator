package exceptions;

public class DuplicatedNameException extends Exception{

    //variables and objects
    String duplicatedName;

    //methods
    public DuplicatedNameException(String duplicatedName){
        this.duplicatedName = duplicatedName;
    }

    @Override
    public String toString() {
        return "Error: the name of the attribute '"+this.duplicatedName+"' is duplicated";
    }
}
