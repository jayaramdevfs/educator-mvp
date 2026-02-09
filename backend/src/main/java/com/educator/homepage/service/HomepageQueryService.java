package com.educator.homepage.service;

import com.educator.homepage.dto.HomepageResponse;
import com.educator.homepage.entity.HomepageSection;
import com.educator.homepage.repository.HomepageSectionRepository;
import com.educator.homepage.repository.SectionBlockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomepageQueryService {

    private final HomepageSectionRepository sectionRepo;
    private final SectionBlockRepository blockRepo;

    public HomepageQueryService(HomepageSectionRepository sectionRepo, SectionBlockRepository blockRepo) {
        this.sectionRepo = sectionRepo;
        this.blockRepo = blockRepo;
    }

    public List<HomepageResponse> getHomepage() {
        return sectionRepo.findAllByEnabledTrueOrderByOrderIndexAsc()
                .stream()
                .map(section -> new HomepageResponse(
                        section,
                        blockRepo.findAllBySectionIdAndEnabledTrueOrderByOrderIndexAsc(section.getId())
                ))
                .collect(Collectors.toList());
    }
}
