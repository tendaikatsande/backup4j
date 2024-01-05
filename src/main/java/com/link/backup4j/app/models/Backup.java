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
public class Backup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "backup_gen")
    @SequenceGenerator(name = "backup_gen", sequenceName = "backup_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    private String backupPath;
    @ManyToOne
    @JoinColumn(name = "backup_config_id")
    private BackupConfig backupConfig;
}