package org.molgenis.vip2rdf.processors.rdf.enricher;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vip2rdf.formats.ModelEnhanced;
import org.molgenis.vip2rdf.rdf.enums.RdfsIri;
import org.molgenis.vip2rdf.processors.rdf.enricher.HpoEnricher;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

class HpoEnricherTest {
    private final static ValueFactory factory = SimpleValueFactory.getInstance();

    private static final String VALID_TURTLE = """
            @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
            @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
            @prefix dcterms: <http://purl.org/dc/terms/> .
            @prefix so: <http://purl.obolibrary.org/obo/> .
            @prefix sio: <http://semanticscience.org/resource/> .
            @prefix ordo: <http://www.orpha.net/ORDO/> .
            @prefix hgnc: <http://identifiers.org/hgnc.symbol/> .
            @prefix vip: <urn:uuid:E4CBEA11-46B8-4B68-A202-B9FC8E5BE255#> .
            @prefix : <urn:uuid:cb173486-ccdd-4713-9dc1-2803ac63af9d#> .

            :patient-100001 a vip:patient ;
                rdfs:label "patient-100001" ;
            	sio:SIO_000008 ordo:Orphanet_94064 .

            ordo:Orphanet_94064 sio:SIO_000001 so:HP_0000407 .

            so:HP_0000407 a so:SIO_010056 .
            """;

    private static final String OWL = """
            <?xml version="1.0"?>
            <rdf:RDF xmlns="http://purl.obolibrary.org/obo/hp.owl#"
                 xml:base="http://purl.obolibrary.org/obo/hp.owl"
                 xmlns:cl="http://purl.obolibrary.org/obo/cl#"
                 xmlns:dc="http://purl.org/dc/elements/1.1/"
                 xmlns:go="http://purl.obolibrary.org/obo/go#"
                 xmlns:hp="http://purl.obolibrary.org/obo/hp#"
                 xmlns:pr="http://purl.obolibrary.org/obo/pr#"
                 xmlns:obo="http://purl.obolibrary.org/obo/"
                 xmlns:owl="http://www.w3.org/2002/07/owl#"
                 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                 xmlns:xml="http://www.w3.org/XML/1998/namespace"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
                 xmlns:cito="http://purl.org/spar/cito/"
                 xmlns:core="http://purl.obolibrary.org/obo/uberon/core#"
                 xmlns:foaf="http://xmlns.com/foaf/0.1/"
                 xmlns:obda="https://w3id.org/obda/vocabulary#"
                 xmlns:obo2="http://www.geneontology.org/formats/oboInOwl#http://purl.obolibrary.org/obo/"
                 xmlns:pato="http://purl.obolibrary.org/obo/pato#"
                 xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                 xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                 xmlns:swrl="http://www.w3.org/2003/11/swrl#"
                 xmlns:chebi="http://purl.obolibrary.org/obo/chebi/"
                 xmlns:swrla="http://swrl.stanford.edu/ontologies/3.3/swrla.owl#"
                 xmlns:swrlb="http://www.w3.org/2003/11/swrlb#"
                 xmlns:vocab="https://w3id.org/semapv/vocab/"
                 xmlns:chebi1="http://purl.obolibrary.org/obo/chebi#"
                 xmlns:chebi3="http://purl.obolibrary.org/obo/chebi#2"
                 xmlns:chebi4="http://purl.obolibrary.org/obo/chebi#3"
                 xmlns:chebi5="http://purl.obolibrary.org/obo/chebi#1"
                 xmlns:hsapdv="http://purl.obolibrary.org/obo/hsapdv#"
                 xmlns:ubprop="http://purl.obolibrary.org/obo/ubprop#"
                 xmlns:dcterms="http://purl.org/dc/terms/"
                 xmlns:subsets="http://purl.obolibrary.org/obo/ro/subsets#"
                 xmlns:oboInOwl="http://www.geneontology.org/formats/oboInOwl#"
                 xmlns:caloha-reqs-vetted="http://purl.obolibrary.org/obo/caloha-reqs-vetted#">
                <owl:Ontology rdf:about="http://purl.obolibrary.org/obo/hp.owl">
                    <owl:versionIRI rdf:resource="http://purl.obolibrary.org/obo/hp/releases/2023-07-21/hp.owl"/>
                    <obo:IAO_0000700 rdf:resource="http://purl.obolibrary.org/obo/HP_0000001"/>
                    <dc:creator>Human Phenotype Ontology Consortium</dc:creator>
                    <dc:creator>Monarch Initiative</dc:creator>
                    <dc:creator>Peter Robinson</dc:creator>
                    <dc:creator>Sebastian Köhler</dc:creator>
                    <dc:description>The Human Phenotype Ontology (HPO) provides a standardized vocabulary of phenotypic abnormalities and clinical features encountered in human disease.</dc:description>
                    <dc:rights>Peter Robinson, Sebastian Koehler, The Human Phenotype Ontology Consortium, and The Monarch Initiative</dc:rights>
                    <dc:subject>Phenotypic abnormalities encountered in human disease</dc:subject>
                    <dc:title>Human Phenotype Ontology</dc:title>
                    <dcterms:license rdf:resource="https://hpo.jax.org/app/license"/>
                    <oboInOwl:default-namespace>human_phenotype</oboInOwl:default-namespace>
                    <oboInOwl:logical-definition-view-relation>has_part</oboInOwl:logical-definition-view-relation>
                    <rdfs:comment>Please see license of HPO at http://www.human-phenotype-ontology.org</rdfs:comment>
                </owl:Ontology>

                <!-- http://purl.obolibrary.org/obo/HP_0000407 -->

                <owl:Class rdf:about="http://purl.obolibrary.org/obo/HP_0000407">
                    <owl:equivalentClass>
                        <owl:Restriction>
                            <owl:onProperty rdf:resource="http://purl.obolibrary.org/obo/BFO_0000051"/>
                            <owl:someValuesFrom>
                                <owl:Class>
                                    <owl:intersectionOf rdf:parseType="Collection">
                                        <rdf:Description rdf:about="http://purl.obolibrary.org/obo/PATO_0000911"/>
                                        <owl:Restriction>
                                            <owl:onProperty rdf:resource="http://purl.obolibrary.org/obo/RO_0000052"/>
                                            <owl:someValuesFrom>
                                                <owl:Class>
                                                    <owl:intersectionOf rdf:parseType="Collection">
                                                        <rdf:Description rdf:about="http://purl.obolibrary.org/obo/GO_0007605"/>
                                                        <owl:Restriction>
                                                            <owl:onProperty rdf:resource="http://purl.obolibrary.org/obo/BFO_0000066"/>
                                                            <owl:someValuesFrom rdf:resource="http://purl.obolibrary.org/obo/UBERON_0001021"/>
                                                        </owl:Restriction>
                                                    </owl:intersectionOf>
                                                </owl:Class>
                                            </owl:someValuesFrom>
                                        </owl:Restriction>
                                        <owl:Restriction>
                                            <owl:onProperty rdf:resource="http://purl.obolibrary.org/obo/RO_0002573"/>
                                            <owl:someValuesFrom rdf:resource="http://purl.obolibrary.org/obo/PATO_0000460"/>
                                        </owl:Restriction>
                                    </owl:intersectionOf>
                                </owl:Class>
                            </owl:someValuesFrom>
                        </owl:Restriction>
                    </owl:equivalentClass>
                    <rdfs:subClassOf rdf:resource="http://purl.obolibrary.org/obo/HP_0000365"/>
                    <rdfs:subClassOf rdf:resource="http://purl.obolibrary.org/obo/HP_0011389"/>
                    <obo:IAO_0000115>A type of hearing impairment in one or both ears related to an abnormal functionality of the cochlear nerve.</obo:IAO_0000115>
                    <oboInOwl:hasAlternativeId>HP:0000374</oboInOwl:hasAlternativeId>
                    <oboInOwl:hasAlternativeId>HP:0001753</oboInOwl:hasAlternativeId>
                    <oboInOwl:hasAlternativeId>HP:0001916</oboInOwl:hasAlternativeId>
                    <oboInOwl:hasAlternativeId>HP:0008538</oboInOwl:hasAlternativeId>
                    <oboInOwl:hasAlternativeId>HP:0008553</oboInOwl:hasAlternativeId>
                    <oboInOwl:hasAlternativeId>HP:0008565</oboInOwl:hasAlternativeId>
                    <oboInOwl:hasAlternativeId>HP:0008576</oboInOwl:hasAlternativeId>
                    <oboInOwl:hasAlternativeId>HP:0008611</oboInOwl:hasAlternativeId>
                    <oboInOwl:hasAlternativeId>HP:0008613</oboInOwl:hasAlternativeId>
                    <oboInOwl:hasAlternativeId>HP:0008614</oboInOwl:hasAlternativeId>
                    <oboInOwl:hasDbXref>MSH:D006319</oboInOwl:hasDbXref>
                    <oboInOwl:hasDbXref>SNOMEDCT_US:60700002</oboInOwl:hasDbXref>
                    <oboInOwl:hasDbXref>UMLS:C0018784</oboInOwl:hasDbXref>
                    <oboInOwl:hasExactSynonym>Hearing loss, sensorineural</oboInOwl:hasExactSynonym>
                    <oboInOwl:hasExactSynonym>Sensorineural deafness</oboInOwl:hasExactSynonym>
                    <oboInOwl:hasExactSynonym>Sensorineural hearing loss</oboInOwl:hasExactSynonym>
                    <oboInOwl:id>HP:0000407</oboInOwl:id>
                    <oboInOwl:inSubset rdf:resource="http://purl.obolibrary.org/obo/hp#hposlim_core"/>
                    <rdfs:comment>Hearing loss caused by damage or dysfunction of the auditory nerve (cranial nerve VIII, also known as the cochlear nerve).</rdfs:comment>
                    <rdfs:label>Sensorineural hearing impairment</rdfs:label>
                </owl:Class>
                <owl:Axiom>
                    <owl:annotatedSource rdf:resource="http://purl.obolibrary.org/obo/HP_0000407"/>
                    <owl:annotatedProperty rdf:resource="http://purl.obolibrary.org/obo/IAO_0000115"/>
                    <owl:annotatedTarget>A type of hearing impairment in one or both ears related to an abnormal functionality of the cochlear nerve.</owl:annotatedTarget>
                    <oboInOwl:hasDbXref rdf:resource="https://orcid.org/0000-0002-0736-9199"/>
                </owl:Axiom>
                <owl:Axiom>
                    <owl:annotatedSource rdf:resource="http://purl.obolibrary.org/obo/HP_0000407"/>
                    <owl:annotatedProperty rdf:resource="http://www.geneontology.org/formats/oboInOwl#hasExactSynonym"/>
                    <owl:annotatedTarget>Hearing loss, sensorineural</owl:annotatedTarget>
                    <oboInOwl:hasDbXref rdf:resource="https://orcid.org/0000-0002-5316-1399"/>
                </owl:Axiom>
            </rdf:RDF>
            """;

    @Test
    void validEnricher() throws IOException, OWLOntologyCreationException {
        Model expectedModel = Rio.parse(new StringReader(VALID_TURTLE), RDFFormat.TURTLE);
        expectedModel.add(
                factory.createIRI("http://purl.obolibrary.org/obo/HP_0000407"),
                RdfsIri.LABEL.getIri(),
                factory.createLiteral("Sensorineural hearing impairment")
        );
        expectedModel.add(
                factory.createIRI("http://purl.obolibrary.org/obo/HP_0000407"),
                RdfsIri.SEE_ALSO.getIri(),
                factory.createIRI("http://linkedlifedata.com/resource/umls/id/C0018784")
        );
        expectedModel.add(
                factory.createIRI("http://purl.obolibrary.org/obo/HP_0000407"),
                RdfsIri.SEE_ALSO.getIri(),
                factory.createIRI("http://bio2rdf.org/umls:C0018784")
        );
        expectedModel.add(
                factory.createIRI("http://purl.obolibrary.org/obo/HP_0000407"),
                RdfsIri.SEE_ALSO.getIri(),
                factory.createIRI("http://id.nlm.nih.gov/mesh/D006319")
        );
        expectedModel.add(
                factory.createIRI("http://purl.obolibrary.org/obo/HP_0000407"),
                RdfsIri.SEE_ALSO.getIri(),
                factory.createIRI("http://bioportal.bioontology.org/ontologies/MESH/D006319")
        );
        expectedModel.add(
                factory.createIRI("http://purl.obolibrary.org/obo/HP_0000407"),
                RdfsIri.SEE_ALSO.getIri(),
                factory.createIRI("http://bio2rdf.org/mesh:D006319")
        );
        expectedModel.add(
                factory.createIRI("http://purl.obolibrary.org/obo/HP_0000407"),
                RdfsIri.SEE_ALSO.getIri(),
                factory.createIRI("http://snomed.info/sct/60700002")
        );
        expectedModel.add(
                factory.createIRI("http://purl.obolibrary.org/obo/HP_0000407"),
                RdfsIri.SEE_ALSO.getIri(),
                factory.createIRI("http://bioportal.bioontology.org/ontologies/SNOMEDCT/60700002")
        );

        ModelEnhanced modelEnhanced = new ModelEnhanced(Rio.parse(new StringReader(VALID_TURTLE), RDFFormat.TURTLE));
        OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(new ByteArrayInputStream(OWL.getBytes()));
        new HpoEnricher(ontology).process(modelEnhanced);

        Assertions.assertEquals(expectedModel, modelEnhanced.getModel());
    }
}