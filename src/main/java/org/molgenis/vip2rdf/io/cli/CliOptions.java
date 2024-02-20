package org.molgenis.vip2rdf.io.cli;

import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;

public class CliOptions {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec; // injected by picocli

    public Path getVcfPath() {
        return vcfPath;
    }

    @CommandLine.Option(names = {"-i", "--input"}, required = true, paramLabel="VCF FILE",
            description = "The VIP vcf file to be processed")
    public void setVcfPath(Path vcfPath) {
        validateInputFile(vcfPath, ".vcf");
        this.vcfPath = vcfPath;
    }

    public Path getSampleSheetPath() {
        return sampleSheetPath;
    }

    @CommandLine.Option(names = {"-s", "--samplesheet"}, required = true, paramLabel="TSV FILE",
            description = "The VIP samplesheet belonging to the vcf file")
    public void setSampleSheetPath(Path sampleSheetPath) {
        validateInputFile(sampleSheetPath, ".tsv");
        this.sampleSheetPath = sampleSheetPath;
    }

    public Path getEnsemblGlossaryPath() {
        return ensemblGlossaryPath;
    }

    @CommandLine.Option(names = {"-e", "--ensembl"}, required = true, paramLabel="OWL FILE",
            description = "The Ensembl glossary (.owl) file")
    public void setEnsemblGlossaryPath(Path ensemblGlossaryPath) {
        validateInputFile(ensemblGlossaryPath, ".owl");
        this.ensemblGlossaryPath = ensemblGlossaryPath;
    }

    public Path getHpoOWlPath() {
        return hpoOWlPath;
    }

    @CommandLine.Option(names = {"-p", "--hpo"}, required = true, paramLabel="OWL FILE",
            description = "The Human Phenotype Ontology (.owl) file")
    public void setHpoOWlPath(Path hpoOWlPath) {
        validateInputFile(hpoOWlPath, ".owl");
        this.hpoOWlPath = hpoOWlPath;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    @CommandLine.Option(names = {"-o", "--output"}, required = true, paramLabel="TTL FILE",
            description = "The output file path (.ttl)")
    public void setOutputPath(Path outputPath) {
        validateFileExtension(outputPath, ".ttl");
        this.outputPath = outputPath;
    }

    public boolean isCmdVersionRequested() {
        return cmdVersionRequested;
    }

    public void setCmdVersionRequested(boolean cmdVersionRequested) {
        this.cmdVersionRequested = cmdVersionRequested;
    }

    public boolean isCmdUsageRequested() {
        return cmdUsageRequested;
    }

    public void setCmdUsageRequested(boolean cmdUsageRequested) {
        this.cmdUsageRequested = cmdUsageRequested;
    }

    private Path vcfPath;
    private Path sampleSheetPath;
    private Path ensemblGlossaryPath;
    private Path hpoOWlPath;
    private Path outputPath;

    @CommandLine.Option(names = {"-v", "--version"}, versionHelp = true, description = "Show version info")
    boolean cmdVersionRequested;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Show help message")
    boolean cmdUsageRequested;

    private void validateInputFile(Path path, String extension) {
        validateFileExists(path);
        validateFileExtension(path, extension);
    }

    private void validateFileExists(Path path) {
        if(!Files.exists(path)) {
            throw new CommandLine.ParameterException(spec.commandLine(),
                    String.format("\"%s\" is not a valid path to an existing file", path));
        }
    }

    private void validateFileExtension(Path path, String extension) {
        if(!path.toString().endsWith(extension)) {
            throw new CommandLine.ParameterException(spec.commandLine(),
                    String.format("\"%s\" is does not have the \"%s\" file extension", path, extension));
        }
    }
}
