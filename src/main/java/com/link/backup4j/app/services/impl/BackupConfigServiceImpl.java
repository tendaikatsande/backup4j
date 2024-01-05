package com.link.backup4j.app.services.impl;

import com.link.backup4j.app.models.BackupConfig;
import com.link.backup4j.app.repositories.BackupConfigRepository;
import com.link.backup4j.app.services.BackupConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BackupConfigServiceImpl implements BackupConfigService {


    @Autowired
    private BackupConfigRepository backupConfigRepository;

    @Override
    public Page<BackupConfig> getAll(Pageable pageable) {
        return backupConfigRepository.findAll(pageable);
    }

    @Override
    public BackupConfig create(BackupConfig backupConfig) {
        return backupConfigRepository.save(backupConfig);
    }

    @Override
    public Optional<BackupConfig> get(Long id) {
        return backupConfigRepository.findById(id);
    }

    @Override
    public void update(Long id, BackupConfig backup) {

    }

    @Override
    public void delete(Long id) {

    }


}
