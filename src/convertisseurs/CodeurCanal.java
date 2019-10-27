package convertisseurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

/**
 * La classe CodeurCanal permet de créer un élément qui sera par la suite ajouté à une chaine de transmission afin de
 * coder le canal. Permettant ainsi de réduire les probléme de transmission
 */
public class CodeurCanal extends Transmetteur<Boolean, Boolean> {
    /**
     * Permet de recuperer l'information, la code et retransmet l'information
     * @param information  l'information  reçue
     * @throws InformationNonConforme
     */
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
        applicationEncodage();
        emettre();
    }

    /**
     * Fonction d'encodage pour ajouter le codage de canal :
     * les 1 deviennent des 101
     * Les 0 deviennent des 010
     */
    private void applicationEncodage() {
        informationEmise = new Information<>();
        for (Boolean datum : informationRecue) {
            if (datum) {  // EMISSION 1 -> 1 0 1
                informationEmise.add(true);   // 1
                informationEmise.add(false);  // 0
                informationEmise.add(true);   // 1
            } else {  // EMISSION 0 -> 0 1 0
                informationEmise.add(false);  // 0
                informationEmise.add(true);   // 1
                informationEmise.add(false);  // 0
            }
        }
    }

    /**
     * Pour chaque destination connectée, on transmet les informations reçues
     * @throws InformationNonConforme Cas d'erreur remonté par l'information
     */
    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Boolean> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }
    }
}
