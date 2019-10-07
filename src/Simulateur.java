import communs.Outils;
import convertisseurs.Emetteur;
import convertisseurs.Recepteur;
import org.apache.commons.cli.*;
import sources.*;
import destinations.*;
import transmetteurs.*;

import filtres.*;
import filtres.Filtre.encoders;

import information.*;
import visualisations.*;

import java.util.Arrays;
import java.util.Iterator;

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
    private int nombreEchantillon = 30;
    /**
     * L'amplitude maximum des encodeurs/décodeurs analogique/numérique (initialisée par -ampl min max)
      */
    private Float amplitudeMax = 1.0f;
    /**
     * L'amplitude minimum des encodeurs/décodeurs analogique/numérique (initialisée par -ampl min max)
     */
    private Float amplitudeMin = 0.0f;

    // <----- OPTIONS ETAPE 3 -----> //

    /**
     * Ratio signal/bruit utilisé pour la génération du bruit
     */
    private Float signalNoiseRatio = 20.0f;
    /**
     * Boolean si le canal est bruité ou non
     */
    private Boolean snr = false;

    // <----- OPTIONS ETAPE 4 -----> //

    private boolean trajetMultiple = false;
    private int[] decalageTemporel;
    private Float[] amplitudeRelative;

    // <----- OPTIONS PERSO -----> //

    /**
     * Boolean si le TEB doit être enregistré dans un fichier csv
     */
    private Boolean csv = false;
    /**
     * Renseigne si les signaux trajets multiples doivent �tre affich�s
     */
    boolean atm = false;
    /**
     * String définissant dans quel fichier csv il faut ajouter le TEB
     */
    private String csvFile = "";

    // <----- SIMULATEUR -----> //

    /**
     * le composant Source de la chaine de transmission
     */
    private Source<Boolean> source = null;
    /**
     * Codeur permettant de transformer l'information analogique dans la forme d'onde spécifiée
     */
    private Filtre filtre = null;
    /**
     * le composant Emetteur de la chaine de transmission
     */
    private Emetteur emetteur = null;
    /**
     * le composant Transmetteur de la chaine de transmission
     */
    private Transmetteur<Float,Float> transmetteur = null;
    private Transmetteur<Float,Float> transmetteurTM = null;
    /**
     * le composant Recepteur de la chaine de transmission
     */
    private Recepteur recepteur = null;
    /**
     * le composant Destination de la chaine de transmission
     */
    private Destination<Boolean> destination = null;
   

    /**
     * Le constructeur de Simulateur construit une chaîne de transmission composée
     * d'une Source Boolean, d'une Destination Boolean et de Transmetteur(s)
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
        simulation();
    }

    private void simulation() throws ArgumentsException {

        // Configuration de la SOURCE ALEATOIRE|FIXE
        if (messageAleatoire) {
            if (aleatoireAvecGerme)
                source = new SourceAleatoire(nbBitsMess, seed);
            else
                source = new SourceAleatoire(nbBitsMess);
        }
        else {
            try {
                source = new SourceFixe(Information.stringToBoolean(messageString));
            }
            catch (InformationNonConforme exception) {
                throw new ArgumentsException(exception.toString());
            }
        }

        // Configuration du filtre
        switch (formeOnde) {
            case RZ:
                filtre = new FiltreRZ(nombreEchantillon, amplitudeMin, amplitudeMax);
                break;

            case NRZ:
                filtre = new FiltreNRZ(nombreEchantillon, amplitudeMin, amplitudeMax);
                break;

            case NRZT:
                filtre = new FiltreNRZT(nombreEchantillon, amplitudeMin, amplitudeMax);
                break;
        }

        // Configuration d'emetteur
        emetteur = new Emetteur(filtre);

        //Configuration des transmetteur
        if (snr){
            // Configuration du TRANSMETTEUR BRUITE
            if (aleatoireAvecGerme)
                transmetteur = new TransmetteurBruite(signalNoiseRatio, seed);
            else
                transmetteur = new TransmetteurBruite(signalNoiseRatio);
        }
        else transmetteur = new TransmetteurParfait<>();

        if (trajetMultiple) {
        	transmetteurTM = new TransmetteurTrajetMultiples(amplitudeRelative, decalageTemporel);
        	if(atm)
        		((TransmetteurTrajetMultiples) transmetteurTM).afficherTrajetsIndirects();
        	}
        else
            transmetteurTM = new TransmetteurParfait<>();

        //Configuration de recepteur
        recepteur = new Recepteur(filtre);

        // Configuration de la DESTINATION
        destination = new DestinationFinale();

        // Connexion des composants
        source.connecter(emetteur);
        emetteur.connecter(transmetteurTM);
        transmetteurTM.connecter(transmetteur);
        transmetteur.connecter(recepteur);
        recepteur.connecter(destination);

        if (affichage) {
            // Ajout des SONDES LOGIQUES
            Sonde<Boolean> sonde_entree = new SondeLogique("Entrée du système", nombreEchantillon);
            source.connecter(sonde_entree);
            Sonde<Boolean> sonde_sortie = new SondeLogique("Sortie du système", nombreEchantillon);
            recepteur.connecter(sonde_sortie);
            // Ajout des SONDES ANALOGIQUES
            Sonde<Float> sonde_emission = new SondeAnalogique("Emission du système");
            emetteur.connecter(sonde_emission);
            Sonde<Float> sonde_reception = new SondeAnalogique("Réception du système");
            transmetteur.connecter(sonde_reception);
            // Ajout des SONDES PUISSANCES
            Sonde<Float> sonde_emission_p = new SondePuissance("Emission du système - Puisance");
            emetteur.connecter(sonde_emission_p);
            Sonde<Float> sonde_reception_p = new SondePuissance("Réception du système - Puisance");
            transmetteur.connecter(sonde_reception_p);
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

        if (commandLine.hasOption("mess")) {
            messageString = commandLine.getOptionValue("mess");
            if (messageString.matches("[0-1]{7,}")) {
                messageAleatoire = false;
                nbBitsMess = messageString.length();
            }
            else if (messageString.matches("[0-9]{1,6}")) {
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
                throw new ArgumentsException("Valeur du parametre -nbEch inférieure ou égale à 0 : " + nombreEchantillon);
            else if (nombreEchantillon < 30 && !commandLine.hasOption("mute"))
                System.out.println("\n*************************************************************************************************************************************************\n" +
                        "WARNING : vous avez demandé " + nombreEchantillon + " échantillons. Le faible nombre d'échantillons risque d'altérer la forme du signal. Nous vous conseillons 100 échantillons" +
                        "\n*************************************************************************************************************************************************\n");
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
                throw new ArgumentsException("Valeur du parametre -ampl ne respecte pas min < max : " + Arrays.toString(amplitudes));
            }
        }

        // <----- OPTIONS ETAPE 3 -----> //

        snr = commandLine.hasOption("snr");

        if (commandLine.hasOption("snr")) {
            try {
                signalNoiseRatio = Float.parseFloat(commandLine.getOptionValue("snr"));
            }
            catch (NumberFormatException e) {
                throw new ArgumentsException("Valeur du parametre -snr invalide : " + commandLine.getOptionValue("snr"));
            }
            if (signalNoiseRatio < -5f && !commandLine.hasOption("mute")) {
                System.out.println("\n****************************************************************************************************************************************************************\n" +
                        "WARNING : la Valeur du parametre -snr est faible : " + signalNoiseRatio  +" (La puissance du bruit est importante par rapport a la puissance du signal). Nous vous conseillons un snr de 5." +
                        "\n****************************************************************************************************************************************************************\n");
            }
        }
        // <----- OPTIONS ETAPE 4 -----> //
        
        trajetMultiple = commandLine.hasOption("ti");
        if (trajetMultiple) {
            String[] optionsValues = commandLine.getOptionValues("ti");

            if (Outils.isOdd(optionsValues.length))
                throw new ArgumentsException("Valeurs du paramètre -ti doivent etre par couple de 2 valeurs : " + String.join(" ", optionsValues));

            int sizeArray = optionsValues.length / 2;
//            if (sizeArray > 4) {
//                throw new ArgumentsException("Le nombre de trajets multiples maximum est 5 et non :" + (sizeArray+1) + " trajets multiples.");
//            }
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
                else if (decalageTemporel[i] > nombreEchantillon) {
                    System.out.println("\n****************************************************************************************************************************************************************\n" +
                            "WARNING : le decalage du "+ (int)(i+1) +"eme trajet multiple est important: " + decalageTemporel[i]  +" cela risque d'entrainer un risque d'erreurs important" +
                            "\n****************************************************************************************************************************************************************\n");
                }
                if (amplitudeRelative[i] < 0) {
                    throw new ArgumentsException("L'attenuation minimale est 0, elle ne peut pas etre négative (l'attenuation est un rapport par rapport au signal direct)");
                }
                else if (amplitudeRelative[i] > 1) {
                    throw new ArgumentsException("L'attenuation maximale est 1, l'attenuation ne peut pas amplifier le signal");
                }
            }
        }

        // <----- OPTIONS PERSO -----> //

        
        atm = commandLine.hasOption("atm");
        if (commandLine.hasOption("csv")){
            atm = true;
        }
        
        csv = commandLine.hasOption("csv");
        if (commandLine.hasOption("csv")){
            csvFile = commandLine.getOptionValue("csv");
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

        final Option silenceOption = Option.builder("mute")
                .desc("Supprime les sorties des cas limites")
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

        // <----- OPTIONS ETAPE 3 -----> //

        final Option snrOption = Option.builder("snr")
                .desc("Permet de spécifier le rapport signal sur bruit (en dB)\n" +
                        "(Par défaut, message de longueur 100)")
                .hasArg()
                .argName("s")
                .build();

        // <----- OPTIONS ETAPE 4 -----> //

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

        // <----- OPTIONS PERSO -----> //

        final Option csvOption = Option.builder("csv")
                .desc("Permet d'enregistrer dans un fichier défini")
                .hasArg()
                .argName("csv")
                .build();

        final Option atmOption = Option.builder("atm")
                .desc("Permet d'afficher les signaux trajet multiple")
                .argName("atm")
                .build();


        // <----- CONCATENATE OPTIONS -----> //

        final Options options = new Options();

        options.addOption(aideOption);
        options.addOption(silenceOption);
            // <----- OPTIONS ETAPE 1 -----> //
        options.addOption(sondeOption);
        options.addOption(messageOption);
        options.addOption(germeOption);
            // <----- OPTIONS ETAPE 2 -----> //
        options.addOption(formeOption);
        options.addOption(nombreEchantillonOption);
        options.addOption(amplitudeOption);
            // <----- OPTIONS ETAPE 3 -----> //
        options.addOption(snrOption);
        // <----- OPTIONS ETAPE 4 -----> //
        options.addOption(trajetIndirectOption);
        // <----- OPTIONS PERSO -----> //
        options.addOption(csvOption);
        options.addOption(atmOption);
        
        return options;
    }

    /**
     * La méthode execute effectue un envoi de message par la source de la chaîne de
     * transmission du Simulateur.
     *
     * @throws Exception si un problème survient lors de l'exécution
     */
    public void execute() throws Exception {  // TODO: Add test that check public access
        source.emettre();
    }

    /**
     * La méthode qui calcule le taux d'erreur binaire en comparant les bits du
     * message émis avec ceux du message reçu.
     *
     * @return La valeur du Taux d'Erreur Binaire.
     */
    public float calculTauxErreurBinaire() {  // TODO: Add test that check public access
        int nb_error = 0;
        Information<Boolean> data_sent = source.getInformationEmise();
        Iterator<Boolean> datum_received_iterator = destination.getInformationRecue().iterator();

        for (Boolean datum_sent: data_sent) {
            boolean datum_received = datum_received_iterator.next();
            if (datum_sent != datum_received) nb_error++;
        }

        float teb = (float)nb_error / data_sent.nbElements();
        
        if (csv){
            extractionCSV.sauvegardeData(csvFile, teb, signalNoiseRatio);
        }
        return teb;
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
