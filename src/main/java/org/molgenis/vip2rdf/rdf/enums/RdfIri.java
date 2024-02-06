package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;

public enum RdfIri implements IriEnum {
    TYPE("type");

    private final IRI iri;

    @Override
    public Namespace getNamespace() {
        return AppNamespace.RDF.getNamespace();
    }

    @Override
    public IRI getIri() {
        return iri;
    }

    RdfIri(String name) {
        this.iri = IriEnumHelper.generateIri(getNamespace(), name);
    }
}
