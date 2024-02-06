package org.molgenis.vip2rdf.formats;

import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vip2rdf.formats.EnsemblGlossaryItem;

import java.util.Set;

class EnsemblGlossaryItemTest {
    private final static ValueFactory factory = SimpleValueFactory.getInstance();

    @Test
    void validCapitalization() {
        EnsemblGlossaryItem actual = new EnsemblGlossaryItem(
                factory.createIRI("http://ensembl.org/glossary/RANDOM_URI"),
                "My CONSEQUENCE 3 name",
                Set.of(factory.createIRI("http://purl.obolibrary.org/obo/RANDOM_URI"))
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals("My CONSEQUENCE 3 name", actual.getLabel()),
                () -> Assertions.assertEquals("my_CONSEQUENCE_3_name", actual.getVepMatch())
        );
    }
}