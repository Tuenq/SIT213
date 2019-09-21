package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

/**
 * Classe du transmetteur logique parfait, celui-ci reçois et émet (pour chaque destination qui lui est connectée)
 * le type de donnée sans les modifier et sans impacter les informations.
 * @author Lucas, Sebastien
 */
public class TransmetteurParfait<T> extends Transmetteur <T, T> {

    /**
     * Permet la réception des données.
     * @param information  l'information reçue
     */
    @Override
    public void recevoir(Information<T> information) throws InformationNonConforme {
        informationRecue = new Information<>(information);
        emettre();
    }

    /**
     * Permet l'émission des données vers les destinations connectées.
     * @throws InformationNonConforme
     */
    @Override
    public void emettre() throws InformationNonConforme {
        informationEmise = new Information<>(informationRecue);
        for (DestinationInterface<T> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }
    }
}
