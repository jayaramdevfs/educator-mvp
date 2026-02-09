package com.educator.importer.common;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {

    private boolean success;
    private final List<ImportError> errors = new ArrayList<>();

    public boolean isSuccess() {
        return success && errors.isEmpty();
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ImportError> getErrors() {
        return errors;
    }

    public void addError(ImportError error) {
        this.errors.add(error);
    }
}
