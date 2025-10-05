package com.menu.menuitem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/menuItems")
@CrossOrigin
public class FileImportController {

    private final FileImportService fileImportService;

    public FileImportController(FileImportService fileImportService) {
        this.fileImportService = fileImportService;
    }

    @PostMapping("/import")
    public ResponseEntity<?> importMenuItems(@RequestParam("file") MultipartFile file) {
        try {
            List<MenuItemRequest> menuItems = fileImportService.processFile(file);
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }
}