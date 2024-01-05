package com.link.backup4j.app.services.impl;

import com.link.backup4j.app.dto.BackupRequest;
import com.link.backup4j.app.models.Backup;
import com.link.backup4j.app.models.BackupConfig;
import com.link.backup4j.app.repositories.BackupRepository;
import com.link.backup4j.app.services.BackupConfigService;
import com.link.backup4j.app.services.BackupsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

@Service
@Slf4j
public class BackupServiceImpl implements BackupsService {

    @Autowired
    private BackupRepository backupRepository;

    @Autowired
    private BackupConfigService backupConfigService;


    @Override
    public Page<Backup> getAll(Pageable pageable) {
        return backupRepository.findAll(pageable);
    }
    @Override
    public void createBackup(BackupRequest backupRequest) {
        try {

            // Create the directory if it doesn't exist
            File directory = new File(backupRequest.getBackupPath()).getParentFile();
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    log.info("Directory created: {}", directory.getAbsolutePath());
                } else {
                    log.error("Failed to create directory: {}", directory.getAbsolutePath());
                    return;  // Exit the method if directory creation fails
                }
            }

            Process process = getProcess(backupRequest);

            logProcessOutput(process);

            int exitCode = process.waitFor();


            if (exitCode == 0) {
                log.info("BackupConfig created successfully.");
                Backup backup = new Backup();
                backup.setBackupPath(backupRequest.getBackupPath());

                Optional<BackupConfig> config = backupConfigService.get(1l);
                config.ifPresent(backup::setBackupConfig);
                backupRepository.save(backup);

            } else {
                log.error("Error creating backup. Exit code: {}", exitCode);
            }
        } catch (IOException | InterruptedException e) {
            log.error("BackupConfig process failed: {}", e.getMessage(), e);
        }
    }



    private static Process getProcess(BackupRequest backupRequest) throws IOException {

        ProcessBuilder processBuilder = new ProcessBuilder(
                "pg_dump",
                "-h", backupRequest.getHost(),
                "-p", String.valueOf(backupRequest.getPort()),
                "-U", backupRequest.getUsername(),
                "-F", backupRequest.getFormat(),
                backupRequest.isWithMetadata() ? "-b" : "",
                backupRequest.isVerbose() ? "-v" : "",
                "-f", backupRequest.getBackupPath(),
                // Provide the password file path
                backupRequest.getDatabaseName()
        );
// Set the PGPASSWORD environment variable with the database password
        processBuilder.environment().put("PGPASSWORD", backupRequest.getPassword());
        processBuilder.redirectErrorStream(true);  // Redirect error stream to input stream

        return processBuilder.start();
    }

    private void logProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("BackupConfig process output: {}", line);
            }
        }
    }



}
