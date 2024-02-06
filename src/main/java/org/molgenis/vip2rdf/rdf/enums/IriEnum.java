package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;

public interface IriEnum {
    public Namespace getNamespace();

    public IRI getIri();
}
