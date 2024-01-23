package com.link.backup4j.app.services;

import com.link.backup4j.app.dto.BackupRequest;
import com.link.backup4j.app.models.Backup;
import com.link.backup4j.app.models.BackupConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BackupsService {
    void createBackup(BackupRequest backupRequest);

    Page<Backup> getAll(Pageable pageable);

    void createBackup(BackupConfig backupConfig);
}
