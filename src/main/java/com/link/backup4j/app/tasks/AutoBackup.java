package com.link.backup4j.app.tasks;

import com.link.backup4j.app.models.BackupConfig;
import com.link.backup4j.app.services.BackupsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AutoBackup {


    @Value("${backup.db.name}")
    private String databaseName;

    @Value("${backup.db.username}")
    private String username;
    @Value("${backup.db.password}")
    private String password;

    @Value("${backup.db.host}")
    private String host;

    @Value("${backup.db.port}")
    private Integer port;

    private final BackupsService backupService;

    public AutoBackup(BackupsService backupService) {
        this.backupService = backupService;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void scheduledBackup() {
        BackupConfig backupConfig = BackupConfig.builder().host(host).port(port).username(username).password(password).databaseName(databaseName).build();
        backupService.createBackup(backupConfig);
        log.info("{} cron test", backupConfig);
    }
}
