package org.molgenis.vip2rdf.formats;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class Patient {
    private String id;
    private SortedSet<String> hpoCodes;

    public String getId() {
        return id;
    }

    public Set<String> getHpoCodes() {
        return hpoCodes;
    }

    public Patient(String id, SortedSet<String> hpoCodes) {
        this.id = requireNonNull(id);
        this.hpoCodes = requireNonNull(hpoCodes);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", hpoCodes=" + hpoCodes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
