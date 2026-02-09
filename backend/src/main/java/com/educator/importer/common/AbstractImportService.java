package com.educator.importer.common;

public abstract class AbstractImportService<T> {

    /**
     * Executes an import and returns a result.
     */
    public abstract ImportResult importData(T input);
}
