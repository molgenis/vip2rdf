package org.molgenis.vip2rdf.io;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class OwlFileReader implements OwlReader {
    private static OwlFileReader singleton = null;

    private OwlFileReader() {
    }

    @Override
    public OWLOntology read(Path path) throws IOException, OWLOntologyCreationException {
        try(InputStream stream = Files.newInputStream(path) ) {
            return OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(stream);
        }
    }

    public static OwlFileReader getInstance() {
        if (singleton == null) {
            singleton = new OwlFileReader();
        }
        return singleton;
    }
}
