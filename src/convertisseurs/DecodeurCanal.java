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

            if ()
        }
    }

    private boolean tableDeVerite(boolean r0, boolean r1, boolean r2) {
        if (r0 && r1 && r2)       // 111 -> 1
            return true;
        else if (r0 && r1 && !r2) // 110 -> 0
            return false;

    }

    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Boolean> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }
    }
}
