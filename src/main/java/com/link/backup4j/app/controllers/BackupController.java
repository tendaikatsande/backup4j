package com.link.backup4j.app.controllers;

import com.link.backup4j.app.dto.BackupRequest;
import com.link.backup4j.app.models.Backup;
import com.link.backup4j.app.models.BackupConfig;
import com.link.backup4j.app.services.BackupConfigService;
import com.link.backup4j.app.services.BackupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URI;
import java.time.Instant;

@RestController
@RequestMapping("/backup")
public class BackupController {

    @Autowired
    private BackupsService backupsService;

    @PostMapping("/create")
    public String createBackup(@RequestBody BackupRequest request) {
        // Get the home directory
        String homeDirectory = System.getProperty("user.home");
        // Specify the relative path for the backup file
        String relativePath = "Documents/Dumps/"+request.getDatabaseName()+"/"+ Instant.now().toString() +".backup";
        // Combine the home directory with the relative path
        String backupPath = new File(homeDirectory, relativePath).getPath();
        request.setBackupPath(backupPath);
        backupsService.createBackup(request);
        return "Backup created successfully.";
    }


    @GetMapping
    public Page<Backup> backups(Pageable pageable){
        return  backupsService.getAll(pageable);
    }
}