package org.molgenis.vip2rdf.io;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.nio.file.Path;

public interface OwlReader {
    public OWLOntology read(Path path) throws IOException, OWLOntologyCreationException;
}
