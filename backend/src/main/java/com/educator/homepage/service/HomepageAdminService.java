package com.educator.homepage.service;

import com.educator.homepage.dto.HomepageSectionRequest;
import com.educator.homepage.dto.SectionBlockRequest;
import com.educator.homepage.entity.HomepageSection;
import com.educator.homepage.entity.SectionBlock;
import com.educator.homepage.repository.HomepageSectionRepository;
import com.educator.homepage.repository.SectionBlockRepository;
import org.springframework.stereotype.Service;

@Service
public class HomepageAdminService {

    private final HomepageSectionRepository sectionRepo;
    private final SectionBlockRepository blockRepo;

    public HomepageAdminService(HomepageSectionRepository sectionRepo, SectionBlockRepository blockRepo) {
        this.sectionRepo = sectionRepo;
        this.blockRepo = blockRepo;
    }

    public HomepageSection createSection(HomepageSectionRequest req) {
        HomepageSection s = new HomepageSection();
        s.setTitle(req.title);
        s.setPosition(req.position);
        s.setOrderIndex(req.orderIndex);
        s.setEnabled(req.enabled);
        return sectionRepo.save(s);
    }

    public SectionBlock createBlock(SectionBlockRequest req) {
        SectionBlock b = new SectionBlock();
        b.setSectionId(req.sectionId);
        b.setBlockType(req.blockType);
        b.setOrderIndex(req.orderIndex);
        b.setEnabled(req.enabled);
        return blockRepo.save(b);
    }
}
