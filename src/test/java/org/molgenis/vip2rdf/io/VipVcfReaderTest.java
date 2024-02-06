package org.molgenis.vip2rdf.io;

import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.molgenis.vip2rdf.io.VepField;
import org.molgenis.vip2rdf.io.VipVcfReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VipVcfReaderTest {
    private static final String CSQ = "CSQ";
    private static VCFReader vcfReader;
    private static final VCFInfoHeaderLine vcfInfoHeaderLine = new VCFInfoHeaderLine(CSQ, VCFHeaderLineCount.UNBOUNDED,
            VCFHeaderLineType.String, "INFO=<ID=CSQ,Number=.,Type=String,Description=\"Consequence annotations from Ensembl VEP. Format: Allele|Consequence|IMPACT|SYMBOL|Gene|Feature_type|Feature|BIOTYPE|EXON|INTRON|HGVSc|HGVSp|cDNA_position|CDS_position|Protein_position|Amino_acids|Codons|Existing_variation|ALLELE_NUM|DISTANCE|STRAND|FLAGS|PICK|SYMBOL_SOURCE|HGNC_ID|REFSEQ_MATCH|REFSEQ_OFFSET|SOURCE|SIFT|PolyPhen|HGVS_OFFSET|CLIN_SIG|SOMATIC|PHENO|PUBMED|CHECK_REF|MOTIF_NAME|MOTIF_POS|HIGH_INF_POS|MOTIF_SCORE_CHANGE|TRANSCRIPTION_FACTORS|Grantham|SpliceAI_pred_DP_AG|SpliceAI_pred_DP_AL|SpliceAI_pred_DP_DG|SpliceAI_pred_DP_DL|SpliceAI_pred_DS_AG|SpliceAI_pred_DS_AL|SpliceAI_pred_DS_DG|SpliceAI_pred_DS_DL|SpliceAI_pred_SYMBOL|CAPICE_CL|CAPICE_SC|existing_InFrame_oORFs|existing_OutOfFrame_oORFs|existing_uORFs|five_prime_UTR_variant_annotation|five_prime_UTR_variant_consequence|IncompletePenetrance|InheritanceModesGene|VKGL|VKGL_CL|ASV_ACMG_class|ASV_AnnotSV_ranking_criteria|ASV_AnnotSV_ranking_score|phyloP|gnomAD|gnomAD_AF|gnomAD_HN|clinVar|clinVar_CLNSIG|clinVar_CLNSIGINCL|clinVar_CLNREVSTAT|VIPC|VIPP\">");

    @BeforeAll
    static void beforeAll() {
        vcfReader = mock(VCFReader.class);
        VCFHeader vcfHeader = mock(VCFHeader.class);

        when(vcfReader.getHeader()).thenReturn(vcfHeader);
        when(vcfHeader.getInfoHeaderLine("CSQ")).thenReturn(vcfInfoHeaderLine);
    }

    @Test
    void transcriptListRetrieval() throws IOException {
        VariantContext vc = mock(VariantContext.class);
        when(vc.getAttributeAsStringList("CSQ", "")).thenReturn(Arrays.asList(
                "G|intron_variant|MODIFIER|CABIN1|23523|Transcript|NM_001199281.1|protein_coding||11/36|NM_001199281.1%3Ac.1400-28A>G|||||||rs56288125|1||1|||EntrezGene|||||||||||||||||||1|2|-15|36|0.69|0|0|0|CABIN1|VUS|0.20619261|||||||||||||-0.834999978542328|chr22%3A24059896-24059896|0.00163611|0|||||LP|chrom&gene&filter&vkgl&clinVar&gnomAD&annotSV&spliceAI&exit_lp",
                "G|intron_variant|MODIFIER|CABIN1|23523|Transcript|NM_001201429.2|protein_coding||10/35|NM_001201429.2%3Ac.1250-28A>G|||||||rs56288125|1||1|||EntrezGene|||||||||||||||||||1|2|-15|36|0.69|0|0|0|CABIN1|VUS|0.20619261|||||||||||||-0.834999978542328|chr22%3A24059896-24059896|0.00163611|0|||||LP|chrom&gene&filter&vkgl&clinVar&gnomAD&annotSV&spliceAI&exit_lp",
                "G|intron_variant|MODIFIER|CABIN1|23523|Transcript|NM_012295.4|protein_coding||11/36|NM_012295.4%3Ac.1400-28A>G|||||||rs56288125|1||1||1|EntrezGene|||||||||||||||||||1|2|-15|36|0.69|0|0|0|CABIN1|VUS|0.20619261|||||||||||||-0.834999978542328|chr22%3A24059896-24059896|0.00163611|0|||||LP|chrom&gene&filter&vkgl&clinVar&gnomAD&annotSV&spliceAI&exit_lp"
        ));

        VipVcfReader reader = new VipVcfReader(vcfReader);
        List<String[]> transcripts = reader.transcripts(vc);
        List<String> actualFieldValues = transcripts.stream().map(i -> reader.getTranscriptField(i, VepField.FEATURE)).toList();

        // Convert transcripts to list of lists due to array equals comparing if same object
        List<List<String>> actualTranscripts = transcripts.stream().map(i -> Arrays.stream(i).toList()).toList();

        List<List<String>> expectedTranscripts = List.of(
                List.of(new String[]{"G", "intron_variant", "MODIFIER", "CABIN1", "23523", "Transcript", "NM_001199281.1", "protein_coding", "", "11/36", "NM_001199281.1%3Ac.1400-28A>G", "", "", "", "", "", "", "rs56288125", "1", "", "1", "", "", "EntrezGene", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "1", "2", "-15", "36", "0.69", "0", "0", "0", "CABIN1", "VUS", "0.20619261", "", "", "", "", "", "", "", "", "", "", "", "", "-0.834999978542328", "chr22%3A24059896-24059896", "0.00163611", "0", "", "", "", "", "LP", "chrom&gene&filter&vkgl&clinVar&gnomAD&annotSV&spliceAI&exit_lp"}),
                List.of(new String[]{"G", "intron_variant", "MODIFIER", "CABIN1", "23523", "Transcript", "NM_001201429.2", "protein_coding", "", "10/35", "NM_001201429.2%3Ac.1250-28A>G", "", "", "", "", "", "", "rs56288125", "1", "", "1", "", "", "EntrezGene", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "1", "2", "-15", "36", "0.69", "0", "0", "0", "CABIN1", "VUS", "0.20619261", "", "", "", "", "", "", "", "", "", "", "", "", "-0.834999978542328", "chr22%3A24059896-24059896", "0.00163611", "0", "", "", "", "", "LP", "chrom&gene&filter&vkgl&clinVar&gnomAD&annotSV&spliceAI&exit_lp"}),
                List.of(new String[]{"G", "intron_variant", "MODIFIER", "CABIN1", "23523", "Transcript", "NM_012295.4", "protein_coding", "", "11/36", "NM_012295.4%3Ac.1400-28A>G", "", "", "", "", "", "", "rs56288125", "1", "", "1", "", "1", "EntrezGene", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "1", "2", "-15", "36", "0.69", "0", "0", "0", "CABIN1", "VUS", "0.20619261", "", "", "", "", "", "", "", "", "", "", "", "", "-0.834999978542328", "chr22%3A24059896-24059896", "0.00163611", "0", "", "", "", "", "LP", "chrom&gene&filter&vkgl&clinVar&gnomAD&annotSV&spliceAI&exit_lp"})
        );
        List<String> expectedFieldValues = List.of("NM_001199281.1", "NM_001201429.2", "NM_012295.4");

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedTranscripts, actualTranscripts),
                () -> Assertions.assertEquals(expectedFieldValues, actualFieldValues)
        );
    }


}