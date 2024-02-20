package org.molgenis.vip2rdf.properties;

public enum AppProperties {
    APP_NAME,
    APP_VERSION;

    private String value;

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }


}
