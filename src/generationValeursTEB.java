import communs.Outils;
import information.Information;
import org.apache.commons.cli.*;

public class generationValeursTEB {
    private String[] args;

    private static String form;
    private static String mess;
    private static String csvFile;
    private static float pas;
    private static float snrMin;
    private static float snrMax;

    private static boolean codeur;

    private static boolean trajetMultiple;
    private static int[] decalageTemporel;
    private static Float[] amplitudeRelative;

    private Information<Float> data;

    /**
     * @param pas utilisé pour incrémenter le SNR dans l'intervale [snrMin;snrMax]
     * @param snrMin utilisé pour spécifier l'intervale [snrMin;snrMax]
     * @param snrMax utilisé pour spécifier l'intervale [snrMin;snrMax]
     */
    private generationValeursTEB(float pas, float snrMin, float snrMax) {
        this.pas = pas;
        this.snrMin = snrMin;
        this.snrMax = snrMax;
    }

    /**
     * Récolte les TEB, avec le pas spécifié, jusqu'au TEB max spécifié
     * @param args Paramètre du simulateur (Sans spécifier le SNR !)
     */
    private void sampling(String[] args) throws Exception {
        data = new Information<>();
        this.args = args;

        for (float snr = snrMin; snr < snrMax; snr += pas) {
            String[] snrSimu = {"-mute", "-snr", Float.toString(snr), "-csv", "RZ.csv", "-nbEch", "30", "-form", "RZ", "-mess", "50000"};
            String[] argsSimu = Outils.concatenate(args, snrSimu);

            Simulateur simu = new Simulateur(argsSimu);
            simu.execute();
            float teb = simu.calculTauxErreurBinaire();

            data.add(teb);

            System.out.println("SNR - TEB : " + snr + " - " + teb);
        }
    }

    private static void analyseArgument(String[] args) throws ArgumentsException {
        // Prepare options
        final Options options = configParameters();
        final CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        // Parse the options
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            throw new ArgumentsException(e.toString());
        }

        form = commandLine.getOptionValue("form");
        mess = commandLine.getOptionValue("mess");
        csvFile = commandLine.getOptionValue("csv");

        final String[] snr = commandLine.getOptionValues("snr");
        snrMin = Float.parseFloat(snr[0]);
        snrMax = Float.parseFloat(snr[1]);
        snrMin = Float.parseFloat(snr[2]);

        codeur = commandLine.hasOption("-codeur");

        trajetMultiple = commandLine.hasOption("ti");
        if (trajetMultiple) {
            String[] optionsValues = commandLine.getOptionValues("ti");

            if (Outils.isOdd(optionsValues.length))
                throw new ArgumentsException("Valeurs du paramètre -ti doivent etre par couple de 2 valeurs : " + String.join(" ", optionsValues));

            int sizeArray = optionsValues.length / 2;
            decalageTemporel = new int[sizeArray];
            amplitudeRelative = new Float[sizeArray];

            for (int i = 0; i < sizeArray; i++) {
                try {
                    decalageTemporel[i] = Integer.parseInt(optionsValues[2*i]);
                    amplitudeRelative[i] = Float.parseFloat(optionsValues[2*i+1]);
                } catch (NumberFormatException e) {
                    throw new ArgumentsException("Valeur du parametre -ti invalide (couple " + i + ") : " + String.join(" ", optionsValues));
                }
                if (decalageTemporel[i] < 0) {
                    throw new ArgumentsException("Signal non causal : le decalage (en nombre d'échantillons) ne peut pas etre inferieur à 0 : " + decalageTemporel[i]);
                }
                if (amplitudeRelative[i] < 0) {
                    throw new ArgumentsException("L'attenuation minimale est 0, elle ne peut pas etre négative (l'attenuation est un rapport par rapport au signal direct)");
                }
                else if (amplitudeRelative[i] > 1) {
                    throw new ArgumentsException("L'attenuation maximale est 1, l'attenuation ne peut pas amplifier le signal");
                }
            }
        }
    }

    private static Options configParameters() {

        final Option formOption = Option.builder("-form")
                .desc("Forme du signal dont nous voulons la courbe")
                .hasArg()
                .optionalArg(false)
                .build();

        final Option messOption = Option.builder("-mess")
                .desc("Longeur du message")
                .hasArg()
                .optionalArg(false)
                .build();

        final Option csvOption = Option.builder("-csv")
                .desc("fichier csv de sortie")
                .hasArg()
                .optionalArg(false)
                .build();

        final Option codeurOption = Option.builder("-codeur")
                .desc("Utilisation des codeur de signal")
                .hasArg()
                .optionalArg(true)
                .build();

        final Option snrOption = Option.builder("snr")
                .desc("Permet de spécifier les bornes du SNR, signal sur bruit (en dB) et le pas")
                .hasArg()
                .numberOfArgs(3)
                .optionalArg(false)
                .valueSeparator(' ')
                .argName("snrStart snrEnd snrPas")
                .build();

        final Option trajetIndirectOption = Option.builder("ti")
                .desc("Permet de spécifier les différents trajets indirects, par couple de valeur \"dt ar\" (5 max)\n" +
                        "> dt : Décalage temporel (en nombre d'échantillons)\n" +
                        "> ar : Amplitude relative, compris entre [0.0;1.0]\n" +
                        "(Par défaut, \"0 0.0\")")
                .hasArg()
                .argName("dt ar")
                .numberOfArgs(10)
                .optionalArg(true)
                .valueSeparator(' ')
                .build();

        // <----- CONCATENATE OPTIONS -----> //

        final Options options = new Options();

        options.addOption(formOption);
        options.addOption(messOption);
        options.addOption(csvOption);
        options.addOption(codeurOption);
        options.addOption(snrOption);
        options.addOption(trajetIndirectOption);

        return options;
    }

    /**
     * Génération de la courbe pour un pas de 0.5 allant de -100 à 0
     * @param args Paramètres du simulateur
     * @throws Exception Simulateur en défaut
     */
    public static void main(String[] args) throws Exception {
        generationValeursTEB valid = new generationValeursTEB(0.1f, -10f, 10f);
        valid.sampling(args);
    }
}