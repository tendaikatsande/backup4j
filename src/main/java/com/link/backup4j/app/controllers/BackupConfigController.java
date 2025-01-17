package com.link.backup4j.app.controllers;

import com.link.backup4j.app.models.BackupConfig;
import com.link.backup4j.app.services.BackupConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/backup-config")
@CrossOrigin
public class BackupConfigController {


    @Autowired
    private BackupConfigService backupConfigService;

    @PostMapping
    public ResponseEntity<BackupConfig> create(@RequestBody @Validated BackupConfig backupConfig){
        backupConfig = backupConfigService.create(backupConfig);
        return ResponseEntity.created(URI.create("/backups/"+backupConfig.getId())).body(backupConfig);
    }

    @GetMapping
    public Page<BackupConfig> backupConfigs(Pageable pageable){
        return  backupConfigService.getAll(pageable);
    }



}