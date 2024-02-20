package org.molgenis.vip2rdf;

import org.molgenis.vip2rdf.exceptions.InvalidRdfModelException;
import org.molgenis.vip2rdf.formats.ModelEnhanced;
import org.molgenis.vip2rdf.io.cli.CliOptions;
import org.molgenis.vip2rdf.properties.AppProperties;
import org.molgenis.vip2rdf.properties.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "vip2rdf", description = "Converts (part of) a VIP vcf file to a RDF Turtle file")
public class VipToRdfConverter {
    private static final Logger logger = LoggerFactory.getLogger(VipToRdfConverter.class);

    @CommandLine.Mixin
    private static CliOptions cliOptions;

    public static void main(String[] args) {
        loadPropertiesFile();
        parseCli(args);
        runApplicationLogic();
    }

    private static void loadPropertiesFile() {
        try {
            PropertiesLoader.loadProperties();
        } catch (IOException e) {
            System.err.println("Properties file failed to load.");
            System.exit(1);
        }
    }

    private static void parseCli(String[] args) {
        CommandLine commandLine = new CommandLine(new VipToRdfConverter());
        if(args.length == 0) {
            commandLine.usage(System.out);
            System.exit(0);
        }

        try {
            commandLine.parseArgs(args);
        } catch (CommandLine.ParameterException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        if(commandLine.isUsageHelpRequested()) {
            commandLine.usage(System.out);
            System.exit(0);
        } else if(commandLine.isVersionHelpRequested()) {
            System.out.println(
                    String.format("%s %s",
                            AppProperties.APP_NAME.getValue(),
                            AppProperties.APP_VERSION.getValue()
            ));
            System.exit(0);
        }
    }

    static void runApplicationLogic() {
        try {
            ModelEnhanced model = VipToRdfCore.processData(
                    cliOptions.getVcfPath(),
                    cliOptions.getSampleSheetPath(),
                    cliOptions.getEnsemblGlossaryPath(),
                    cliOptions.getHpoOWlPath()
            );
            VipToRdfCore.writeTurtle(cliOptions.getOutputPath(), model);
        } catch (IOException | InvalidRdfModelException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}