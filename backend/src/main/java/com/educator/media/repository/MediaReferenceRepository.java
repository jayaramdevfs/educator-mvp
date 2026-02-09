package com.educator.media.repository;

import com.educator.media.entity.MediaReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MediaReferenceRepository extends JpaRepository<MediaReference, UUID> {
}
