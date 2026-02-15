package com.educator.homepage.repository;

import com.educator.homepage.entity.BlockConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BlockConfigRepository extends JpaRepository<BlockConfig, UUID> {

    Optional<BlockConfig> findByBlockId(UUID blockId);

}
