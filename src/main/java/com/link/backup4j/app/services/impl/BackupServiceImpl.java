package com.link.backup4j.app.services.impl;

import com.link.backup4j.app.dto.BackupRequest;
import com.link.backup4j.app.dto.EmailDetails;
import com.link.backup4j.app.models.Backup;
import com.link.backup4j.app.models.BackupConfig;
import com.link.backup4j.app.repositories.BackupRepository;
import com.link.backup4j.app.services.BackupConfigService;
import com.link.backup4j.app.services.BackupsService;
import com.link.backup4j.app.services.EmailService;
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
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class BackupServiceImpl implements BackupsService {

    private final BackupRepository backupRepository;

    private final BackupConfigService backupConfigService;

    private final EmailService emailService;

    public BackupServiceImpl(BackupRepository backupRepository, BackupConfigService backupConfigService, EmailService emailService) {
        this.backupRepository = backupRepository;
        this.backupConfigService = backupConfigService;
        this.emailService = emailService;
    }


    @Override
    public Page<Backup> getAll(Pageable pageable) {
        return backupRepository.findAll(pageable);
    }
    @Override
    public void createBackup(BackupRequest backupRequest) {
        BackupConfig backupConfig = backupConfigService.get(backupRequest.getBackConfigId()).orElse(null);
        if(backupConfig ==null) return;
        // Get the home directory
        String homeDirectory = System.getProperty("user.home");
        // Specify the relative path for the backup file
        String relativePath = "Documents/Dumps/"+backupConfig.getDatabaseName()+"/"+ Instant.now().toString() +".backup";
        // Combine the home directory with the relative path
        String backupPath = new File(homeDirectory, relativePath).getPath();
        backupConfig.setBackupPath(backupPath);


        try {

            // Create the directory if it doesn't exist
            File directory = new File(backupConfig.getBackupPath()).getParentFile();
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    log.info("Directory created: {}", directory.getAbsolutePath());
                } else {
                    log.error("Failed to create directory: {}", directory.getAbsolutePath());
                    return;  // Exit the method if directory creation fails
                }
            }

            Process process = getProcess(backupConfig);

            logProcessOutput(process);

            int exitCode = process.waitFor();


            if (exitCode == 0) {
                log.info("BackupConfig created successfully.");
                Backup backup = new Backup();
                backup.setBackupPath(backupConfig.getBackupPath());
                backup.setBackupConfig(backupConfig);
                backupRepository.save(backup);

                //send mail
                EmailDetails details = EmailDetails.builder().recipient("cmugabe@csam.co.zw").msgBody("Backup").subject("Backup").attachment(backup.getBackupPath()).build();
                emailService.sendMailWithAttachment(details);


            } else {
                log.error("Error creating backup. Exit code: {}", exitCode);
            }
        } catch (IOException | InterruptedException e) {
            log.error("BackupConfig process failed: {}", e.getMessage(), e);
        }
    }



    private static Process getProcess(BackupConfig backupConfig) throws IOException {

        ProcessBuilder processBuilder = new ProcessBuilder(
                "pg_dump",
                "-h", backupConfig.getHost(),
                "-p", String.valueOf(backupConfig.getPort()),
                "-U", backupConfig.getUsername(),
                "-F", backupConfig.getFormat(),
                backupConfig.isWithMetadata() ? "-b" : "",
                backupConfig.isVerbose() ? "-v" : "",
                "-f", backupConfig.getBackupPath(),
                // Provide the password file path
                backupConfig.getDatabaseName()
        );
        // Set the PGPASSWORD environment variable with the database password
        processBuilder.environment().put("PGPASSWORD", backupConfig.getPassword());
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
