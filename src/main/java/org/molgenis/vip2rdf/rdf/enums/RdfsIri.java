package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;

public enum RdfsIri implements IriEnum {
    LABEL("label"),
    SEE_ALSO("seeAlso");

    private final IRI iri;

    @Override
    public Namespace getNamespace() {
        return AppNamespace.RDFS.getNamespace();
    }

    @Override
    public IRI getIri() {
        return iri;
    }

    RdfsIri(String name) {
        this.iri = IriEnumHelper.generateIri(getNamespace(), name);
    }
}
