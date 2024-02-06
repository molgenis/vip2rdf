package org.molgenis.vip2rdf.io.beans;

import com.opencsv.bean.*;
import org.molgenis.vip2rdf.io.beans.converters.AffectedStatusConverter;
import org.molgenis.vip2rdf.io.beans.converters.HpoConverter;
import org.molgenis.vip2rdf.io.beans.converters.SexConverter;
import org.molgenis.vcf.utils.sample.model.AffectedStatus;
import org.molgenis.vcf.utils.sample.model.Sex;

import java.util.SortedSet;

public class SampleSheetBean {
    private static final String LIST_SEPARATOR = ",";

    @CsvBindByName(column = "family_id")
    public String familyId;

    @CsvBindByName(column = "individual_id")
    public String individualId;

    @CsvBindByName(column = "paternal_id")
    public String paternalId;

    @CsvBindByName(column = "maternal_id")
    public String maternalId;

    @CsvCustomBindByName(converter = SexConverter.class)
    public Sex sex;

    @CsvCustomBindByName(converter = AffectedStatusConverter.class)
    public AffectedStatus affected;

    public boolean proband;

    @CsvBindAndSplitByName(column = "hpo_ids", elementType = String.class, splitOn = LIST_SEPARATOR, converter = HpoConverter.class)
    public SortedSet<String> hpoIds;

    public String assembly;

    public String vcf;
}
