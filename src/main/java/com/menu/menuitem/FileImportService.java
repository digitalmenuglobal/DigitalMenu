package com.menu.menuitem;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Service
public class FileImportService {

    public List<MenuItemRequest> processFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        List<MenuItemRequest> menuItems = new ArrayList<>();

        if (fileName.endsWith(".csv")) {
            menuItems = processCSV(file);
        } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
            menuItems = processExcel(file);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Please upload a CSV or Excel file.");
        }

        return menuItems;
    }

    private List<MenuItemRequest> processCSV(MultipartFile file) throws IOException {
        List<MenuItemRequest> menuItems = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            boolean isFirstRow = true;
            
            for (String[] row : rows) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip header row
                }
                
                MenuItemRequest item = createMenuItemFromRow(row);
                menuItems.add(item);
            }
        } catch (CsvException e) {
            throw new IOException("Error reading CSV file", e);
        }
        return menuItems;
    }

    private List<MenuItemRequest> processExcel(MultipartFile file) throws IOException {
        List<MenuItemRequest> menuItems = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;
            
            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip header row
                }
                
                MenuItemRequest item = createMenuItemFromExcelRow(row);
                menuItems.add(item);
            }
        }
        return menuItems;
    }

    private MenuItemRequest createMenuItemFromRow(String[] row) {
        MenuItemRequest item = new MenuItemRequest();
        try {
            item.setName(row[0]);
            item.setCategory(row[1]);
            item.setDescription(row[2]);
            item.setQuantity(row[3]);
            item.setType(row[4]);
            item.setPrice(Double.parseDouble(row[5]));
            item.setImage(row[6]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid data format in CSV row");
        }
        return item;
    }

    private MenuItemRequest createMenuItemFromExcelRow(Row row) {
        MenuItemRequest item = new MenuItemRequest();
        try {
            item.setName(getCellValueAsString(row.getCell(0)));
            item.setCategory(getCellValueAsString(row.getCell(1)));
            item.setDescription(getCellValueAsString(row.getCell(2)));
            item.setQuantity( row.getCell(3).getNumericCellValue());
            item.setType(getCellValueAsString(row.getCell(4)));
            item.setPrice(row.getCell(5).getNumericCellValue());
            item.setImage(getCellValueAsString(row.getCell(6)));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid data format in Excel row");
        }
        return item;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
}