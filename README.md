# vip2rdf

A concept version of converting (a part of) VIP vcf files into RDF Turtle files.

## Regarding the RDF files

An uuid was created for defining terms within the generated files: `urn:uuid:E4CBEA11-46B8-4B68-A202-B9FC8E5BE255#`

## Basic ttl file without enrichers
```mermaid
%%{
    init: {
        'theme': 'base',
        'themeVariables': {
            'primaryTextColor': '#000000',
            'secondaryColor': '#ffffff'
        },
        'flowchart': {
            'curve': 'stepAfter'
        }
    }
}%%
flowchart LR
    patient["patient\n(sio:SIO_000393)"]:::iriNode
    patient --->|"has variant\n(vip:has-variant)"| variant["genomic sequence variant\n(sio:SIO_001381)"]:::iriNode
    patient --->|"has phenotype\n(sio:SIO_001279)"| phenotype["phenotype\n(sio:SIO_010056)"]:::externalLink
    
    variant --->|"chromosome\n(vip:chrom)"| chromosome:::literalNode
    variant --->|"position\n(vip:pos)"| position:::literalNode
    variant --->|"id\n(vip:id)"| id:::literalNode
    variant --->|"ref\n(vip:ref)"| reference:::literalNode
    variant --->|"alt\n(vip:alt)"| alternative:::literalNode
    variant --->|"has transcript\n(vip:has-transcript)"| transcript["RNA transcript\n(sio:SIO_010450)"]:::iriNode
    
    transcript --->|"gene\n(vip:gene)"| gene["gene\n(sio:SIO_010035)"]:::externalLink
    transcript --->|"classification\n(vip:vipc)"| vipc:::uniquePossibility
    transcript --->|"consequence\n(vip:consequence)"| consequence["Ensembl variant consequence"]:::externalLink
    transcript --->|"has feature\n(vip:has-feature)"| feature["NCBI Reference Sequence"]:::externalLink
    
    gene --->|"rdfs:label"| geneSymbol["NCBI gene symbol"]:::literalNode
    
    phenotype --->|"rdfs:label"| phenotypeName["name"]:::literalNode
    phenotype --->|rdfs:seeAlso| phenotypeSeeAlso:::multiplePossibilities
    
    subgraph phenotypeSeeAlso[" "]
        direction LR
        UMLS:::externalLink
        MESH:::externalLink
        SNOMED:::externalLink
    end
    
    subgraph vipc[" "]
        direction LR
        P["P\n(vip:vipc-p)"]:::iriNode
        LP["LP\n(vip:vipc-lp)"]:::iriNode
        VUS["VUS\n(vip:vipc-vus)"]:::iriNode
        LB["LB\n(vip:vipc-lb)"]:::iriNode
        B["B\n(vip:vipc-b)"]:::iriNode
        LQ["LQ\n(vip:vipc-lq)"]:::iriNode
    end
    
    %% Styles
    classDef iriNode fill:#7eb0d5,stroke:black
    classDef literalNode fill:#7eb0d5,stroke-dasharray: 10 5,stroke:black
    classDef externalLink fill:#8bd3c7,stroke:black
    classDef uniquePossibility fill:#ffee65,stroke:none
    classDef multiplePossibilities fill:#ffb55a,stroke:none
    classDef subGraphTitle fill:none,stroke:none,font-size:40px
    
    %% Legend
    subgraph legend[" "]
        direction LR
        legendTitle["legend"]:::subGraphTitle
        l1[IRI node]:::iriNode
        l2[literal node]:::literalNode
        l3[externally linkable node]:::externalLink
        l4["possible nodes\n(can only have 1)"]:::uniquePossibility
        l5["possible nodes\n(can have multiple)"]:::multiplePossibilities
        
    end
    style legend fill:none,stroke:#000000
```
Note: every unique subject has an `rdfs:label "<label>"`. The diagram only explicitly mentions the cases where the `rdfs:label` contains information that is not very similar to the IRI itself (f.e. a gene ID IRI but the symbol as label).