package org.molgenis.vip2rdf.processors;

import htsjdk.variant.variantcontext.VariantContext;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.molgenis.vip2rdf.exceptions.InvalidInputFileException;
import org.molgenis.vip2rdf.io.VepField;
import org.molgenis.vip2rdf.io.VipVcfReader;
import org.molgenis.vip2rdf.formats.*;
import org.molgenis.vip2rdf.rdf.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class RdfGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RdfGenerator.class);

    private ModelBuilder builder = new ModelBuilder();
    private final Namespace modelNamespace = new UuidNamespace("");
    private final EnsemblGlossary ensemblGlossary;

    private static final ValueFactory factory = SimpleValueFactory.getInstance();

    private Map<String, IRI> processedVariants = new HashMap();

    private int variantCounter = 0;
    private int transcriptCounter = 0; // Resets every time variantCounter increases

    public RdfGenerator(EnsemblGlossary ensemblGlossary) {
        this.ensemblGlossary = requireNonNull(ensemblGlossary);
        AppNamespace.getAllNamespaces().forEach(e -> builder.setNamespace(e));
        builder.setNamespace(modelNamespace);
        addTriples(TripleEnum.getAllTriples(VipIri.class));
        addTriples(TripleEnum.getAllTriples(VipClassificationIri.class));
        addTriples(TripleEnum.getAllTriples(SioIri.class));
    }

    public RdfGenerator processVcf(VipVcfReader vipVcfReader) throws InvalidInputFileException {
        for (VariantContext vc : vipVcfReader.getReader()) {
            // Skip variant if duplicate ID is found
            if (processedVariants.containsKey(vc.getID())) {
                logger.warn(String.format("ID %s was found multiple times within the VCF file. Skipping duplicate ID...", vc.getID()));
            } else {
                // Some pre-processing steps.
                IRI variant = newVariant();
                List<String[]> transcripts = vipVcfReader.transcripts(vc);

                // Add Variant information.
                builder.subject(variant)
                        .add(RdfsIri.LABEL.getIri(), variant.getLocalName())
                        .add(RdfIri.TYPE.getIri(), SioIri.GENOMIC_SEQUENCE_VARIANT.getIri())
                        .add(VipIri.CHROM.getIri(), vc.getContig())
                        .add(VipIri.POS.getIri(), convertToInteger(vc.getStart()))
                        .add(VipIri.ID.getIri(), vc.getID())
                        .add(VipIri.REF.getIri(), vc.getReference().getDisplayString());
                vc.getAlternateAlleles().forEach(e -> builder.add(VipIri.ALT.getIri(), e.getDisplayString()));

                // Process transcripts
                System.out.println(String.format("%s has %s transcripts", variant, transcripts.size()));
                for(String[] transcript : transcripts) {
                    processTranscript(transcript, vipVcfReader, variant);
                }

                // Add variant ID to processed list.
                processedVariants.put(vc.getID(), variant);
            }
        }

        return this;
    }

    private void processTranscript(String[] transcript, VipVcfReader vipVcfReader, IRI variant) throws InvalidInputFileException {
        // Validating whether dealing with a transcript
        if(!vipVcfReader.getTranscriptField(transcript, VepField.FEATURE_TYPE).equals("Transcript")) {
            logger.debug("A non-transcript feature was found: " + Arrays.toString(transcript));
            return;
        }

        // Some preprocessing
        IRI transcriptIri = newTranscript();
        IRI gene = AppNamespace.NCBI_GENE_ID.generateIri(
                vipVcfReader.getTranscriptField(transcript, VepField.GENE));
        String geneLabel = vipVcfReader.getTranscriptField(transcript, VepField.SYMBOL);

        // Adds consequences (if not yet added)
        String[] consequences = vipVcfReader.getTranscriptField(transcript, VepField.CONSEQUENCE).split("&");
        logger.debug(String.format("%s has consequences %s",
                transcriptIri.toString().split("#")[1],
                Arrays.toString(consequences)));
        List<EnsemblGlossaryItem> ensemblGlossaryItems = new ArrayList<>();
        for (String consequence : consequences) {
            EnsemblGlossaryItem ensemblGlossaryItem = ensemblGlossary.get(consequence);
            if(ensemblGlossaryItem == null) {
                throw new InvalidInputFileException("ensembl-glossary missing data for consequence: " + consequence);
            }
            addEnsemblGlossaryItemToBuilder(ensemblGlossaryItem);
            ensemblGlossaryItems.add(ensemblGlossaryItem);
        }


        // Add gene/symbol data (if not already added)
        builder.add(gene, RdfIri.TYPE.getIri(), SioIri.GENE.getIri());
        builder.add(gene, RdfsIri.LABEL.getIri(), geneLabel);

        // Adds transcript to variant
        builder.add(variant,
                VipIri.HAS_TRANSCRIPT.getIri(),
                transcriptIri);

        // Add transcript
        builder.subject(transcriptIri);
        builder.add(RdfIri.TYPE.getIri(), SioIri.RNA_TRANSCRIPT.getIri());
        builder.add(RdfsIri.LABEL.getIri(), transcriptIri.getLocalName());
        builder.add(VipIri.GENE.getIri(),
                AppNamespace.NCBI_GENE_ID.generateIri(vipVcfReader.getTranscriptField(transcript, VepField.GENE)));
        builder.add(VipIri.VIPC.getIri(),
                VipClassificationIri.getEnumValue(vipVcfReader.getTranscriptField(transcript, VepField.VIPC)).getIri());
        builder.add(VipIri.HAS_FEATURE.getIri(),
                AppNamespace.REFSEQ.generateIri(vipVcfReader.getTranscriptField(transcript, VepField.FEATURE)));
        ensemblGlossaryItems.forEach(e -> builder.add(VipIri.CONSEQUENCE.getIri(), e.getIri()));
    }

    private void addEnsemblGlossaryItemToBuilder(EnsemblGlossaryItem item) {
        builder.add(item.getIri(), RdfsIri.LABEL.getIri(), item.getLabel());
        item.getDbXref().forEach(e -> builder.add(item.getIri(), RdfsIri.SEE_ALSO.getIri(), e));
    }

    /**
     * Adds patients to {@link Model}.
     * {@link #processVcf(VipVcfReader)} should NOT be called anymore after this method has been called!
     * @param patients
     */
    public RdfGenerator processPatients(Set<Patient> patients) {
        // Goes through the patients
        for(Patient patient : patients) {
            builder.subject(convertToModelIri("patient-" + patient.getId()))
                    .add(RdfsIri.LABEL.getIri(), patient.getId())
                    .add(RdfIri.TYPE.getIri(), SioIri.PATIENT.getIri());
            patient.getHpoCodes().forEach(this::processHpoCodes);
            // Quick workaround, only works in single-patient cases
            processedVariants.values().forEach(e -> builder.add(VipIri.HAS_VARIANT.getIri(), e));
        }
        return this;
    }

    private void processHpoCodes(String hpoId) {
        IRI iri = AppNamespaceIdPrefix.HPO.generateIri(hpoId.substring(3));
        builder.add(SioIri.HAS_PHENOTYPE.getIri(), iri);
        builder.add(iri, RdfIri.TYPE.getIri(), SioIri.PHENOTYPE.getIri());
    }

    public ModelEnhanced build() {
        return new ModelEnhanced(builder.build());
    }

    private IRI newVariant() {
        transcriptCounter = 0;
        return convertToModelIri("variant-" + ++variantCounter);
    }

    private IRI newTranscript() {
        return convertToModelIri("transcript-" + variantCounter + '.' + ++transcriptCounter);
    }

    private IRI convertToModelIri(Object text) {
        return factory.createIRI(modelNamespace.getName() + text);
    }

    private Literal convertToInteger(int value) {
        return factory.createLiteral(BigInteger.valueOf(value));
    }

    private void addTriples(Set<Triple> triples) {
        triples.forEach(e -> builder.add(e.getSubject(), e.getPredicate(), e.getObject()));
    }
}
