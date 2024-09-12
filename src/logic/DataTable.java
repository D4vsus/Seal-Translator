package logic;

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
    String comments;
    String relation;
    ArrayList<Attribute> metaData;
    HashMap<Integer,ArrayList<String>> table;

    //methods
    public DataTable(ArrayList<Attribute> columns){
        comments = "";
        table=new HashMap<>();
        setMetaData(columns);
    }

    //methods
    public DataTable(ArrayList<Attribute> columns,String relation){
        comments = "";
        this.relation = relation;
        table=new HashMap<>();
        setMetaData(columns);
    }

    public void setComments(String comments) {this.comments = comments;}

    public String getComments() {return comments;}

    private void setMetaData(ArrayList<Attribute> columns){
        metaData = columns;
    }

    public void setRelation(String relation){this.relation = relation;}

    public ArrayList<Attribute> getMetaData() {return metaData;}

    public String getRelation() {return relation;}

    public void addRow(String @NotNull [] record) throws NotMatchSizeMetadata {
        if (record.length != metaData.size()) throw new NotMatchSizeMetadata();

        table.put(table.size(),new ArrayList<>(Arrays.asList(record)));
    }

    public void setRow(int row,String @NotNull [] record) throws NotMatchSizeMetadata{
        if (record.length != metaData.size()) throw new NotMatchSizeMetadata();

        table.put(row,new ArrayList<>(Arrays.asList(record)));
    }

    public String[] getRow(int row) throws TableOverflow {
        if (table.size() - 1 < row || row < 0) throw new TableOverflow();

        return table.get(row).toArray(new String[0]);
    }

    public ArrayList<Attribute> getAttributes() {
        return metaData;
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

    public void reorganize(){
        for (int i = table.size(); i > 0;i--)
            if (table.get(i).isEmpty()) {
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
            string.append(attribute.getAttribute()).append('\n');
        }
        string.append("@data").append('\n');
        string.append(addData());
        return string.toString();
    }

    public static void main(String[] args) throws NullRelation {

        ArrayList<Attribute> arrayList = new ArrayList<>();
        arrayList.add(new Attribute("saludo"));
        arrayList.add(new Attribute("despedida"));
        arrayList.get(0).setAttributeType("string");
        arrayList.get(1).setAttributeType("string");

        DataTable dataTable = new DataTable(arrayList,"Saludos");
        String[] feed = {"hey","bye"};

        try {
            dataTable.addRow(feed);
        } catch (NotMatchSizeMetadata e) {
            throw new RuntimeException(e);
        }

        System.out.println(dataTable);
        System.out.println(dataTable.toARFF());
    }
}
