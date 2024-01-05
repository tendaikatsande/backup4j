package com.link.backup4j.app.models;

import jakarta.persistence.*;
import lombok.*;

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BackupConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "backup_config_gen")
    @SequenceGenerator(name = "backup_config_gen", sequenceName = "backup_config_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    private String host;
    @Builder.Default
    private int port = 5432;
    private String username;
    private String password;
    private String format;
    @Builder.Default
    private boolean withMetadata = true;
    @Builder.Default
    @Column(name = "is_verbose")
    private boolean isVerbose = true;
    private String backupPath;
    private String databaseName;
}
