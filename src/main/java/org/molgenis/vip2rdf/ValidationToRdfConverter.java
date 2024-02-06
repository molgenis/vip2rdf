package org.molgenis.vip2rdf;

import htsjdk.variant.vcf.VCFFileReader;
import org.molgenis.vip2rdf.exceptions.InvalidRdfModelException;
import org.molgenis.vip2rdf.formats.EnsemblGlossary;
import org.molgenis.vip2rdf.formats.ModelEnhanced;
import org.molgenis.vip2rdf.formats.Patient;
import org.molgenis.vip2rdf.processors.EnsemblGlossaryGenerator;
import org.molgenis.vip2rdf.processors.RdfGenerator;
import org.molgenis.vip2rdf.io.OwlFileReader;
import org.molgenis.vip2rdf.io.SampleSheetReader;
import org.molgenis.vip2rdf.io.TurtleFileWriter;
import org.molgenis.vip2rdf.io.VipVcfReader;
import org.molgenis.vip2rdf.processors.rdf.enricher.HpoEnricher;
import org.molgenis.vip2rdf.processors.rdf.enricher.OwlEnricher;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class ValidationToRdfConverter {
    private static final Logger logger = LoggerFactory.getLogger(ValidationToRdfConverter.class);
    /**
     * [0] vip.vcf.gz
     * [1] sample_sheet.csv
     * [2] ensembl-glossary.owl (no restriction? https://github.com/Ensembl/ensembl-glossary/issues/9)-> https://github.com/Ensembl/ensembl-glossary/blob/93c46f58299411f3b7d76c040b9cb73e907084a2/ensembl-glossary.owl
     * [3] hp.owl (custom: https://hpo.jax.org/app/license) -> https://github.com/obophenotype/human-phenotype-ontology/releases/latest/download/hp.owl
     * [4] outputFile.ttl
     *
     * @param args The supplied files &
     */
    public static void main(String[] args) {
        try {
            ModelEnhanced model = processData(args);
            logger.info("### Writing output ttl file.");
            writeTurtle(Paths.get(args[4]), model);
        } catch (IOException | InvalidRdfModelException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * @see #main(String[])
     */
    static ModelEnhanced processData(String[] args) throws IOException {
        logger.info("### Generating model from vcf file");
        ModelEnhanced model = generateRdf(Paths.get(args[0]), Paths.get(args[1]), Paths.get(args[2]));
        logger.info("### Enriching model with hp.owl file");
        enrichWithOwlFile(HpoEnricher.class, model, Paths.get(args[3]));
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
        new TurtleFileWriter(outputFile, model.getModel()).write();
    }
}