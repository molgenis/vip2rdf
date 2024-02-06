package org.molgenis.vip2rdf.io.beans.converters;

import com.opencsv.bean.AbstractCsvConverter;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.regex.Pattern;

public class HpoConverter extends AbstractCsvConverter {
    private static final String HPO_REGEX = "^HP:\\d{7}$";

    @Override
    public Object convertToRead(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if(!Pattern.matches(HPO_REGEX, value)) {
            throw new CsvConstraintViolationException(String.format("%s is not a valid HPO id", value));
        }
        return value;
    }
}
