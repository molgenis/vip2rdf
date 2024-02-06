package org.molgenis.vip2rdf.formats;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.molgenis.vip2rdf.exceptions.InvalidRdfModelException;
import org.molgenis.vip2rdf.rdf.enums.AppNamespaceIdPrefix;
import org.molgenis.vip2rdf.rdf.enums.RdfsIri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class ModelEnhanced {
    private static final Logger logger = LoggerFactory.getLogger(ModelEnhanced.class);

    private Model model;
    private Set<IRI> orphaCodes;
    private Set<IRI> hpoCodes;

    public Model getModel() {
        return model;
    }

    public Set<IRI> getOrphaCodes() {
        return orphaCodes;
    }

    public Set<IRI> getHpoCodes() {
        return hpoCodes;
    }

    public ModelEnhanced(Model model) {
        this.model = requireNonNull(model);
        update();
    }

    public void validate() throws InvalidRdfModelException {
        boolean invalid = false;

        // Ensures every subject only has 1 label
        for(Resource resource : model.subjects()) {
            Set<Value> labels = model.filter(resource, RdfsIri.LABEL.getIri(), null).objects();
            if(labels.size() > 1) {
                invalid = true;
                logger.error(String.format("%s has multiple labels: %s", resource, labels));
            }
        }

        // Ensures exception is thrown after all invalid cases were logged
        if(invalid) {
            throw new InvalidRdfModelException("Generated model is invalid. Please contact the developers.");
        }
    }

    public void update() {
        this.hpoCodes = generateIriSetUsingFilter(AppNamespaceIdPrefix.HPO.getIriPrefix());
    }

    private Set<IRI> generateIriSetUsingFilter(String startsWith) {
        return this.model.objects().stream()
                .filter(e -> e.stringValue().startsWith(startsWith))
                .map(IRI.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelEnhanced that = (ModelEnhanced) o;
        return Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }

    @Override
    public String toString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Rio.write(getModel(), baos, RDFFormat.TURTLE);
        return baos.toString();
    }
}
