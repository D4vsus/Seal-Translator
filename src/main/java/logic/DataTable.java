package logic;

import exceptions.DuplicatedNameException;
import exceptions.NotMatchSizeMetadata;
import exceptions.NullRelation;
import exceptions.TableOverflow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    private ArrayList<Attribute> metaData;
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
        setAttribute(new ArrayList<>());
    }

    /**
     * <h1>Constructor</h1>
     * <p>Instantiate the class with attributes added</p>
     *
     * @param columns {@link ArrayList}<{@link Attribute}>
     */
    public DataTable(ArrayList<Attribute> columns){
        setComments("");
        setRelation("");
        table=new HashMap<>();
        setAttribute(columns);
    }

    /**
     * <h1>Constructor</h1>
     * <p>Instantiate the class with attributes added</p>
     *
     * @param columns {@link Attribute[]}
     */
    public DataTable(Attribute[] columns){
        setComments("");
        setRelation("");
        table=new HashMap<>();
        setAttribute((ArrayList<Attribute>) Arrays.asList(columns));
    }

    /**
     * <h1>Constructor</h1>
     * <p>Instantiate the class with attributes added</p>
     *
     * @param columns {@link Attribute[]}
     * @param relation {@link String}
     */
    public DataTable(ArrayList<Attribute> columns,String relation){
        setComments("");
        setRelation(relation);
        table=new HashMap<>();
        setAttribute(columns);
    }

    public void setComments(String comments) {this.comments = comments;}

    public void setAttribute(ArrayList<Attribute> columns){metaData = columns;}

    public void setRelation(String relation){this.relation = relation;}

    public String getComments() {return comments;}

    public Attribute getAttribute(int position){
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
        if (record.length != metaData.size()) {
            System.out.println(metaData.stream().toList());
            System.out.println(Arrays.stream(record).toList());
            System.out.println(metaData.size());
            System.out.println(record.length);
            throw new NotMatchSizeMetadata();
        }
        table.put(table.size(),new ArrayList<>(Arrays.asList(record)));
    }

    /**
     * <h1>addAttribute()</h1>
     * <p>Add an attribute to the table</p>
     *
     * @param attribute {@link Attribute}
     * @throws DuplicatedNameException
     */
    public void addAttribute(Attribute attribute) throws DuplicatedNameException {
        if (metaData.contains(attribute))throw new DuplicatedNameException(attribute.attributeName);

        metaData.add(attribute);
    }

    /**
     * <h1>setRow()</h1>
     *
     * @param row int
     * @param record {@link String}
     * @throws NotMatchSizeMetadata
     */
    public void setRow(int row,String @NotNull [] record) throws NotMatchSizeMetadata{
        if (record.length != metaData.size()) throw new NotMatchSizeMetadata();

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
     * <h1>getAttributes()</h1>
     * <p>Return the Attributes</p>
     *
     * @return {@link Attribute}
     */
    public Attribute[] getAttributes() {
        return metaData.toArray(new Attribute[0]);
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
        for (Attribute Attribute: metaData){
            string.append(Attribute.getAttributeName()).append(",");
        }
        string.setCharAt(string.length()-1, '\n');
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

        string.append("@relation ").append(relation).append("\n");
        for (Attribute attribute:metaData){
            string.append(attribute.getAttributeARRF()).append('\n');
        }
        string.append("@data").append('\n');
        string.append(addData());
        return string.toString();
    }

    /**
     * <h1>loadARFFAttributes()</h1>
     * <p>Load the arff attributes</p>
     *
     * @param attributesType {@link String...}
     * @throws NotMatchSizeMetadata
     */
    public void loadARFFAttributes(String...attributesType) throws NotMatchSizeMetadata {
        if (attributesType.length != metaData.size()) throw  new NotMatchSizeMetadata();

        for (int i = 0 ; i < attributesType.length;i++){
            loadARFFAttribute(i,attributesType[i]);
        }
    }

    /**
     * <h1>loadARFFAttribute()</h1>
     * <p>Load the ARFF file attributes</p>
     *
     * @param attributePosition int
     * @param attributeType {@link String}
     */
    private void loadARFFAttribute(int attributePosition,String attributeType){
        metaData.get(attributePosition).setAttributeType(attributeType);
    }

    public int size(){
        return table.size();
    }
}
