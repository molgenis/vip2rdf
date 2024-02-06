package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.molgenis.vip2rdf.helpers.SelfDeclaringEnum;

import java.util.HashSet;
import java.util.Set;

public enum DbXrefIriGenerator implements SelfDeclaringEnum<DbXrefIriGenerator> {
    UMLS("UMLS", new String[]{"http://linkedlifedata.com/resource/umls/id/", "http://bio2rdf.org/umls:"}),
    MSH("MSH", new String[]{"http://id.nlm.nih.gov/mesh/", "http://bioportal.bioontology.org/ontologies/MESH/", "http://bio2rdf.org/mesh:"}),
    SNOMEDCT_US("SNOMEDCT_US", new String[]{"http://snomed.info/sct/", "http://bioportal.bioontology.org/ontologies/SNOMEDCT/"}),
    OMIM("OMIM", new String[]{"http://bioportal.bioontology.org/ontologies/OMIM/", "http://bio2rdf.org/omim:"}),
    ICD10("ICD-10", new String[]{"http://bioportal.bioontology.org/ontologies/ICD10/"}),
    SO("SO", new String[]{AppNamespaceIdPrefix.SO.getIriPrefix()});

    private final String prefixMatch;
    private final String[] iriStarts;

    @Override
    public String getName() {
        return prefixMatch;
    }

    DbXrefIriGenerator(String prefixMatch, String[] iriStarts) {
        this.prefixMatch = prefixMatch;
        this.iriStarts = iriStarts;
    }

    public Set<IRI> generateIris(String postfix) {
        Set<IRI> iris = new HashSet<>();
        for (String iriStart : iriStarts) {
            iris.add(SimpleValueFactory.getInstance().createIRI(iriStart + postfix));
        }
        return iris;
    }

    /**
     * @see SelfDeclaringEnum#getEnumValue(Class, String) (Class, String)
     */
    public static DbXrefIriGenerator getEnumValue(String name) {
        return SelfDeclaringEnum.getEnumValue(DbXrefIriGenerator.class, name);
    }

    public static Set<IRI> generateFromField(String dbXrefField) {
        String[] splitString = dbXrefField.split(":");
        return getEnumValue(splitString[0]).generateIris(splitString[1]);
    }
}
