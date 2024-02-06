package org.molgenis.vip2rdf.io;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

public class TurtleFileWriter {
    private Path path;
    private Model model;

    public TurtleFileWriter(Path path, Model model) {
        this.path = requireNonNull(path);
        this.model = requireNonNull(model);
    }

    public void write() throws IOException {
        try (Writer writer = Files.newBufferedWriter(path)) {
            Rio.write(model, Files.newBufferedWriter(path), RDFFormat.TURTLE);
        }
    }
}
