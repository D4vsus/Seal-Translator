package logic;

import UI.AttributeItem;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import exceptions.*;

import java.io.*;
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
    public static void loadCSV(DataTable dataTable, String path) throws IOException, DuplicatedNameException, NotMatchSizeMetadata {

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

        } catch (IOException | CsvException e) {
           throw new IOException();
        }
    }

    /**
     * <h1>loadXLSAndXSLX()</h1>
     * <p>load the xls or xlsx </p>
     *
     * @param dataTable {@link DataTable}
     * @param path {@link String}
     * @throws IOException
     * @throws NullPointerException
     * @throws NotMatchSizeMetadata
     * @throws DuplicatedNameException
     * @throws FileFormatNotRecognisedException
     */
    public static void loadXLSAndXSLX(DataTable dataTable, String path) throws IOException, NullPointerException, NotMatchSizeMetadata, DuplicatedNameException, FileFormatNotRecognisedException {
        //open the file
        FileInputStream fis = new FileInputStream(path);
        Workbook workbook;

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
            for (Cell attribute : iterator.next()) {
                dataTable.addAttribute(new AttributeItem(attribute.getStringCellValue().replace(" ", "-")));
            }
        }

        //get the data
        Row row;
        while (iterator.hasNext()){
            row = iterator.next();
            String[] data = new String[row.getLastCellNum()];
            for (Cell cell : row) {
                // Process each cell based on its type
                switch (cell.getCellType()) {
                    case STRING:
                        data[cell.getColumnIndex()] = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        data[cell.getColumnIndex()] = ""+cell.getNumericCellValue();
                        break;
                    case BOOLEAN:
                        data[cell.getColumnIndex()] = "" + cell.getBooleanCellValue();
                        break;
                    default:

                }
            }
            dataTable.addRow(data);
        }

        // Close resources
        workbook.close();
        fis.close();
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
    public static void loadFile(DataTable dataTable, @NotNull String path) throws DuplicatedNameException, NotMatchSizeMetadata, IOException, FileFormatNotRecognisedException {
        //clear all the table
        dataTable.clearAll();

        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(String.join(",",FileManager.SUPPORTEDFORMATS),FileManager.SUPPORTEDFORMATS);
        File file = new File(path);
        if (!extensionFilter.accept(file)) throw new FileFormatNotRecognisedException();
        if (path.trim().toLowerCase().endsWith(".csv")) loadCSV(dataTable,path);
        if (path.trim().toLowerCase().endsWith(".xls") || path.trim().toLowerCase().endsWith(".xlsx")) loadXLSAndXSLX(dataTable,path);
    }

}
