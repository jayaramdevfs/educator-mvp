package com.educator.homepage.controller;

import com.educator.homepage.dto.BlockConfigRequest;
import com.educator.homepage.entity.BlockConfig;
import com.educator.homepage.service.BlockConfigAdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/homepage/blocks/{blockId}/config")
public class BlockConfigAdminController {

    private final BlockConfigAdminService service;

    public BlockConfigAdminController(BlockConfigAdminService service) {
        this.service = service;
    }

    @PostMapping
    public BlockConfig createConfig(
            @PathVariable UUID blockId,
            @Valid @RequestBody BlockConfigRequest req
    ) {
        return service.createConfig(blockId, req);
    }

    @PutMapping
    public BlockConfig updateConfig(
            @PathVariable UUID blockId,
            @Valid @RequestBody BlockConfigRequest req
    ) {
        return service.updateConfig(blockId, req);
    }

    @DeleteMapping
    public void deleteConfig(@PathVariable UUID blockId) {
        service.deleteConfig(blockId);
    }

    @GetMapping
    public BlockConfig getConfig(@PathVariable UUID blockId) {
        return service.getConfig(blockId);
    }
}
