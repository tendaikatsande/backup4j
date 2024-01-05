package com.link.backup4j.app.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BackupRequest {
    private String host;
    @Builder.Default
    private int port = 5432;
    private String username;
    private String password;
    private String format;
    @Builder.Default
    private boolean withMetadata = true;
    @Builder.Default
    private boolean verbose = true;
    private String backupPath;
    private String databaseName;
}
