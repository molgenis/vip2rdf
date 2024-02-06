package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;

public enum AppNamespaceIdPrefix {
    SO(AppNamespace.OBO, "SO_"),
    HPO(AppNamespace.OBO, "HP_");

    private final AppNamespace appNamespace;
    private final String idPrefix;

    public Namespace getNamespace() {
        return appNamespace.getNamespace();
    }

    public String getIriPrefix() {
        return appNamespace.getNamespace().getName() + idPrefix;
    }

    AppNamespaceIdPrefix(AppNamespace appNamespace, String idPrefix) {
        this.appNamespace = appNamespace;
        this.idPrefix = idPrefix;
    }

    public IRI generateIri(String localName) {
        return appNamespace.generateIri(idPrefix + localName);
    }

    public IRI generateIri(Integer localName) {
        return appNamespace.generateIri(idPrefix + localName);
    }
}
