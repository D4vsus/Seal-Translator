package logic;

import UI.AttributeItem;
import exceptions.NotMatchSizeMetadata;
import exceptions.TableOverflow;

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
    ArrayList<AttributeItem> metaData;
    HashMap<Integer,ArrayList<String>> table;

    //methods
    public DataTable(ArrayList<AttributeItem> columns){
        setMetaData(columns);
    }

    private void setMetaData(ArrayList<AttributeItem> columns){
        metaData = columns;
    }

    public void addRow(String[] record) throws NotMatchSizeMetadata {
        if (record.length != metaData.size()) throw new NotMatchSizeMetadata();

        table.put(table.size(),new ArrayList<>(Arrays.asList(record)));
    }

    public void setRow(int row,String[] record) throws NotMatchSizeMetadata{
        if (record.length != metaData.size()) throw new NotMatchSizeMetadata();

        table.put(row,new ArrayList<>(Arrays.asList(record)));
    }

    public String[] getRow(int row) throws TableOverflow {
        if (table.size() - 1 < row || row < 0) throw new TableOverflow();

        return table.get(row).toArray(new String[0]);
    }

    public String[][] getRows(int rowFrom,int rowTo) throws TableOverflow {
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
}
