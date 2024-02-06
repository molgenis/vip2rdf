package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Triple;

public enum VipIri implements IriEnum, TripleEnum {
    CHROM("chrom", "chromosome"),
    POS("pos", "position"),
    ID("id", "id"),
    REF("ref", "ref"),
    ALT("alt", "alt"),
    GENE("gene", "gene"),
    CONSEQUENCE("consequence", "consequence"),
    VIPC("vipc", "classification"),
    HAS_VARIANT("has-variant", "has variant"),
    HAS_TRANSCRIPT("has-transcript", "has transcript"),
    HAS_FEATURE("has-feature", "has feature");

    private final IRI iri;
    private final Triple[] triples;

    @Override
    public Namespace getNamespace() {
        return AppNamespace.VIP.getNamespace();
    }

    @Override
    public IRI getIri() {
        return iri;
    }

    @Override
    public Triple[] getTriples() {
        return triples;
    }

    VipIri(String name, String label) {
        this.iri = IriEnumHelper.generateIri(getNamespace(), name);
        this.triples = new Triple[] {
                IriEnumHelper.generateLabelTriple(this.iri, label),
        };
    }
}
