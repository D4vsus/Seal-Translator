package logic;

import UI.AttributeItem;
import exceptions.DuplicatedNameException;
import exceptions.NotMatchSizeMetadata;
import exceptions.NullRelation;
import exceptions.TableOverflow;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <h1>DataTable</h1>
 * <p>Save the content in a tables (HashMap<Integer,ArrayList<String>>) and export it to formats CSV (toString()) and ARFF</p>
 *
 * @author D4vsus
 */
public class DataTable {

    //variables and objects
    private String comments;
    private String relation;
    private ArrayList<AttributeItem> metaData;
    private final HashMap<Integer,ArrayList<String>> table;

    //methods
    /**
     * <h1>Constructor</h1>
     * <p>Instantiate the class</p>
     */
    public DataTable(){
        setComments("");
        setRelation("");
        table=new HashMap<>();
        setAttributeItem(new ArrayList<>());
    }

    /**
     * <h1>Constructor</h1>
     * <p>Instantiate the class with AttributeItems added</p>
     *
     * @param columns {@link ArrayList}<{@link AttributeItem}>
     */
    public DataTable(ArrayList<AttributeItem> columns){
        setComments("");
        setRelation("");
        table=new HashMap<>();
        setAttributeItem(columns);
    }

    /**
     * <h1>Constructor</h1>
     * <p>Instantiate the class with AttributeItems added</p>
     *
     * @param columns {@link AttributeItem[]}
     */
    public DataTable(AttributeItem[] columns){
        setComments("");
        setRelation("");
        table=new HashMap<>();
        setAttributeItem((ArrayList<AttributeItem>) Arrays.asList(columns));
    }

    /**
     * <h1>Constructor</h1>
     * <p>Instantiate the class with AttributeItems added</p>
     *
     * @param columns {@link AttributeItem[]}
     * @param relation {@link String}
     */
    public DataTable(ArrayList<AttributeItem> columns,String relation){
        setComments("");
        setRelation(relation);
        table=new HashMap<>();
        setAttributeItem(columns);
    }

    public void setComments(String comments) {this.comments = comments;}

    public void setAttributeItem(ArrayList<AttributeItem> columns){metaData = columns;}

    public void setRelation(String relation){this.relation = relation;}

    public String getComments() {return comments;}

    public AttributeItem getAttributeItem(int position){
        return metaData.get(position);
    }

    public String getRelation() {return relation;}

    /**
     * <h1>addRow()</h1>
     * <p>Add a record to the table</p>
     *
     * @param record {@link String[]}
     * @throws NotMatchSizeMetadata : when record don't match with attributes size
     */
    public void addRow(String @NotNull [] record) throws NotMatchSizeMetadata {
        if (record.length != metaData.size()) throw new NotMatchSizeMetadata(table.size() + 2);//return the error line starting from 1 without the header
        table.put(table.size(),new ArrayList<>(Arrays.asList(record)));
    }

    /**
     * <h1>addAttributeItem()</h1>
     * <p>Add an AttributeItem to the table</p>
     *
     * @param attribute {@link AttributeItem}
     * @throws DuplicatedNameException : throw when find the same attribute
     */
    public void addAttribute(AttributeItem attribute) throws DuplicatedNameException {
        if (metaData.contains(attribute))throw new DuplicatedNameException(attribute.getAttributeName());

        metaData.add(attribute);
    }

    /**
     * <h1>addAttributeItems()</h1>
     * <p>Add all AttributeItems to the table</p>
     *
     * @param attribute {@link AttributeItem}
     * @throws DuplicatedNameException : throw when add a new attribute and is another equal
     */
    public void addAttributes(ArrayList<AttributeItem> attribute) throws DuplicatedNameException {
        Set<AttributeItem> set = new HashSet<>();
        for (AttributeItem item : attribute) {
            if (!set.add(item)) {
                throw new DuplicatedNameException(item.getAttributeName());
            }
        }

        metaData = attribute;
    }

    /**
     * <h1>setRow()</h1>
     *
     * @param row int
     * @param record {@link String}
     * @throws NotMatchSizeMetadata : throw when record size don`t mach with the metadata size
     */
    public void setRow(int row,String @NotNull [] record) throws NotMatchSizeMetadata{
        if (record.length != metaData.size()) throw new NotMatchSizeMetadata(row);

        table.put(row,new ArrayList<>(Arrays.asList(record)));
    }

    /**
     * <h1>getRow()</h1>
     * <p>Get a specified row</p>
     *
     * @param row int
     * @return {@link String}[]
     * @throws TableOverflow : if they request a record out of bound
     */
    public String[] getRow(int row) throws TableOverflow {
        if (table.size() - 1 < row || row < 0) throw new TableOverflow();

        return table.get(row).toArray(new String[0]);
    }

    /**
     * <h1>getColumn()</h1>
     * <p>Get a specified column</p>
     *
     * @param numColumn int
     * @return {@link HashMap}<{@link Integer},{@link String}>
     * @throws TableOverflow : if they request a record out of bound
     */
    public HashMap<Integer,String> getColumn(int numColumn) throws TableOverflow {
        if (metaData.size() - 1 < numColumn || numColumn < 0) throw new TableOverflow();
        HashMap <Integer,String> column = new HashMap<>();
        for (int i = 0;i < table.size();i++){
            column.put(i,table.get(i).get(numColumn));
        }

        return column;
    }

    /**
     * <h1>getAttributeItems()</h1>
     * <p>Return the AttributeItems</p>
     *
     * @return {@link AttributeItem}
     */
    public ArrayList<AttributeItem> getAttributes() {
        return metaData;
    }

    public Set<Integer> droppableColumns(){
        int i = 0;
        Set<Integer> droppableColumns = new HashSet<>();
        for (AttributeItem attributeItem : metaData){
            if (attributeItem.isDropColumn()){
                droppableColumns.add(i);
            }
            i++;
        }
        return droppableColumns;
    }

    /**
     * <h1>getRows()</h1>
     * <p>get rows from x to y.</br> if from is bigger than row the return you in revers order</p>
     *
     * @param rowFrom int
     * @param rowTo int
     * @return {@link String[][]}
     * @throws TableOverflow : if they request a record out of bound
     */
    public String[][] getRows(int rowFrom, int rowTo) throws TableOverflow {
        if (table.size() - 1 < rowFrom || rowFrom < 0 || table.size() - 1 < rowTo || rowTo < 0) throw new TableOverflow();

        String[][] rows = new String[Math.abs(rowTo - rowFrom)][metaData.size()];
        if (rowTo > rowFrom) {
            for (int i = rowFrom; i < rowTo; i++) {
                rows[i] = table.get(i).toArray(new String[0]);
            }
        } else {
            for (int i = rowFrom; i > rowTo; i--) {
                rows[i] = table.get(i).toArray(new String[0]);
            }
        }
        return rows;
    }

    /**
     * <h1>clearTable()</h1>
     * <p>Clear the table content</p>
     *
     */
    public void clearTable(){
        table.clear();
    }

    /**
     * <h1>clearAll()</h1>
     * <p>Clear all the content</p>
     */
    public void clearAll(){
        setComments("");
        setRelation("");
        metaData.clear();
        table.clear();
    }

    /**
     * <h1>reorganize()</h1>
     * <p>Reorganize the table</p>
     */
    public void reorganize(){
        for (int i = table.size(); i > 0;i--)
            if (table.get(i) == null) {
                for (int x = i; x < table.size(); x++) {
                    table.replace(x, table.get(x + 1));
                }
                table.remove(table.size() - 1);
                reorganize();
            }
    }

    /**
     * <h1>removeRow()</h1>
     * <p>Remove the selected row</p>
     *
     * @param row int
     * @throws TableOverflow : if they request a record out of bound
     */
    public void removeRow(int row) throws TableOverflow {
        if (table.size() - 1 < row || row < 0) throw new TableOverflow();

        table.remove(row);
        reorganize();
    }

    /**
     * <h1>isEmpty()</h1>
     * <p>See if the table is empty</p>
     *
     * @return boolean
     */
    public boolean isEmpty(){
        return table.isEmpty();
    }

    /**
     * <h1>addData()</h1>
     * <p>Add the data to the table</p>
     *
     * @return {@link String}
     */
    private @NotNull String addData(){
        int column;
        StringBuilder string = new StringBuilder();
        for (int row = 0;row < table.size();row++) {
            column = 0;
            for (String cell : table.get(row)){

                cell = convertStringQuotes(cell);

                // If column is droppable
                if (droppableColumns().contains(column)) {continue;}

                // If cell is null or if they contain the null text
                if (!cell.isBlank() && !cell.equals(Config.getNullString())) {

                    // See if it's nominal to replace spaces with "-"
                    if (!metaData.get(column).getAttributeType().equals("nominal")){

                        // See if it's a string type, doesn't have quotes between them and is Configuration active
                        Pattern pattern = Pattern.compile("^'[^']*'$");
                        if (Config.isAddQuotesToStringAttributes() && !pattern.matcher(cell).matches() && metaData.get(column).getAttributeType().equals("string")){
                            string.append("'").append(cell).append("'");
                        } else {
                            string.append(cell);
                        }

                    } else {
                        string.append(cell.replace(" ","-"));
                    }

                } else {
                    string.append("?");
                }

                string.append(",");
                column++;
            }
            if (!string.isEmpty()) {
                string.setCharAt(string.length() - 1, '\n');
            }
        }
        return string.toString();
    }

    /**
     * <h1>convertStringQuotes()</h1>
     * <p>Return the String converting the ' to \' except if it's between ''</p>
     *
     * @param string : {@link String}
     * @return {@link String}
     */
    public String convertStringQuotes(String string){
        // if is between quotes
        if (string.startsWith("'") && string.endsWith("'") && string.length() > 2){
            // Replace ' with \' in the middle part of the string (excluding the first and last character)
            return string.charAt(0) + string.substring(1, string.length() - 1).replace("'", "\\'") + string.charAt(string.length() - 1);
        } else {
            // Replace ' with \'
            return string.replace("'","\\'");
        }
    }

    /**
     * <h1>toString()</h1>
     * <p>Return the content as CSV</p>
     *
     * @return {@link String}
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        if (!metaData.isEmpty()) {
            for (AttributeItem attribute : metaData) {
                string.append(attribute.getAttributeARRF()).append(",");
            }
            string.setCharAt(string.length() - 1, '\n');
            string.append(addData());
            string.deleteCharAt(string.length() - 1);
        } else {
            return "NaN";
        }
        return string.toString();
    }

    /**
     * <h1>commentToArff()</h1>
     * <p>Pass the comments to ARFF format</p>
     *
     * @return {@link String}
     */
    private String commentToArff(){
        return "%" + comments.replaceAll("\n","\n% ");
    }

    /**
     * <h1>toARFF()</h1>
     * <p>Return the content as ARFF </p>
     *
     * @return {@link String}
     * @throws NullRelation : if the relation is null
     */
    public String toARFF() throws NullRelation {
        if (relation == null||relation.isEmpty()) throw new NullRelation();

        StringBuilder string = new StringBuilder();
        if (!comments.isBlank())string.append(commentToArff()).append("\n");

        string.append("@relation ").append(relation).append("\n\n");
        int column = 0;
        for (AttributeItem attribute:metaData){
            if (!attribute.isDropColumn()) {
                string.append(attribute.toARFF()).append('\n');
            }
        }
        string.append("\n").append("@data").append('\n');
        string.append(addData());
        return string.toString();
    }

    /**
     * <h1>size()</h1>
     * <p>Get the size of the table</p>
     *
     * @return int
     */
    public int size(){
        return table.size();
    }

    /**
     * <h1>numberOfCells()</h1>
     * <p>Get the number of cells of the table</p>
     *
     * @return int
     */
    public int numberOfCells(){
        return table.size() * metaData.size();
    }
}
