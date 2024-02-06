package org.molgenis.vip2rdf.formats;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Only allows {@link EnsemblGlossaryItem}{@code s} that are completely filled with values.
 */
public class EnsemblGlossary {
    private Map<String, EnsemblGlossaryItem> itemsByVepMatch = new HashMap<>();

    public EnsemblGlossary(Collection<EnsemblGlossaryItem> items) {
        requireNonNull(items);
        for(EnsemblGlossaryItem item : items) {
            if(item.anyNull()) {
                throw new NullPointerException();
            }
            itemsByVepMatch.put(item.getVepMatch(), item);
        }
    }

    public EnsemblGlossaryItem get(String item) {
        return itemsByVepMatch.get(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnsemblGlossary glossary = (EnsemblGlossary) o;
        return Objects.equals(itemsByVepMatch, glossary.itemsByVepMatch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemsByVepMatch);
    }

    @Override
    public String toString() {
        return "EnsemblGlossary{" +
                "itemsByVepMatch=" + itemsByVepMatch +
                '}';
    }
}
