package org.molgenis.vip2rdf.io;

import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFReader;
import org.molgenis.vip2rdf.processors.RdfGenerator;
import org.molgenis.vcf.utils.metadata.FieldMetadataServiceImpl;
import org.molgenis.vcf.utils.model.NestedField;
import org.molgenis.vcf.utils.vep.VepMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class VipVcfReader implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(RdfGenerator.class);
    private static final String CSQ = "CSQ";

    private final Map<String, NestedField> csqFields;
    private final VCFReader reader;

    public VCFReader getReader() {
        return reader;
    }

    public VipVcfReader(VCFReader reader) {
        this.reader = reader;
        csqFields = new VepMetadataService(new FieldMetadataServiceImpl()).load(reader.getHeader().getInfoHeaderLine(CSQ)).getNestedFields();
    }

    public List<String[]> transcripts(VariantContext vc) {
        return vc.getAttributeAsStringList(CSQ, "").stream().map(i -> i.split("\\|")).toList();
    }

    public List<String> getTranscriptsField(List<String[]> transcripts, VepField field) {
        return transcripts.stream().map(e -> getTranscriptField(e, field)).toList();
    }

    public String getTranscriptField(String[] transcript, VepField field) {
        return transcript[csqFields.get(field.getFieldName()).getIndex()];
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
