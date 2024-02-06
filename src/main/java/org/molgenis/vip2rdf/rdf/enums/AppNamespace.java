package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum AppNamespace {
    RDF("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"),
    RDFS("rdfs", "http://www.w3.org/2000/01/rdf-schema#"),
    DCTERMS("dcterms", "http://purl.org/dc/terms/"),
    OBO("obo", "http://purl.obolibrary.org/obo/"),
    SIO("sio", "http://semanticscience.org/resource/"),
    VIP("vip", "urn:uuid:E4CBEA11-46B8-4B68-A202-B9FC8E5BE255#"),
    REFSEQ("refseq", "http://identifiers.org/refseq/"),
    NCBI_GENE_ID("ncbigene", "http://identifiers.org/ncbigene/"),
    ENSEMBL_GLOSSARY("ensembl", "http://ensembl.org/glossary/");

    private static final ValueFactory factory = SimpleValueFactory.getInstance();

    private final Namespace namespace;

    public Namespace getNamespace() {
        return namespace;
    }

    AppNamespace(String prefix, String name) {
        this.namespace = new SimpleNamespace(prefix, name);
    }

    public static Set<Namespace> getAllNamespaces() {
        return Arrays.stream(AppNamespace.values()).map(AppNamespace::getNamespace).collect(Collectors.toSet());
    }

    public IRI generateIri(String localName) {
        return factory.createIRI(namespace.getName() + localName);
    }

    public IRI generateIri(Integer localName) {
        return generateIri(Integer.toString(localName));
    }
}
