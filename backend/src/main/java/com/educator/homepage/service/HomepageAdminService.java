package com.educator.homepage.service;

import com.educator.homepage.dto.HomepageSectionRequest;
import com.educator.homepage.dto.SectionBlockRequest;
import com.educator.homepage.entity.HomepageSection;
import com.educator.homepage.entity.SectionBlock;
import com.educator.homepage.repository.HomepageSectionRepository;
import com.educator.homepage.repository.SectionBlockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HomepageAdminService {

    private final HomepageSectionRepository sectionRepo;
    private final SectionBlockRepository blockRepo;

    public HomepageAdminService(HomepageSectionRepository sectionRepo,
                                SectionBlockRepository blockRepo) {
        this.sectionRepo = sectionRepo;
        this.blockRepo = blockRepo;
    }

    // -------------------------------------------------
    // SECTION METHODS (Already Implemented)
    // -------------------------------------------------

    public HomepageSection createSection(HomepageSectionRequest req) {
        HomepageSection s = new HomepageSection();
        s.setTitle(req.title);
        s.setPosition(req.position);
        s.setOrderIndex(req.orderIndex);
        s.setEnabled(req.enabled);
        return sectionRepo.save(s);
    }

    public HomepageSection updateSection(UUID sectionId, HomepageSectionRequest req) {
        HomepageSection section = sectionRepo.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));

        section.setTitle(req.title);
        section.setPosition(req.position);
        section.setOrderIndex(req.orderIndex);
        section.setEnabled(req.enabled);

        return sectionRepo.save(section);
    }

    public void deleteSection(UUID sectionId) {
        HomepageSection section = sectionRepo.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));

        section.setEnabled(false);
        sectionRepo.save(section);
    }

    // -------------------------------------------------
    // BLOCK CREATE
    // -------------------------------------------------

    public SectionBlock createBlock(SectionBlockRequest req) {
        SectionBlock b = new SectionBlock();
        b.setSectionId(req.sectionId);
        b.setBlockType(req.blockType);
        b.setOrderIndex(req.orderIndex);
        b.setEnabled(req.enabled);
        return blockRepo.save(b);
    }

    // -------------------------------------------------
    // BLOCK UPDATE (B4.7)
    // -------------------------------------------------

    public SectionBlock updateBlock(UUID blockId, SectionBlockRequest req) {

        SectionBlock block = blockRepo.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        block.setBlockType(req.blockType);
        block.setOrderIndex(req.orderIndex);
        block.setEnabled(req.enabled);

        return blockRepo.save(block);
    }

    // -------------------------------------------------
    // BLOCK DELETE (Soft)
    // -------------------------------------------------

    public void deleteBlock(UUID blockId) {

        SectionBlock block = blockRepo.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        block.setEnabled(false);
        blockRepo.save(block);
    }

    // -------------------------------------------------
    // BLOCK REORDER (Sibling shifting)
    // -------------------------------------------------

    public SectionBlock reorderBlock(UUID blockId, int newOrderIndex) {

        SectionBlock block = blockRepo.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        List<SectionBlock> siblings =
                blockRepo.findAllBySectionIdAndEnabledTrueOrderByOrderIndexAsc(block.getSectionId());

        int currentIndex = block.getOrderIndex();

        if (currentIndex == newOrderIndex) {
            return block;
        }

        for (SectionBlock sibling : siblings) {

            if (sibling.getId().equals(blockId)) {
                continue;
            }

            int siblingIndex = sibling.getOrderIndex();

            if (newOrderIndex > currentIndex) {
                if (siblingIndex > currentIndex && siblingIndex <= newOrderIndex) {
                    sibling.setOrderIndex(siblingIndex - 1);
                    blockRepo.save(sibling);
                }
            } else {
                if (siblingIndex >= newOrderIndex && siblingIndex < currentIndex) {
                    sibling.setOrderIndex(siblingIndex + 1);
                    blockRepo.save(sibling);
                }
            }
        }

        block.setOrderIndex(newOrderIndex);

        return blockRepo.save(block);
    }
}
