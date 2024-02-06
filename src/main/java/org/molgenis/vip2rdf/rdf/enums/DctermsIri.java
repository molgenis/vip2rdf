package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;

public enum DctermsIri implements IriEnum {
    DESCRIPTION("description");

    private final IRI iri;

    @Override
    public Namespace getNamespace() {
        return AppNamespace.DCTERMS.getNamespace();
    }

    @Override
    public IRI getIri() {
        return iri;
    }

    DctermsIri(String name) {
        this.iri = IriEnumHelper.generateIri(getNamespace(), name);
    }
}
