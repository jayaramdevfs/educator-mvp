package com.educator.homepage.repository;

import com.educator.homepage.entity.SectionBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SectionBlockRepository extends JpaRepository<SectionBlock, UUID> {
    List<SectionBlock> findAllBySectionIdAndEnabledTrueOrderByOrderIndexAsc(UUID sectionId);
}
