package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Set;

public class IriEnumHelper {
    private final static ValueFactory iriFactory = SimpleValueFactory.getInstance();
    private final static OWLDataFactory owlFactory = OWLManager.createOWLOntologyManager().getOWLDataFactory();

    public static final IRI generateIri(Namespace namespace, String name) {
        return iriFactory.createIRI(namespace.getName() + name);
    }

    public static final IRI generateIri(String iri) {
        return iriFactory.createIRI(iri);
    }

    public static final IRI generateIri(OWLClass owlClass) {
        return iriFactory.createIRI(owlClass.getIRI().toString());
    }

    public static final Triple generateTriple(IRI subject, IRI predicate, String object) {
        return iriFactory.createTriple(subject, predicate, iriFactory.createLiteral(object));
    }

    public static final Triple generateLabelTriple(IRI subject, String object) {
        return generateTriple(subject, RdfsIri.LABEL.getIri(), object);
    }

    public static final Triple generateDescriptionTriple(IRI subject, String object) {
        return generateTriple(subject, DctermsIri.DESCRIPTION.getIri(), object);
    }

    public static final void addTriples(Model model, Set<Triple> triples) {
        triples.forEach(e -> model.add(e.getSubject(), e.getPredicate(), e.getObject()));
    }

    public static final Statement generateTriple(IRI subject, IRI predicate, Literal object) {
        return iriFactory.createStatement(subject, predicate, object);
    }

    public static final OWLClass generateOwlClass(Namespace namespace, String name) {
        return owlFactory.getOWLClass(namespace.getName() + name);
    }

    public static final OWLClass generateOwlClass(String iri) {
        return owlFactory.getOWLClass(iri);
    }
}
