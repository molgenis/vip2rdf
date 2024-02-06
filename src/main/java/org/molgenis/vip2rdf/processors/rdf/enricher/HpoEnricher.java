package org.molgenis.vip2rdf.processors.rdf.enricher;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.molgenis.vip2rdf.formats.ModelEnhanced;
import org.molgenis.vip2rdf.rdf.enums.RdfsIri;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HpoEnricher extends OwlEnricher {
    private static final Logger logger = LoggerFactory.getLogger(HpoEnricher.class);
    private static final ValueFactory valueFactory = SimpleValueFactory.getInstance();

    public HpoEnricher(OWLOntology ontology) {
        super(ontology);
    }

    @Override
    public void process(ModelEnhanced model) {
        getOntology().axioms(AxiomType.ANNOTATION_ASSERTION).forEach(e -> processAnnotationAssertion(e, model));
    }

    private void processAnnotationAssertion(OWLAnnotationAssertionAxiom axiom, ModelEnhanced model) {
        IRI subjectIri = valueFactory.createIRI(axiom.getSubject().toString());

        // Checks if HPO from OWL file is present in model
        if(model.getHpoCodes().contains(subjectIri)) {
            // Adds label to model
            if (axiom.getProperty().isLabel()) {
                model.getModel().add(
                        subjectIri,
                        RdfsIri.LABEL.getIri(),
                        createLiteralFromAxiom(axiom)
                );
            }

            ifDbXrefThenProcess(axiom, subjectIri, model);
        }
    }
}
