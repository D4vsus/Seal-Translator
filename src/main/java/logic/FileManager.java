package logic;

import UI.AttributeItem;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import exceptions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // For .xlsx files
import org.apache.poi.hssf.usermodel.HSSFWorkbook; // For .xls files
import org.jetbrains.annotations.NotNull;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * <h1>FileManager</h1>
 * <p>Manage the IO from the files</p>
 *
 * @author D4vsus
 */
public class FileManager {

    //variables and objects
    public final static String[] SUPPORTEDFORMATS = {"csv","xls","xlsx"};

    //methods
    /**
     * <h1>writeFile()</h1>
     * <p>Write the content into the file</p>
     *
     * @param file : {@link File}
     * @param content : {@link String}
     */
    private static void writeFile(File file, String content) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.close();
    }

    /**
     * <h1>exportARFF()</h1>
     * <p>Create the arff file and write it's content</p>
     *
     * @param dataTable : {@link DataTable}
     * @param fileName : {@link String}
     * @author D4vsus
     */
    public static void exportARFF(@NotNull DataTable dataTable, String fileName) throws Exception{
        //Preprocess
        //see if they added the name to the dataset
        if (dataTable.getRelation().isBlank()) throw new NoDatasetNameException();

        //see they select an attribute
        for (Attribute attribute : dataTable.getAttributes()){
            if (attribute.getAttributeType().equals("Select attribute...")){
                throw new NotSelectedAttributeException();
            }
        }

        //Process
        File file = new File(fileName + ".arff");
        if (!file.createNewFile()) {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        }

        writeFile(file, dataTable.toARFF());
    }

    /**
     * <h1>loadCSV()</h1>
     * <p>Load the data to the program</p>
     *
     * @param dataTable : {@link DataTable}
     * @param path      : {@link String}
     * @author D4vsus
     */
    public static void loadCSV(DataTable dataTable, String path) throws IOException, DuplicatedNameException, NotMatchSizeMetadata, CsvException {

        BufferedReader preprocess = new BufferedReader(new FileReader(path));
        StringBuilder content = new StringBuilder();

        //delete CSV comments "#"
        if (Config.isDeleteCSComments()) {
            content.append(preprocess.lines()
                    .filter(line -> !line.trim().startsWith("#")) // Ignore lines starting with #
                    .collect(Collectors.joining("\n"))
                    .replace(';', ',')
            );
        } else {
            content.append(preprocess.lines()
                    .collect(Collectors.joining("\n"))
                    .replace(';', ','));
        }

        CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
        try (CSVReader reader = new CSVReaderBuilder(new StringReader(content.toString())).withCSVParser(parser).build()) {
            List<String[]> records = reader.readAll();  // Read all records

            // Skip the header (if the first row is a header)
            boolean isFirstRow = true;
            for (String[] record : records) {
                if (isFirstRow) {
                    isFirstRow = false;
                    for (String attributeName : record) {
                        dataTable.addAttribute(new AttributeItem(attributeName.replace(' ','-')));
                    }
                    continue;
                }
                dataTable.addRow(record);
            }
        }
    }

    /**
     * <h1>loadXLSAndXSLX()</h1>
     * <p>load the xls or xlsx </p>
     *
     * @param dataTable {@link DataTable}
     * @param path {@link String}
     * @throws IOException : from FileInputStream
     * @throws NullPointerException : if workbook still null
     * @throws NotMatchSizeMetadata
     * @throws DuplicatedNameException
     * @throws FileFormatNotRecognisedException : if it's not a xsl or xslx file
     */
    public static void loadXLSAndXSLX(DataTable dataTable, String path) throws IOException, NullPointerException, NotMatchSizeMetadata, DuplicatedNameException, FileFormatNotRecognisedException {
        //set up the loading bar
        Workbook workbook = null;
        try (FileInputStream fis = new FileInputStream(path)){
                //open the file

                //see if it's a xls or a xlsx
                if (path.endsWith("xls")) workbook = new HSSFWorkbook(fis);
                else if (path.endsWith("xlsx")) workbook = new XSSFWorkbook(fis);
                else throw new FileFormatNotRecognisedException();

                // Get the first sheet
                Sheet sheet = Objects.requireNonNull(workbook).getSheetAt(0);


                //initialize the iterator
                Iterator<Row> iterator = sheet.rowIterator();

                //get the attributes
                if (iterator.hasNext()) {
                    if (Config.isDeleteCSComments()) {
                        boolean endComment = false;
                        do {
                            Row row = iterator.next();
                            if (!row.getCell(0).getStringCellValue().startsWith("#")) {
                                endComment = true;
                                for (Cell attribute : row) {
                                    dataTable.addAttribute(new AttributeItem(attribute.getStringCellValue().replace(" ", "-")));
                                }
                            }
                        } while (iterator.hasNext() && !endComment);
                    } else {
                        for (Cell attribute : iterator.next()) {
                            dataTable.addAttribute(new AttributeItem(attribute.getStringCellValue().replace(" ", "-")));
                        }
                    }
                }

                //get the data
                Row row;
                while (iterator.hasNext()) {
                    row = iterator.next();
                    ArrayList<String> data = getRows(row);

                    //break if the rows is empty
                    if (data.stream().allMatch(String::isBlank)){
                        break;
                    }

                    dataTable.addRow(data.toArray(new String[0]));
                }

        } finally {
            // Close resources
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    /**
     * <h1>getRows()</h1>
     * <p>Parse the row types and return the content as an array list of strings</p>
     *
     * @param row {@link ArrayList}<{@link String}>
     * @return ArrayList<String>
     */
    private static @NotNull ArrayList<String> getRows(Row row) {
        ArrayList<String> data = new ArrayList<>();
        for (Cell cell : row) {
            // Process each cell based on its type
            switch (cell.getCellType()) {
                case STRING:
                    data.add(cell.getStringCellValue());
                    break;
                case NUMERIC:
                    data.add(String.valueOf(cell.getNumericCellValue()));
                    break;
                case BOOLEAN:
                    data.add(String.valueOf(cell.getBooleanCellValue()));
                    break;
                default:
                    data.add("");
            }
        }
        return data;
    }

    /**
     * <h1>loadFile()</h1>
     * <p>parse and load the file</p>
     *
     * @param dataTable {@link DataTable}
     * @param path {@link String}
     * @throws DuplicatedNameException
     * @throws NotMatchSizeMetadata
     * @throws IOException
     * @throws FileFormatNotRecognisedException
     */
    public static void loadFile(DataTable dataTable, @NotNull String path) throws DuplicatedNameException, NotMatchSizeMetadata, IOException, FileFormatNotRecognisedException, CsvException {
        //clear all the table
        dataTable.clearAll();

        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(String.join(",",FileManager.SUPPORTEDFORMATS),FileManager.SUPPORTEDFORMATS);
        File file = new File(path);
        if (!extensionFilter.accept(file)) throw new FileFormatNotRecognisedException();
        if (path.trim().toLowerCase().endsWith(".csv")) loadCSV(dataTable,path);
        if (path.trim().toLowerCase().endsWith(".xls") || path.trim().toLowerCase().endsWith(".xlsx")) loadXLSAndXSLX(dataTable,path);
    }
}
