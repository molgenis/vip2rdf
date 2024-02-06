package org.molgenis.vip2rdf.formats;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.IRI;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class EnsemblGlossaryItem {
    private IRI iri;
    private String vepMatch;
    private String label;
    private Set<IRI> dbXref = new HashSet<>();

    public IRI getIri() {
        return iri;
    }

    public String getVepMatch() {
        return vepMatch;
    }

    public String getLabel() {
        return label;
    }

    public Set<IRI> getDbXref() {
        return dbXref;
    }

    public EnsemblGlossaryItem(IRI iri) {
        this.iri = requireNonNull(iri);
    }

    public EnsemblGlossaryItem(IRI iri, String label, Set<IRI> dbXref) {
        this.iri = requireNonNull(iri);
        this.label = requireNonNull(label);
        this.vepMatch = labelToVepMatch(requireNonNull(label));
        this.dbXref = requireNonNull(dbXref);
    }

    public boolean anyNull() {
        return vepMatch == null || label == null;
    }

    private <T> T setIfNull(T curVal, T newVal) {
        if(curVal != null) {
            throw new IllegalArgumentException("value was already set");
        }
        return newVal;
    }

    public void setLabel(String label) {
        this.label = setIfNull(this.label, requireNonNull(label));
        this.vepMatch = setIfNull(this.vepMatch, labelToVepMatch(requireNonNull(label)));
    }

    private String labelToVepMatch(String label) {
        return StringUtils.uncapitalize(label).replace(' ', '_');
    }

    public void addDbXref(Set<IRI> dbXref) {
        this.dbXref.addAll(dbXref);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnsemblGlossaryItem that = (EnsemblGlossaryItem) o;
        return Objects.equals(iri, that.iri) && Objects.equals(label, that.label) && Objects.equals(dbXref, that.dbXref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, label, dbXref);
    }

    @Override
    public String toString() {
        return "EnsemblGlossaryItem{" +
                "iri=" + iri +
                ", vepMatch='" + vepMatch + '\'' +
                ", label='" + label + '\'' +
                ", dbXref=" + dbXref +
                '}';
    }
}
