import org.apache.commons.cli.*;
import sources.*;
import destinations.*;
import transmetteurs.*;

import information.*;
import visualisations.Sonde;
import visualisations.SondeLogique;

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

    /**
     * le composant Source de la chaine de transmission
     */
    private Source<Boolean> source = null;
    /**
     * le composant Transmetteur parfait logique de la chaine de transmission
     */
    private Transmetteur<Boolean, Boolean> transmetteurLogique = null;
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
            final String footer = "\nProjet réalisé par M.Bartoli, L.Dumestre, O.Gueye, S.HugdeLarauze et F.Ludovic\n";

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Simulateur", header, options, footer, true);
            return;
        }

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
    }

    /**
     * Permet de définir les options disponibles en ligne de commande
     * @return un objet contenant les différentes options
     */
    private static Options configParameters() {
        final Option sondeOption = Option.builder("s")
                .desc("Active l'utilisation des sondes")
                .build();

        final Option messageOption = Option.builder("mess")
                .desc("Permet de préciser le message ou la longueur du message")
                .hasArg()
                .argName("m")
                .build();

        final Option germeOption = Option.builder("seed")
                .desc("Permet d'initialiser le générateur aléatoire avec une germe spécifique")
                .hasArg()
                .argName("v")
                .build();

        final Option aideOption = Option.builder("h")
                .desc("Affiche la liste des usages")
                .build();

        final Options options = new Options();

        options.addOption(sondeOption);
        options.addOption(messageOption);
        options.addOption(germeOption);
        options.addOption(aideOption);

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
            System.out.println(s + "  =>   TEB : " + tauxErreurBinaire);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(-2);
        }
    }

}
