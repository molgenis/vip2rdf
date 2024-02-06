package org.molgenis.vip2rdf.io;

import org.molgenis.vip2rdf.exceptions.InvalidInputFileException;
import org.molgenis.vip2rdf.formats.Patient;
import org.molgenis.vip2rdf.io.beans.SampleSheetBean;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.*;

public class SampleSheetReader extends CsvFileReader<SampleSheetBean> {
    private Map<String, Patient> patientsMap = new HashMap<>();

    public Set<Patient> getPatients() {
        return new HashSet<>(patientsMap.values());
    }

    @Override
    public SampleSheetReader read(Path path) throws IOException {
        read(path, SampleSheetBean.class);
        return this;
    }

    @Override
    public SampleSheetReader read(Reader reader) throws IOException {
        read(reader, SampleSheetBean.class);
        return this;
    }

    @Override
    protected void process(List<SampleSheetBean> beans) throws InvalidInputFileException {
        for(SampleSheetBean bean : beans) {
            Patient patient = patientsMap.get(bean.individualId);
            if (patient == null) {
                patientsMap.put(bean.individualId, new Patient(bean.individualId, bean.hpoIds));
            }
        }
    }
}
