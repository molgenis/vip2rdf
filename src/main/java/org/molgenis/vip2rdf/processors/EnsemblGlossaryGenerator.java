package org.molgenis.vip2rdf.processors;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.molgenis.vip2rdf.formats.EnsemblGlossary;
import org.molgenis.vip2rdf.formats.EnsemblGlossaryItem;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class EnsemblGlossaryGenerator extends OwlProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EnsemblGlossaryGenerator.class);
    private static final ValueFactory valueFactory = SimpleValueFactory.getInstance();

    public EnsemblGlossaryGenerator() {
    }

    public EnsemblGlossary process(OWLOntology ontology) {
        Map<IRI, EnsemblGlossaryItem> glossaryItemsByIri = new HashMap<>();
        ontology.axioms(AxiomType.ANNOTATION_ASSERTION).forEach(e -> processAnnotationAssertion(e, glossaryItemsByIri));
        return new EnsemblGlossary(glossaryItemsByIri.values());
    }

    private void processAnnotationAssertion(OWLAnnotationAssertionAxiom axiom,
                                            Map<IRI, EnsemblGlossaryItem> glossaryItemsByIri) {
        IRI subjectIri = valueFactory.createIRI(axiom.getSubject().toString());
        EnsemblGlossaryItem glossaryMatch = glossaryItemsByIri.get(subjectIri);
        if(glossaryMatch == null) {
            glossaryMatch = new EnsemblGlossaryItem(subjectIri);
            glossaryItemsByIri.put(subjectIri, glossaryMatch);
        }

        if(axiom.getProperty().isLabel()) {
            glossaryMatch.setLabel(axiom.getValue().asLiteral().get().getLiteral());
        } else if(isDbXref(axiom)) {
            glossaryMatch.addDbXref(processDbXref(axiom));
        }
    }
}
