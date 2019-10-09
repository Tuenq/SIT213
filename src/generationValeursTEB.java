import communs.Outils;
import information.Information;
import org.apache.commons.cli.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class generationValeursTEB {
    private String[] args;

    private static String form;
    private static String mess;
    private static int nbSimulations;
    private static String directory = "";
    private static String csvFile = "";
    private static float pas;
    private static float snrMin;
    private static float snrMax;

    private static boolean codeur;

    private static boolean trajetMultiple;
    private static String[] decalageTemporel;
    private static String[] amplitudeRelative;

    private static Information<Float> data;

    private static void generateFileName(){
        DateFormat df = new SimpleDateFormat("dd-MM-yy_HH:mm");
        Date dateobj = new Date();
        csvFile = df.format(dateobj);
        csvFile += "_Form:" + form;
        csvFile += "_SNRmin:" + snrMin + "_SNRmax:" + snrMax + "_SNRpas:" + pas;
        csvFile += "_TMess:" + mess;
        if (codeur)
            csvFile += "_CodeurUtilises";
        if (trajetMultiple){
            csvFile += "_TrajetMultiple:";
            for (int trajet = 0 ; trajet < decalageTemporel.length ; trajet++){
                csvFile += "_" + decalageTemporel[trajet];
                csvFile += "_" + amplitudeRelative[trajet];
            }
        }
        csvFile += ".csv";
    }

    /**
     * Récolte les TEB, avec le pas spécifié, jusqu'au TEB max spécifié
     */
    private static void sampling() throws Exception {
        data = new Information<>();

        String[] argsSimu = {"-mute", "-form", form, "-mess", mess, "-nbEch", "15"};
        if (codeur)
            argsSimu = append(argsSimu, "-codeur");
        if (trajetMultiple){
            argsSimu = append(argsSimu, "-ti");
            for (int trajet = 0 ; trajet < decalageTemporel.length ; trajet++){
                argsSimu = append(argsSimu, decalageTemporel[trajet]);
                argsSimu = append(argsSimu, amplitudeRelative[trajet]);
            }
        }

        for (float snr = snrMin; snr < snrMax; snr += pas) {
            float averageTEB = 0.0F;
            for (int simulation = 0 ; simulation < nbSimulations ; simulation++) {
                argsSimu = append(argsSimu, "-snr");
                argsSimu = append(argsSimu, Float.toString(snr));

                Simulateur simulateur = new Simulateur(argsSimu);
                simulateur.execute();
                averageTEB += simulateur.calculTauxErreurBinaire();
            }
            float teb = averageTEB / nbSimulations;
            System.out.println("SNR - TEB : " + snr + " - " + teb);

            extractionCSV.sauvegardeData(directory + csvFile, teb, snr);
        }
    }

    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
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
        nbSimulations = Integer.parseInt(commandLine.getOptionValue("simu"));
        directory = commandLine.getOptionValue("directory");

        final String[] snr = commandLine.getOptionValues("snr");
        snrMin = Float.parseFloat(snr[0]);
        snrMax = Float.parseFloat(snr[1]);
        pas = Float.parseFloat(snr[2]);

        codeur = commandLine.hasOption("codeur");

        trajetMultiple = commandLine.hasOption("ti");
        if (trajetMultiple) {
            String[] optionsValues = commandLine.getOptionValues("ti");

            if (Outils.isOdd(optionsValues.length))
                throw new ArgumentsException("Valeurs du paramètre -ti doivent etre par couple de 2 valeurs : " + String.join(" ", optionsValues));

            int sizeArray = optionsValues.length / 2;
            decalageTemporel = new String[sizeArray];
            amplitudeRelative = new String[sizeArray];

            for (int i = 0; i < sizeArray; i++) {
                try {
                    decalageTemporel[i] = optionsValues[2*i];
                    amplitudeRelative[i] = optionsValues[2*i+1];
                } catch (NumberFormatException e) {
                    throw new ArgumentsException("Valeur du parametre -ti invalide (couple " + i + ") : " + String.join(" ", optionsValues));
                }
                if (Integer.parseInt(decalageTemporel[i]) < 0) {
                    throw new ArgumentsException("Signal non causal : le decalage (en nombre d'échantillons) ne peut pas etre inferieur à 0 : " + decalageTemporel[i]);
                }
                if (Float.parseFloat(amplitudeRelative[i]) < 0) {
                    throw new ArgumentsException("L'attenuation minimale est 0, elle ne peut pas etre négative (l'attenuation est un rapport par rapport au signal direct)");
                }
                else if (Float.parseFloat(amplitudeRelative[i]) > 1) {
                    throw new ArgumentsException("L'attenuation maximale est 1, l'attenuation ne peut pas amplifier le signal");
                }
            }
        }
    }

    private static Options configParameters() {

        final Option formOption = Option.builder("form")
                .desc("Forme du signal dont nous voulons la courbe")
                .hasArg()
                .build();

        final Option messOption = Option.builder("mess")
                .desc("Longeur du message")
                .hasArg()
                .build();

        final Option simulationOption = Option.builder("simu")
                .desc("Nombre de simulation pour une valeur de SNR")
                .hasArg()
                .build();

        final Option directoryOption = Option.builder("directory")
                .desc("dossier de destination de sortie")
                .hasArg()
                .build();

        final Option codeurOption = Option.builder("codeur")
                .desc("Utilisation des codeur de signal")
                .hasArg()
                .build();

        final Option snrOption = Option.builder("snr")
                .desc("Permet de spécifier les bornes du SNR, signal sur bruit (en dB) et le pas")
                .hasArg()
                .numberOfArgs(3)
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
        options.addOption(simulationOption);
        options.addOption(directoryOption);
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
        analyseArgument(args);
        generateFileName();
        sampling();
    }
}