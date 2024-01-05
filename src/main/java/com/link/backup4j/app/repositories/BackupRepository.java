package com.link.backup4j.app.repositories;

import com.link.backup4j.app.models.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BackupRepository extends JpaRepository<Backup,Long>, PagingAndSortingRepository<Backup,Long> {
}
