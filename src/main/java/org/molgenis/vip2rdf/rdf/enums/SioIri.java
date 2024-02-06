package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Triple;

import static java.util.Objects.requireNonNull;

/**
 * Based on: https://raw.githubusercontent.com/MaastrichtU-IDS/semanticscience/master/ontology/sio/release/sio-release.owl
 */
public enum SioIri implements IriEnum, TripleEnum {
    IS_REPRESENTED_BY("SIO_000205", "is represented by"),
    PATIENT("SIO_000393", "patient"),
    HAS_PHENOTYPE("SIO_001279", "has phenotype"),
    GENOMIC_SEQUENCE_VARIANT("SIO_001381", "genomic sequence variant"),
    GENE_SYMBOL("SIO_001383", "gene symbol"),
    GENE("SIO_010035", "gene"),
    PHENOTYPE("SIO_010056", "phenotype"),
    RNA_TRANSCRIPT("SIO_010450", "RNA transcript");

    private final IRI iri;
    private final Triple[] triples;

    @Override
    public Namespace getNamespace() {
        return AppNamespace.SIO.getNamespace();
    }

    @Override
    public Triple[] getTriples() {
        return triples;
    }

    @Override
    public IRI getIri() {
        return iri;
    }

    SioIri(String name, String label) {
        this.iri = IriEnumHelper.generateIri(getNamespace(), requireNonNull(name));
        this.triples = new Triple[] {
            IriEnumHelper.generateLabelTriple(this.iri, label)
        };
    }
}
