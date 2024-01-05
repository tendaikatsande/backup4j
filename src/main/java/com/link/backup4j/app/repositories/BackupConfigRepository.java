package com.link.backup4j.app.repositories;

import com.link.backup4j.app.models.BackupConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BackupConfigRepository extends JpaRepository<BackupConfig,Long>, PagingAndSortingRepository<BackupConfig,Long> {
}
