package com.educator.importer.common;

import com.educator.importer.enums.ImportEntityType;
import com.educator.importer.enums.ImportSourceType;

public class ImportContext {

    private ImportEntityType entityType;
    private ImportSourceType sourceType;

    public ImportEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(ImportEntityType entityType) {
        this.entityType = entityType;
    }

    public ImportSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(ImportSourceType sourceType) {
        this.sourceType = sourceType;
    }
}
