package org.molgenis.vip2rdf.processors.rdf.enricher;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.molgenis.vip2rdf.formats.ModelEnhanced;
import org.molgenis.vip2rdf.processors.OwlProcessor;
import org.molgenis.vip2rdf.rdf.enums.RdfsIri;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OwlEnricher extends OwlProcessor implements Enricher {
    private static final Logger logger = LoggerFactory.getLogger(OwlEnricher.class);

    private final OWLOntology ontology;

    OWLOntology getOntology() {
        return ontology;
    }

    public OwlEnricher(OWLOntology ontology) {
        this.ontology = ontology;
    }

    Literal createLiteralFromAxiom(OWLAnnotationAssertionAxiom axiom) {
        return SimpleValueFactory.getInstance().createLiteral(axiom.getValue().asLiteral().get().getLiteral());
    }

    void ifDbXrefThenProcess(OWLAnnotationAssertionAxiom axiom, IRI subject, ModelEnhanced model) {
        if(isDbXref(axiom)) {
            processDbXref(axiom).forEach(iri -> model.getModel().add(subject, RdfsIri.SEE_ALSO.getIri(), iri));
        }
    }
}
