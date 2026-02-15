package com.educator.homepage.service;

import com.educator.homepage.dto.BlockConfigRequest;
import com.educator.homepage.entity.BlockConfig;
import com.educator.homepage.repository.BlockConfigRepository;
import com.educator.homepage.repository.SectionBlockRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BlockConfigAdminService {

    private final BlockConfigRepository configRepo;
    private final SectionBlockRepository blockRepo;

    public BlockConfigAdminService(
            BlockConfigRepository configRepo,
            SectionBlockRepository blockRepo
    ) {
        this.configRepo = configRepo;
        this.blockRepo = blockRepo;
    }

    // -------------------------------------------------
    // CREATE CONFIG (1 per block)
    // -------------------------------------------------
    public BlockConfig createConfig(UUID blockId, BlockConfigRequest req) {

        blockRepo.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        if (configRepo.findByBlockId(blockId).isPresent()) {
            throw new IllegalStateException("Config already exists for this block");
        }

        BlockConfig config = new BlockConfig();
        config.setBlockId(blockId);
        apply(config, req);

        return configRepo.save(config);
    }

    // -------------------------------------------------
    // UPDATE CONFIG
    // -------------------------------------------------
    public BlockConfig updateConfig(UUID blockId, BlockConfigRequest req) {

        BlockConfig config = configRepo.findByBlockId(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Config not found"));

        apply(config, req);

        return configRepo.save(config);
    }

    // -------------------------------------------------
    // DELETE CONFIG
    // -------------------------------------------------
    public void deleteConfig(UUID blockId) {

        BlockConfig config = configRepo.findByBlockId(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Config not found"));

        configRepo.delete(config);
    }

    // -------------------------------------------------
    // GET CONFIG
    // -------------------------------------------------
    public BlockConfig getConfig(UUID blockId) {

        return configRepo.findByBlockId(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Config not found"));
    }

    // -------------------------------------------------
    // Helper
    // -------------------------------------------------
    private void apply(BlockConfig config, BlockConfigRequest req) {

        config.setTitle(req.title);
        config.setSubtitle(req.subtitle);
        config.setImageUrl(req.imageUrl);
        config.setVideoUrl(req.videoUrl);
        config.setTargetType(req.targetType);
        config.setTargetId(req.targetId);
        config.setTargetUrl(req.targetUrl);
        config.setClickable(req.clickable);
    }
}
