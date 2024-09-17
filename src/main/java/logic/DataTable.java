package logic;

import UI.AttributeItem;
import exceptions.DuplicatedNameException;
import exceptions.NotMatchSizeMetadata;
import exceptions.NullRelation;
import exceptions.TableOverflow;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * <h1>DataTable</h1>
 * <p>save the content of a file</p>
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
     * @throws NotMatchSizeMetadata
     */
    public void addRow(String @NotNull [] record) throws NotMatchSizeMetadata {
        if (record.length != metaData.size()) throw new NotMatchSizeMetadata(table.size()-1);
        table.put(table.size(),new ArrayList<>(Arrays.asList(record)));
    }

    /**
     * <h1>addAttributeItem()</h1>
     * <p>Add an AttributeItem to the table</p>
     *
     * @param attribute {@link AttributeItem}
     * @throws DuplicatedNameException
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
     * @throws DuplicatedNameException
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
     * @throws NotMatchSizeMetadata
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
     * @return {@link String[]}
     * @throws TableOverflow
     */
    public String[] getRow(int row) throws TableOverflow {
        if (table.size() - 1 < row || row < 0) throw new TableOverflow();

        return table.get(row).toArray(new String[0]);
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

    /**
     * <h1>getRows()</h1>
     * <p>get rows from x to y.</br> if from is bigger than row the return you in revers order</p>
     *
     * @param rowFrom int
     * @param rowTo int
     * @return {@link String[][]}
     * @throws TableOverflow
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
     * @throws TableOverflow
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
        StringBuilder string = new StringBuilder();
        for (int i = 0;i < table.size();i++) {
            for (String cell : table.get(i)){
                string.append(cell).append(",");
            }
            string.setCharAt(string.length()-1, '\n');
        }
        return string.toString();
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
        for (AttributeItem attribute: metaData){
            string.append(attribute.getAttributeARRF()).append(",");
        }
        string.setCharAt(string.length() - 1, '\n');
        string.append(addData());
        string.deleteCharAt(string.length() - 1);
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
     * @throws NullRelation
     */
    public String toARFF() throws NullRelation {
        if (relation == null||relation.isEmpty()) throw new NullRelation();

        StringBuilder string = new StringBuilder();
        if (!comments.isBlank())string.append(commentToArff()).append("\n");

        string.append("@relation ").append(relation).append("\n\n");
        for (AttributeItem attribute:metaData){
            string.append(attribute.toARFF()).append('\n');
        }
        string.append("\n").append("@data").append('\n');
        string.append(addData());
        return string.toString();
    }

    public int size(){
        return table.size();
    }
}
