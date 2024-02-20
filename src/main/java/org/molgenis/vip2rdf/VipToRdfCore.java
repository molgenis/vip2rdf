package org.molgenis.vip2rdf;

import htsjdk.variant.vcf.VCFFileReader;
import org.molgenis.vip2rdf.exceptions.InvalidRdfModelException;
import org.molgenis.vip2rdf.formats.EnsemblGlossary;
import org.molgenis.vip2rdf.formats.ModelEnhanced;
import org.molgenis.vip2rdf.formats.Patient;
import org.molgenis.vip2rdf.io.OwlFileReader;
import org.molgenis.vip2rdf.io.SampleSheetReader;
import org.molgenis.vip2rdf.io.TurtleFileWriter;
import org.molgenis.vip2rdf.io.VipVcfReader;
import org.molgenis.vip2rdf.processors.EnsemblGlossaryGenerator;
import org.molgenis.vip2rdf.processors.RdfGenerator;
import org.molgenis.vip2rdf.processors.rdf.enricher.HpoEnricher;
import org.molgenis.vip2rdf.processors.rdf.enricher.OwlEnricher;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Set;

public class VipToRdfCore {
    private static final Logger logger = LoggerFactory.getLogger(VipToRdfCore.class);

    public VipToRdfCore() {
    }

    static ModelEnhanced processData(Path vcfPath, Path sampleSheetPath, Path ensemblGlossaryPath, Path hpoOWlPath)
            throws IOException {
        logger.info("### Generating model from vcf file");
        ModelEnhanced model = generateRdf(vcfPath, sampleSheetPath, ensemblGlossaryPath);
        logger.info("### Enriching model with hp.owl file");
        enrichWithOwlFile(HpoEnricher.class, model, hpoOWlPath);
        return model;
    }

    static ModelEnhanced generateRdf(Path vcfFiles, Path sampleSheet, Path ensemblGlossaryFile) throws IOException {
        // Load data
        Set<Patient> patients = new SampleSheetReader().read(sampleSheet).getPatients();
        EnsemblGlossary ensemblGlossary = loadEnsemblGlossary(ensemblGlossaryFile);

        // Generate Model using vcf data & patients set
        try (VipVcfReader vipVcfReader = new VipVcfReader(new VCFFileReader(vcfFiles, false))) {
            return new RdfGenerator(ensemblGlossary).processVcf(vipVcfReader).processPatients(patients).build();
        }
    }

    static EnsemblGlossary loadEnsemblGlossary(Path ensemblGlossaryFile) throws IOException {
        try {
            return new EnsemblGlossaryGenerator().process(OwlFileReader.getInstance().read(ensemblGlossaryFile));
        } catch (OWLOntologyCreationException e) {
            throw new IOException(e);
        }
    }

    static void enrichWithOwlFile(Class<? extends OwlEnricher> enricher, ModelEnhanced model, Path owlFile) throws IOException {
        try {
            enricher.getDeclaredConstructor(OWLOntology.class)
                    .newInstance(OwlFileReader.getInstance().read(owlFile))
                    .process(model);
        } catch (OWLOntologyCreationException e) {
            throw new IOException(e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    static void writeTurtle(Path outputFile, ModelEnhanced model) throws IOException, InvalidRdfModelException {
        model.validate();
        logger.info("### Writing output ttl file.");
        new TurtleFileWriter(outputFile, model.getModel()).write();
    }
}
