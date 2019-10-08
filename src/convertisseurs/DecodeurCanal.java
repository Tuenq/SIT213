package convertisseurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

import java.util.Iterator;

public class DecodeurCanal extends Transmetteur<Boolean, Boolean> {

    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
        applicationDecodage();
        emettre();
    }

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

    boolean tableDeVerite(boolean r0, boolean r1, boolean r2) {
        boolean and0 = !r1 && r2;       // AND
        boolean and1 = r0 && !r1;       // AND
        boolean and2 = r0 && r2;        // AND
        return and0 || and1 || and2;    // OR
    }

    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Boolean> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }
    }
}
