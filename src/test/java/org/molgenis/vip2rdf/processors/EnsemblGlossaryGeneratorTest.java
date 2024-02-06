package org.molgenis.vip2rdf.processors;

import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vip2rdf.formats.EnsemblGlossary;
import org.molgenis.vip2rdf.formats.EnsemblGlossaryItem;
import org.molgenis.vip2rdf.processors.EnsemblGlossaryGenerator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.ByteArrayInputStream;
import java.util.Set;

class EnsemblGlossaryGeneratorTest {
    private final static ValueFactory factory = SimpleValueFactory.getInstance();

    private static final String OWL = """
            <?xml version="1.0"?>
            <rdf:RDF xmlns="http://purl.obolibrary.org/obo/ensembl-glossary.owl#"
                 xml:base="http://purl.obolibrary.org/obo/ensembl-glossary.owl"
                 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                 xmlns:owl="http://www.w3.org/2002/07/owl#"
                 xmlns:oboInOwl="http://www.geneontology.org/formats/oboInOwl#"
                 xmlns:xml="http://www.w3.org/XML/1998/namespace"
                 xmlns:property="http://ensembl.org/glossary/property/"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
                 xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                 xmlns:obo="http://purl.obolibrary.org/obo/">
                <owl:Ontology rdf:about="http://purl.obolibrary.org/obo/ensembl-glossary.owl">
                    <owl:versionIRI rdf:resource="http://ensembl.org/glossary/ensembl-glossary/releases/2023-01-04/ensembl-glossary.owl"/>
                    <rdfs:comment>ensembl-glossary</rdfs:comment>
                </owl:Ontology>
               
                <!-- http://ensembl.org/glossary/ENSGLOSSARY_0000150 -->
                <owl:Class rdf:about="http://ensembl.org/glossary/ENSGLOSSARY_0000150">
                    <rdfs:subClassOf rdf:resource="http://ensembl.org/glossary/ENSGLOSSARY_0000134"/>
                    <property:documentation rdf:datatype="http://www.w3.org/2001/XMLSchema#string">http://www.ensembl.org/info/genome/variation/predicted_data.html</property:documentation>
                    <obo:IAO_0000115 rdf:datatype="http://www.w3.org/2001/XMLSchema#string">A sequence variant, that changes one or more bases, resulting in a different amino acid sequence but where the length is preserved</obo:IAO_0000115>
                    <oboInOwl:hasDbXref rdf:datatype="http://www.w3.org/2001/XMLSchema#string">SO:0001583</oboInOwl:hasDbXref>
                    <rdfs:label rdf:datatype="http://www.w3.org/2001/XMLSchema#string">Missense variant</rdfs:label>
                </owl:Class>
                
                <!-- http://ensembl.org/glossary/ENSGLOSSARY_0000002 -->
                <owl:Class rdf:about="http://ensembl.org/glossary/ENSGLOSSARY_0000002">
                    <rdfs:subClassOf rdf:resource="http://ensembl.org/glossary/ENSGLOSSARY_0000001"/>
                    <obo:IAO_0000115 rdf:datatype="http://www.w3.org/2001/XMLSchema#string">Genomic locus where transcription occurs. A gene may have one or more transcripts, which may or may not encode proteins.</obo:IAO_0000115>
                    <oboInOwl:hasDbXref rdf:datatype="http://www.w3.org/2001/XMLSchema#string">SO:0000704</oboInOwl:hasDbXref>
                    <oboInOwl:hasDbXref rdf:datatype="http://www.w3.org/2001/XMLSchema#string">https://en.wikipedia.org/wiki/Gene</oboInOwl:hasDbXref>
                    <rdfs:label rdf:datatype="http://www.w3.org/2001/XMLSchema#string">Gene</rdfs:label>
                </owl:Class>
            </rdf:RDF>
            """;

    @Test
    void validOwlCheck() throws OWLOntologyCreationException {
        EnsemblGlossary expected = new EnsemblGlossary(Set.of(
                new EnsemblGlossaryItem(
                        factory.createIRI("http://ensembl.org/glossary/ENSGLOSSARY_0000150"),
                        "Missense variant",
                        Set.of(factory.createIRI("http://purl.obolibrary.org/obo/SO_0001583"))
                ),
                new EnsemblGlossaryItem(
                        factory.createIRI("http://ensembl.org/glossary/ENSGLOSSARY_0000002"),
                        "Gene",
                        Set.of(factory.createIRI("http://purl.obolibrary.org/obo/SO_0000704"))
                )
        ));

        EnsemblGlossary actual = new EnsemblGlossaryGenerator()
                .process(OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(new ByteArrayInputStream(OWL.getBytes())));

        Assertions.assertEquals(expected, actual);
    }
}