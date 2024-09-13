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
 * @author D4vsus
 */
public class DataTable {
    //variables and objects
    private String comments;
    private String relation;
    private ArrayList<Attribute> metaData;
    private final HashMap<Integer,ArrayList<String>> table;

    //methods
    public DataTable(){
        setComments("");
        setRelation("");
        table=new HashMap<>();
        setAttribute(new ArrayList<>());
    }

    public DataTable(ArrayList<Attribute> columns){
        setComments("");
        setRelation("");
        table=new HashMap<>();
        setAttribute(columns);
    }

    public DataTable(Attribute[] columns){
        setComments("");
        setRelation("");
        table=new HashMap<>();
        setAttribute((ArrayList<Attribute>) Arrays.asList(columns));
    }

    //methods
    public DataTable(ArrayList<Attribute> columns,String relation){
        setComments("");
        setRelation(relation);
        table=new HashMap<>();
        setAttribute(columns);
    }

    public void setComments(String comments) {this.comments = comments;}

    public void setAttribute(ArrayList<Attribute> columns){metaData = columns;}

    public Attribute getAttribute(int position){
        return metaData.get(position);
    }

    public void setRelation(String relation){this.relation = relation;}

    public String getComments() {return comments;}

    public String getRelation() {return relation;}

    public void addRow(String @NotNull [] record) throws NotMatchSizeMetadata {
        if (record.length != metaData.size()) throw new NotMatchSizeMetadata();

        table.put(table.size(),new ArrayList<>(Arrays.asList(record)));
    }

    public void addAttribute(Attribute attribute) throws DuplicatedNameException {
        if (metaData.contains(attribute))throw new DuplicatedNameException(attribute.attributeName);
        metaData.add(attribute);
    }


    public void setRow(int row,String @NotNull [] record) throws NotMatchSizeMetadata{
        if (record.length != metaData.size()) throw new NotMatchSizeMetadata();

        table.put(row,new ArrayList<>(Arrays.asList(record)));
    }

    public String[] getRow(int row) throws TableOverflow {
        if (table.size() - 1 < row || row < 0) throw new TableOverflow();

        return table.get(row).toArray(new String[0]);
    }

    public Attribute[] getAttributes() {
        return metaData.toArray(new Attribute[0]);
    }

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

    public void clearTable(){
        table.clear();
    }

    public void clearAll(){
        setComments("");
        setRelation("");
        metaData.clear();
        table.clear();
    }

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

    public void removeRow(int row) throws TableOverflow {
        if (table.size() - 1 < row || row < 0) throw new TableOverflow();

        table.remove(row);
        reorganize();
    }

    public boolean isEmpty(){
        return table.isEmpty();
    }

    private String addData(){
        StringBuilder string = new StringBuilder();
        for (int i = 0;i < table.size();i++) {
            for (String cell : table.get(i)){
                string.append(cell).append(",");
            }
            string.setCharAt(string.length()-1, '\n');
        }
        return string.toString();
    }

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

    private String commentToArff(){
        return "%" + comments.replaceAll("\n","\n% ");
    }

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
    public void loadARFFAttributes(String...attributesType) throws NotMatchSizeMetadata {
        if (attributesType.length != metaData.size()) throw  new NotMatchSizeMetadata();

        for (int i = 0 ; i < attributesType.length;i++){
            loadARFFAttribute(i,attributesType[i]);
        }
    }

    private void loadARFFAttribute(int attributePosition,String attributeType){
        metaData.get(attributePosition).setAttributeType(attributeType);
    }


}
