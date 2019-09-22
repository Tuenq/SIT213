import encoders.Encoder.encoders;
import org.apache.commons.cli.*;
import sources.*;
import destinations.*;
import transmetteurs.*;

import information.*;
import visualisations.Sonde;
import visualisations.SondeLogique;

import java.util.Arrays;

/**
 * La classe Simulateur permet de construire et simuler une chaîne de
 * transmission composée d'une Source, d'un nombre variable de Transmetteur(s)
 * et d'une Destination.
 *
 * @author cousin
 * @author prou
 */
public class Simulateur {

    /**
     * indique si le Simulateur est inhibé par seulement afficher l'aide
     */
    private boolean affichageAide = false;

    // <----- OPTIONS ETAPE 1 -----> //

    /**
     * indique si le Simulateur utilise des sondes d'affichage
     */
    private boolean affichage = false;
    /**
     * indique si le Simulateur utilise un message généré de manière aléatoire
     */
    private boolean messageAleatoire = true;
    /**
     * indique si le Simulateur utilise un germe pour initialiser les générateurs
     * aléatoires
     */
    private boolean aleatoireAvecGerme = false;
    /**
     * la valeur de la semence utilisée pour les générateurs aléatoires
     */
    private Integer seed = null;
    /**
     * la longueur du message aléatoire à transmettre si un message n'est pas impose
     */
    private int nbBitsMess = 100;
    /**
     * la chaîne de caractères correspondant à m dans l'argument -mess m
     */
    private String messageString = "100";

    // <----- OPTIONS ETAPE 2 -----> //

    /**
     * La forme d'onde par défaut (initialisée par -form)
     */
    private encoders formeOnde = encoders.RZ;
    /**
     * Le nombre d'échantillons par bit (initialisée par -nbEch) avec comme valeur par défaut = 30
     */
    private Integer nombreEchantillon = 30;
    /**
     * L'amplitude maximum des encodeurs/décodeurs analogique/numérique (initialisée par -ampl min max)
      */
    private Float amplitudeMax = 1.0f;
    /**
     * L'amplitude minimum des encodeurs/décodeurs analogique/numérique (initialisée par -ampl min max)
     */
    private Float amplitudeMin = 0.0f;

    // <----- SIMULATEUR -----> //

    /**
     * le composant Source de la chaine de transmission
     */
    private Source<Boolean> source = null;
    /**
     * le composant Transmetteur parfait logique de la chaine de transmission
     */
    private Transmetteur<Boolean,Boolean> transmetteurLogique = null;
    /**
     * le composant Transmetteur parfait analogique de la chaine de transmission
     */
    private Transmetteur<Float,Float> transmetteurAnalogique = null;
    /**
     * le composant Destination de la chaine de transmission
     */
    private Destination<Boolean> destination = null;


    /**
     * Le constructeur de Simulateur construit une chaîne de transmission composée
     * d'une Source <Boolean>, d'une Destination <Boolean> et de Transmetteur(s)
     * [voir la méthode analyseArguments]... <br>
     * Les différents composants de la chaîne de transmission (Source,
     * Transmetteur(s), Destination, Sonde(s) de visualisation) sont créés et
     * connectés.
     *
     * @param args le tableau des différents arguments.
     * @throws ArgumentsException si un des arguments est incorrect
     */
    public Simulateur(String[] args) throws ArgumentsException {
        analyseArguments(args);
        simulationParfaite();
    }

    private void simulationParfaite() throws ArgumentsException {

        // Configuration de la SOURCE ALEATOIRE|FIXE
        if (messageAleatoire) {
            if (aleatoireAvecGerme)
                source = new SourceAleatoire(nbBitsMess, seed);
            else
                source = new SourceAleatoire(nbBitsMess);
        } else {
            try {
                source = new SourceFixe(Information.stringToBoolean(messageString));
            } catch (InformationNonConforme exception) {
                throw new ArgumentsException(exception.toString());
            }
        }

        // Configuration du TRANSMETTEUR PARFAIT
        transmetteurLogique = new TransmetteurParfait<>();
        source.connecter(transmetteurLogique);

        // Configuration de la DESTINATION
        destination = new DestinationFinale();
        transmetteurLogique.connecter(destination);

        if (affichage) {
            // Ajout des SONDES LOGIQUES
            Sonde<Boolean> sonde_entree = new SondeLogique("Entrée du système", 100);
            source.connecter(sonde_entree);
            Sonde<Boolean> sonde_sortie = new SondeLogique("Sortie du système", 100);
            transmetteurLogique.connecter(sonde_sortie);
        }
    }

    /**
     * La méthode analyseArguments extrait d'un tableau de chaînes de caractères les
     * différentes options de la simulation. Elle met à jour les attributs du
     * Simulateur.
     *
     * @param args Pour plus d'information, lancer le simulateur avec le paramètre '-h'
     * @throws ArgumentsException si un des arguments est incorrect.
     */
    private void analyseArguments(String[] args) throws ArgumentsException {

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

        // Process the options
        affichageAide = commandLine.hasOption("h");
        if (affichageAide) {
            final String header = "Simulateur de chaîne de transmission\n\n";
            final String footer = "\nProjet réalisé par M.Bartoli, L.Dumestre, L.Francis, O.Gueye et S.HugdeLarauze\n";

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Simulateur", header, options, footer, true);
            return;
        }

        // <----- OPTIONS ETAPE 1 -----> //

        affichage = commandLine.hasOption("s");

        aleatoireAvecGerme = commandLine.hasOption("seed");
        if (aleatoireAvecGerme) {
            try {
                seed = Integer.parseInt(commandLine.getOptionValue("seed"));
            } catch (Exception e) {
                throw new ArgumentsException("Valeur du parametre -seed  invalide :" + commandLine.getOptionValue("seed"));
            }
        }

        messageAleatoire = commandLine.hasOption("mess");
        if (messageAleatoire) {
            messageString = commandLine.getOptionValue("mess");
            if (messageString.matches("[0,1]{7,}")) {
                messageAleatoire = false;
                nbBitsMess = messageString.length();
            } else if (messageString.matches("[0-9]{1,6}")) {
                messageAleatoire = true;
                nbBitsMess = Integer.parseInt(messageString);
                if (nbBitsMess < 1)
                    throw new ArgumentsException("Valeur du parametre -mess invalide : " + nbBitsMess);
            } else
                throw new ArgumentsException("Valeur du parametre -mess invalide : " + messageString);
        }

        // <----- OPTIONS ETAPE 2 -----> //

        if (commandLine.hasOption("form")) {
            try {
                formeOnde = encoders.valueOf(commandLine.getOptionValue("form"));
            } catch (IllegalArgumentException e) {
                throw new ArgumentsException("Valeur du parametre -form invalide : " + commandLine.getOptionValue("form"));
            }
        }

        if (commandLine.hasOption("nbEch")) {
            try {
                nombreEchantillon = Integer.parseInt(commandLine.getOptionValue("nbEch"));
            } catch (NumberFormatException e) {
                throw new ArgumentsException("Valeur du parametre -nbEch invalide : " + commandLine.getOptionValue("nbEch"));
            }

            if (nombreEchantillon <= 0)
                throw new ArgumentsException("Valeur du parametre -nbEch inférieur ou égale à 0 : " + nombreEchantillon);
        }

        if (commandLine.hasOption("ampl")) {

            final String[] amplitudes = commandLine.getOptionValues("ampl");

            try {
                amplitudeMin = Float.parseFloat(amplitudes[0]);
                amplitudeMax = Float.parseFloat(amplitudes[1]);
            } catch (NumberFormatException e) {
                throw new ArgumentsException("Valeurs du parametre -ampl invalides : " + Arrays.toString(amplitudes));
            }

            if (amplitudeMin >= amplitudeMax) {
                throw new ArgumentsException("Valeur du parametre -nbEch ne respecte pas min < max : " + Arrays.toString(amplitudes));
            }
        }

    }

    /**
     * Permet de définir les options disponibles en ligne de commande
     * @return un objet contenant les différentes options
     */
    private static Options configParameters() {

        final Option aideOption = Option.builder("h")
                .desc("Affiche cette liste des usages")
                .build();

        // <----- OPTIONS ETAPE 1 -----> //

        final Option sondeOption = Option.builder("s")
                .desc("Active l'utilisation des sondes\n" +
                        "(Par défaut désactivée)")
                .hasArg(false)
                .build();

        final Option messageOption = Option.builder("mess")
                .desc("Permet de préciser le message ou la longueur du message\n" +
                        "(Par défaut, message de longueur 100)")
                .hasArg()
                .argName("m")
                .build();

        final Option germeOption = Option.builder("seed")
                .desc("Permet d'initialiser le générateur aléatoire avec une germe spécifique\n" +
                        "(Par défaut, non initialisée)")
                .hasArg()
                .argName("v")
                .build();

        // <----- OPTIONS ETAPE 2 -----> //

        final Option formeOption = Option.builder("form")
                .desc("Permet de spécifier la forme d'onde:\n" +
                        "> (RZ) - impulsionnelle [Valeur par défaut]\n" +
                        "> (NRZ) - rectangulaire\n" +
                        "> (NRZT) - trapézoïdale")
                .hasArg()
                .argName("f")
                .build();

        final Option nombreEchantillonOption = Option.builder("nbEch")
                .desc("Permet de spécifier le nombre d'échantillons par bits\n" +
                        "(Par défaut = 30 ech/bit)")
                .hasArg()
                .argName("ne")
                .build();

        final Option amplitudeOption = Option.builder("ampl")
                .desc("Permet de spécifier les amplitudes maximum et minimum\n" +
                        "(Par défaut min=0.0 et max=1.0)")
                .argName("min max")
                .hasArgs()
                .numberOfArgs(2)
                .valueSeparator(' ')
                .build();

        // <----- CONCATENATE OPTIONS -----> //

        final Options options = new Options();

        options.addOption(aideOption);
            // <----- OPTIONS ETAPE 1 -----> //
        options.addOption(sondeOption);
        options.addOption(messageOption);
        options.addOption(germeOption);
            // <----- OPTIONS ETAPE 2 -----> //
        options.addOption(formeOption);
        options.addOption(nombreEchantillonOption);
        options.addOption(amplitudeOption);

        return options;
    }

    /**
     * La méthode execute effectue un envoi de message par la source de la chaîne de
     * transmission du Simulateur.
     *
     * @throws Exception si un problème survient lors de l'exécution
     */
    private void execute() throws Exception {
        source.emettre();
    }

    /**
     * La méthode qui calcule le taux d'erreur binaire en comparant les bits du
     * message émis avec ceux du message reçu.
     *
     * @return La valeur du Taux d'Erreur Binaire.
     */
    private float calculTauxErreurBinaire() {
        int nb_error = 0;

        Information<Boolean> informationsEmises, informationsRecues;
        informationsEmises = source.getInformationEmise();
        informationsRecues = destination.getInformationRecue();

        for (int i = 0; i < informationsEmises.nbElements(); i++) {
            if (!informationsEmises.iemeElement(i).equals(informationsRecues.iemeElement(i))) {
                nb_error++;
            }
        }

        return (float) nb_error / informationsEmises.nbElements() * 100;
    }

    /**
     * La fonction main instancie un Simulateur à l'aide des arguments paramètres et
     * affiche le résultat de l'exécution d'une transmission.
     *
     * @param args les différents arguments qui serviront à l'instanciation du
     *             Simulateur.
     */
    public static void main(String[] args) {
        Simulateur simulateur = null;

        try {
            simulateur = new Simulateur(args);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }

        if (simulateur.affichageAide)
            return;

        try {
            simulateur.execute();
            float tauxErreurBinaire = simulateur.calculTauxErreurBinaire();
            String s = "java  Simulateur  ";
            for (int i = 0; i < args.length; i++) {
                s += args[i] + "  ";
            }
            System.out.println(s + " =>   TEB : " + tauxErreurBinaire);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(-2);
        }
    }
}
