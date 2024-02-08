package org.molgenis.vip2rdf;

import org.apache.commons.collections4.SetUtils;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.molgenis.vip2rdf.VipToRdfConverter.processData;

class ValidationToRdfConverterIT {
    @Test
    void validRdfGeneration() throws IOException {
        Model expectedModel = Rio.parse(Files.newBufferedReader(TestResourceRetriever.RDF_OUTPUT_TTL.getFullPath()),
                RDFFormat.TURTLE);

        String[] args = new String[]{
                TestResourceRetriever.VIP_VCF.getFullPathString(),
                TestResourceRetriever.SAMPLE_SHEET.getFullPathString(),
                TestResourceRetriever.ENSEMBL_GLOSSARY.getFullPathString(),
                TestResourceRetriever.HP_OWL.getFullPathString()
        };

        // Mocking random UUID generation for equal comparison.
        Model actualModel;
        UUID uuid = UUID.fromString("cb173486-ccdd-4713-9dc1-2803ac63af9d");
        try(MockedStatic mock = mockStatic(UUID.class)) {
            mock.when(UUID::randomUUID).thenReturn(uuid);
            actualModel = processData(args).getModel();
        }

        // Prints data in expected model not present in actual model
        System.out.println("Data in expected model missing in actual model: " +
                SetUtils.difference(expectedModel, actualModel));
        // Prints data in actual model not present in expected model
        System.out.println("Data in actual model not present in expected model: " +
                SetUtils.difference(actualModel, expectedModel));

        Assertions.assertEquals(expectedModel, actualModel);
    }
}