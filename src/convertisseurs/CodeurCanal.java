package convertisseurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

public class CodeurCanal extends Transmetteur<Boolean, Boolean> {

    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
        applicationEncodage();
        emettre();
    }

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

    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Boolean> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }
    }
}
