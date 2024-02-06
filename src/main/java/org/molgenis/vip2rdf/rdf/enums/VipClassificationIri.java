package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.*;
import org.molgenis.vip2rdf.helpers.SelfDeclaringEnum;

public enum VipClassificationIri implements IriEnum, TripleEnum, SelfDeclaringEnum<VipClassificationIri> {
    LQ("vipc-lq", "Low quality", "LQ"),
    LB("vipc-b", "Benign", "B"),
    B("vipc-lb", "Likely benign", "LB"),
    VUS("vipc-vus", "Variant of uncertain significance", "VUS"),
    LP("vipc-lp", "Likely pathogenic", "LP"),
    P("vipc-p", "Pathogenic", "P");

    private final IRI iri;
    private final Triple[] triples;

    @Override
    public String getName() {
        return this.name();
    }

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

    VipClassificationIri(String name, String description, String label) {
        this.iri = IriEnumHelper.generateIri(getNamespace(), name);
        this.triples = new Triple[] {
                IriEnumHelper.generateLabelTriple(this.iri, label),
                IriEnumHelper.generateDescriptionTriple(this.iri, description)
        };
    }

    /**
     * @see SelfDeclaringEnum#getEnumValue(Class, String) (Class, String)
     */
    public static VipClassificationIri getEnumValue(String name) {
        return SelfDeclaringEnum.getEnumValue(VipClassificationIri.class, name);
    }
}
