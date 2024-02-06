package org.molgenis.vip2rdf.io;

public enum VepField {
    GENE("Gene"),
    SYMBOL("SYMBOL"),
    VIPC("VIPC"),
    CONSEQUENCE("Consequence"),
    FEATURE_TYPE("Feature_type"),
    FEATURE("Feature");

    private final String fieldName;

    public String getFieldName() {
        return fieldName;
    }

    VepField(String fieldName) {
        this.fieldName = fieldName;
    }
}
