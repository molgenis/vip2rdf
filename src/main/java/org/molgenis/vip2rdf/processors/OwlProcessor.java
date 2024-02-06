package org.molgenis.vip2rdf.processors;

import org.eclipse.rdf4j.model.IRI;
import org.molgenis.vip2rdf.rdf.enums.DbXrefIriGenerator;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class OwlProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OwlProcessor.class);

    protected boolean isDbXref(OWLAnnotationAssertionAxiom axiom) {
        return axiom.getProperty().getIRI().toString().equals("http://www.geneontology.org/formats/oboInOwl#hasDbXref");
    }

    protected Set<IRI> processDbXref(OWLAnnotationAssertionAxiom axiom) {
        String value = axiom.getValue().asLiteral().get().getLiteral();
        try {
            return DbXrefIriGenerator.generateFromField(value);
        } catch (NoSuchElementException e) {
            logger.debug(value + " not processable by DbXrefIriGenerator");
        }
        return Collections.emptySet();
    }
}
