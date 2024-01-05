package com.link.backup4j.app.services;

import com.link.backup4j.app.models.BackupConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BackupConfigService {


    Page<BackupConfig> getAll(Pageable pageable);

    BackupConfig create(BackupConfig backupConfig);

    Optional<BackupConfig> get(Long id);

    void update(Long id, BackupConfig backup);

    void delete(Long id);

}
