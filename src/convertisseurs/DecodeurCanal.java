package convertisseurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

import java.util.Iterator;

public class DecodeurCanal extends Transmetteur<Boolean, Boolean> {
    /**
     * Permet de recuperer l'information, la decode et retransmet l'information aux destinations connectees
     * @param information  l'information  reçue
     * @throws InformationNonConforme
     */
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
        applicationDecodage();
        emettre();
    }

    /**
     * Permet de determiner la sequence de bit decodee a partir d'une table de verite
     */
    private void applicationDecodage() {
        Boolean r0, r1, r2;
        informationEmise = new Information<>();
        Iterator<Boolean> received = informationRecue.iterator();

        while (received.hasNext()) {
            r0 = received.next();
            r1 = received.next();
            r2 = received.next();

            informationEmise.add(tableDeVerite(r0, r1, r2));
        }
    }

    /**
     * Permet de determiner si la sequence envoyee correspond a un 0 ou un 1 en prenant en compte les erreurs
     * @param r0 premier bit de la sequence a determiner
     * @param r1 deuxiemme bit de la sequence a determiner
     * @param r2 troisieme bit de la sequence a determiner
     * @return le bit correspondant a la sequence
     */
    boolean tableDeVerite(boolean r0, boolean r1, boolean r2) {
        boolean and0 = !r1 && r2;       // AND
        boolean and1 = r0 && !r1;       // AND
        boolean and2 = r0 && r2;        // AND
        return and0 || and1 || and2;    // OR
    }
    /**
     * Pour chaque destination connectée, on transmet les informations reçues aux destinations coonnectees
     * @throws InformationNonConforme Cas d'erreur remonté par l'information
     */
    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Boolean> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }
    }
}
