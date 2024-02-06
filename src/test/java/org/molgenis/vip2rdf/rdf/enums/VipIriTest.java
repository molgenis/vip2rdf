package org.molgenis.vip2rdf.rdf.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vip2rdf.rdf.enums.VipIri;

class VipIriTest {
    @Test
    void designValidity() {
        Assertions.assertEquals("urn:uuid:E4CBEA11-46B8-4B68-A202-B9FC8E5BE255#chrom",
                VipIri.CHROM.getIri().toString());
    }
}