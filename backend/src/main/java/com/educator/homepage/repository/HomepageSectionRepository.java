package com.educator.homepage.repository;

import com.educator.homepage.entity.HomepageSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HomepageSectionRepository extends JpaRepository<HomepageSection, UUID> {
    List<HomepageSection> findAllByEnabledTrueOrderByOrderIndexAsc();

    Page<HomepageSection> findAllByEnabledTrueOrderByOrderIndexAsc(Pageable pageable);
}
