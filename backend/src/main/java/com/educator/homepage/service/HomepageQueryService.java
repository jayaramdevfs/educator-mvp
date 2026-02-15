package com.educator.homepage.service;

import com.educator.homepage.dto.HomepageResponse;
import com.educator.homepage.entity.HomepageSection;
import com.educator.homepage.entity.SectionBlock;
import com.educator.homepage.repository.HomepageSectionRepository;
import com.educator.homepage.repository.SectionBlockRepository;
import com.educator.homepage.repository.BlockConfigRepository;
import com.educator.homepage.entity.BlockConfig;
import com.educator.homepage.enums.BlockTargetType;
import com.educator.course.CourseRepository;
import com.educator.course.CourseStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class HomepageQueryService {

    private final HomepageSectionRepository sectionRepo;
    private final SectionBlockRepository blockRepo;
    private final BlockConfigRepository blockConfigRepo;
    private final CourseRepository courseRepo;

    public HomepageQueryService(HomepageSectionRepository sectionRepo,
                                SectionBlockRepository blockRepo,
                                BlockConfigRepository blockConfigRepo,
                                CourseRepository courseRepo) {
        this.sectionRepo = sectionRepo;
        this.blockRepo = blockRepo;
        this.blockConfigRepo = blockConfigRepo;
        this.courseRepo = courseRepo;
    }

    public List<HomepageResponse> getHomepage() {
        return sectionRepo.findAllByEnabledTrueOrderByOrderIndexAsc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Page<HomepageResponse> getHomepage(Pageable pageable) {
        return sectionRepo.findAllByEnabledTrueOrderByOrderIndexAsc(pageable)
                .map(this::mapToResponse);
    }

    private HomepageResponse mapToResponse(HomepageSection section) {

        List<SectionBlock> rawBlocks =
                blockRepo.findAllBySectionIdAndEnabledTrueOrderByOrderIndexAsc(section.getId());

        List<SectionBlock> filteredBlocks = new ArrayList<>();

        for (SectionBlock block : rawBlocks) {

            BlockConfig config =
                    blockConfigRepo.findByBlockId(block.getId()).orElse(null);

            // Validate COURSE blocks only
            if (config != null && config.getTargetType() == BlockTargetType.COURSE) {
                if (!isValidCourseUrl(config.getTargetUrl())) {
                    continue; // Skip invalid/draft/deleted courses
                }
            }

            filteredBlocks.add(block);
        }

        return new HomepageResponse(section, filteredBlocks);
    }

    private boolean isValidCourseUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            // Expected format: /courses/{id}
            String[] parts = url.split("/");
            String idStr = parts[parts.length - 1];
            Long courseId = Long.parseLong(idStr);

            return courseRepo
                    .findByIdAndStatusAndIsDeletedFalse(courseId, CourseStatus.PUBLISHED)
                    .isPresent();

        } catch (Exception e) {
            return false; // Malformed URL or invalid ID
        }
    }
}
