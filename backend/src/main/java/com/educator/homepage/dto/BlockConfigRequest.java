package com.educator.homepage.dto;

import com.educator.homepage.enums.BlockTargetType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class BlockConfigRequest {

    public String title;
    public String subtitle;
    public String imageUrl;
    public String videoUrl;

    @NotNull
    public BlockTargetType targetType;

    public UUID targetId;
    public String targetUrl;

    public boolean clickable = true;
}
