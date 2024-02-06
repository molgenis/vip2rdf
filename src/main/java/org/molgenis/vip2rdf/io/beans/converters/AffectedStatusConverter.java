package org.molgenis.vip2rdf.io.beans.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.molgenis.vcf.utils.sample.model.AffectedStatus;

public class AffectedStatusConverter extends AbstractBeanField<AffectedStatus, Integer> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if(value == null) {
            return AffectedStatus.MISSING;
        }
        return switch (value.toLowerCase()) {
            case "true" -> AffectedStatus.AFFECTED;
            case "false" -> AffectedStatus.UNAFFECTED;
            default -> throw new CsvConstraintViolationException("Illegal value for affected status: " + value);
        };
    }
}
