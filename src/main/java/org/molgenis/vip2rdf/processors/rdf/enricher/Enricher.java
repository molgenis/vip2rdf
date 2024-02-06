package org.molgenis.vip2rdf.processors.rdf.enricher;

import org.molgenis.vip2rdf.formats.ModelEnhanced;

public interface Enricher {
    void process(ModelEnhanced model);
}
