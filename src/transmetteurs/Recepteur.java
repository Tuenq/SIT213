package transmetteurs;

import destinations.DestinationInterface;
import encoders.Encoder;
import information.Information;
import information.InformationNonConforme;

public class Recepteur extends Transmetteur<Float,Boolean> {

    private Encoder encoder;

	public Recepteur(Encoder encoder) {
	    this.encoder = encoder;
	}
    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        informationRecue = information;
        informationEmise = encoder.decodage(informationRecue);
        emettre();
    }

    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Boolean> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }

    }

}


