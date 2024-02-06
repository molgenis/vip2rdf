package org.molgenis.vip2rdf.io;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.molgenis.vip2rdf.exceptions.InvalidInputFileException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Reads in a CSV file.
 * Automatically closes after {@link #read(Path, Class)} has been done. If using a custom {@link Reader}, this still
 * needs to be closed externally.
 * @param <T>
 */
public abstract class CsvFileReader<T> {
    protected void read(Path path, Class<T> beanClass) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            read(reader, beanClass);
        }
    }

    protected void read(Reader reader, Class<T> beanClass) throws IOException {
        List<T> beans = new CsvToBeanBuilder(reader).withType(beanClass).withQuoteChar(getQuoteChar())
                .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH).withSeparator(getSeperator()).build().parse();
        process(beans);
    }

    public abstract CsvFileReader read(Path path) throws IOException;

    public abstract CsvFileReader read(Reader reader) throws IOException;

    protected abstract void process(List<T> beans) throws InvalidInputFileException;

    protected char getSeperator() {
        return '\t';
    }

    protected char getQuoteChar() {
        return '"';
    }
}
