import org.apache.commons.cli.*;

public class ValidationTests {

    private static void validationEtape1() {
        String[] args1 = {"-s", "-seed", "123", "-snr", "1000"};
        String[] args2 = {"-s", "-mess", "000000111111", "-snr", "1000"};

        try {
            Simulateur simulateur = new Simulateur(args1);
            simulateur.execute();

            simulateur = new Simulateur(args2);
            simulateur.execute();
        }
        catch (Exception exception){
            exception.printStackTrace();
            System.out.println("Erreur durant le test de validation etape 1");
        }
    }

    private static void validationEtape2() {
        String[] args1 = {"-s", "-seed", "123", "-nbEch", "100", "-form", "RZ", "-snr", "1000"};
        String[] args2 = {"-s", "-seed", "123", "-nbEch", "100", "-form", "NRZ", "-snr", "1000"};
        String[] args3 = {"-s", "-seed", "123", "-nbEch", "100", "-form", "NRZT", "-snr", "1000"};
        try {
            Simulateur simulateur = new Simulateur(args1);
            simulateur.execute();

            simulateur = new Simulateur(args2);
            simulateur.execute();

            simulateur = new Simulateur(args3);
            simulateur.execute();
        }
        catch (Exception exception){
            System.out.println("Erreur durant le test de validation etape 2");
        }
    }

    private static void validationEtape3() {
        String[] args = {"-s", "-seed", "123", "-snr", "20", "-nbEch", "100", "-form", "RZ"};
        try {
            Simulateur simulateur = new Simulateur(args);
            simulateur.execute();
        }
        catch (Exception exception){
            System.out.println("Erreur durant le test de validation etape 3");
        }
    }

    private static int analyseArgument(String[] args) throws ArgumentsException {
        // Prepare options
        final Options options = configParameters();
        final CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;

        // Parse the options
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            throw new ArgumentsException(e.toString());
        }

        int numEtape = 0;

        if (commandLine.hasOption("e")){
            numEtape = Integer.parseInt(commandLine.getOptionValue("e"));
        }

        return numEtape;
    }

    /**
     * Permet de définir les options disponibles en ligne de commande
     * @return un objet contenant les différentes options
     */
    private static Options configParameters() {

        final Option etapeOption = Option.builder("e")
                .desc("Affiche cette liste des usages")
                .hasArg()
                .build();

        // <----- CONCATENATE OPTIONS -----> //

        final Options options = new Options();

        options.addOption(etapeOption);

        return options;
    }

    public static void main(String[] args) throws ArgumentsException {

        switch (analyseArgument(args)){
            case 1:
                validationEtape1();
                break;
            case 2:
                validationEtape2();
                break;
            case 3:
                validationEtape3();
                break;
            default:
                validationEtape1();
                validationEtape2();
                validationEtape3();
        }

    }
}