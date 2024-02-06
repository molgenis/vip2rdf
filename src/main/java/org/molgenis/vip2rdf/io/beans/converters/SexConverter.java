package org.molgenis.vip2rdf.io.beans.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.molgenis.vcf.utils.sample.model.Sex;

public class SexConverter extends AbstractBeanField<Sex, Integer> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if(value == null) {
            return Sex.UNKNOWN;
        }
        return switch (value.toLowerCase()) {
            case "male" -> Sex.MALE;
            case "female" -> Sex.FEMALE;
            default -> throw new CsvConstraintViolationException("Illegal value for sex: " + value);
        };
    }
}
