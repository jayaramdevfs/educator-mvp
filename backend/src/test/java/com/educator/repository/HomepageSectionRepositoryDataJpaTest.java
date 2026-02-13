package com.educator.repository;

import com.educator.homepage.entity.HomepageSection;
import com.educator.homepage.enums.SectionPosition;
import com.educator.homepage.repository.HomepageSectionRepository;
import com.educator.homepage.repository.SectionBlockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HomepageSectionRepositoryDataJpaTest {

    @Autowired
    private HomepageSectionRepository sectionRepository;

    @Autowired
    private SectionBlockRepository blockRepository;

    @Test
    void findAllByEnabledTrueOrderByOrderIndexAsc_returnsEnabledSectionsOrdered() {
        saveSection("B", 2, true);
        saveSection("A", 1, true);
        saveSection("Disabled", 3, false);

        assertThat(sectionRepository.findAllByEnabledTrueOrderByOrderIndexAsc())
                .extracting(HomepageSection::getTitle)
                .containsExactly("A", "B");

        assertThat(sectionRepository.findAllByEnabledTrueOrderByOrderIndexAsc(PageRequest.of(0, 10)).getContent())
                .extracting(HomepageSection::getTitle)
                .containsExactly("A", "B");
    }

    @Test
    void sectionBlockRepository_returnsEnabledBlocksOrderedByIndex() {
        HomepageSection section = saveSection("Section", 1, true);

        var block3 = new com.educator.homepage.entity.SectionBlock();
        block3.setSectionId(section.getId());
        block3.setBlockType(com.educator.homepage.enums.BlockType.TEXT);
        block3.setOrderIndex(3);
        block3.setEnabled(true);
        blockRepository.save(block3);

        var block1 = new com.educator.homepage.entity.SectionBlock();
        block1.setSectionId(section.getId());
        block1.setBlockType(com.educator.homepage.enums.BlockType.CTA);
        block1.setOrderIndex(1);
        block1.setEnabled(true);
        blockRepository.save(block1);

        var block2Disabled = new com.educator.homepage.entity.SectionBlock();
        block2Disabled.setSectionId(section.getId());
        block2Disabled.setBlockType(com.educator.homepage.enums.BlockType.COURSE);
        block2Disabled.setOrderIndex(2);
        block2Disabled.setEnabled(false);
        blockRepository.save(block2Disabled);

        assertThat(blockRepository.findAllBySectionIdAndEnabledTrueOrderByOrderIndexAsc(section.getId()))
                .extracting(com.educator.homepage.entity.SectionBlock::getOrderIndex)
                .containsExactly(1, 3);
    }

    private HomepageSection saveSection(String title, int orderIndex, boolean enabled) {
        HomepageSection section = new HomepageSection();
        section.setTitle(title);
        section.setPosition(SectionPosition.TOP);
        section.setOrderIndex(orderIndex);
        section.setEnabled(enabled);
        return sectionRepository.save(section);
    }
}


