package com.educator.homepage.dto;

import com.educator.homepage.entity.HomepageSection;
import com.educator.homepage.entity.SectionBlock;

import java.util.List;

public class HomepageResponse {
    public HomepageSection section;
    public List<SectionBlock> blocks;

    public HomepageResponse(HomepageSection section, List<SectionBlock> blocks) {
        this.section = section;
        this.blocks = blocks;
    }
}
