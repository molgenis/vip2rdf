package org.molgenis.vip2rdf;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum TestResourceRetriever {
    // Edited version based on: https://molgenis.github.io/vip/vip_giab_hg001.html (from 2023-06-22)
    VIP_VCF {
        @Override
        public String getName() {
            return "single_patient/vip.vcf";
        }
    },
    SAMPLE_SHEET {
        @Override
        public String getName() {
            return "single_patient/sample_sheet.tsv";
        }
    },
    // Expected RDF Turtle file based on other "_IT" resource files as input.
    RDF_OUTPUT_TTL {
        @Override
        public String getName() {
            return "single_patient/rdf_result.ttl";
        }
    },
    HP_OWL {
        @Override
        public String getName() {
            return "single_patient/hp.owl";
        }
    },
    ENSEMBL_GLOSSARY {
        @Override
        public String getName() {
            return "single_patient/ensembl-glossary.owl";
        }
    };

    /**
     * ClassLoader object to view test resource files. Test files can be retrieved using {@code getResource()}, where an
     * empty {@link String} will refer to the folder {@code target/test-classes}.
     */
    protected final String mainDir = Thread.currentThread().getContextClassLoader().getResource("").getFile();

    /**
     * Returns the full path of the test resource.
     * @return {@link String}
     */
    public String getFullPathString() {
        return mainDir + getName();
    }

    /**
     * Returns the full path of the test resource.
     * @return {@link Path}
     */
    public Path getFullPath() {
        return Paths.get(getFullPathString());
    }

    /**
     * Returns the name of the test resource.
     * @return {@link String}
     */
    public abstract String getName();

}
