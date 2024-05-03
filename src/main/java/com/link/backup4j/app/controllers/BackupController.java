package com.link.backup4j.app.controllers;

import com.link.backup4j.app.dto.BackupRequest;
import com.link.backup4j.app.models.Backup;
import com.link.backup4j.app.services.BackupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backup")
@CrossOrigin
public class BackupController {

    @Autowired
    private BackupsService backupsService;

    @PostMapping("/create")
    public String createBackup(@RequestBody BackupRequest request) {
        backupsService.createBackup(request);
        return "Backup created successfully.";
    }


    @GetMapping
    public Page<Backup> backups(Pageable pageable){
        return  backupsService.getAll(pageable);
    }
}