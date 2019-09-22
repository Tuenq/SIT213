package transmetteurs;

import destinations.DestinationInterface;
import encoders.Encoder;
import information.Information;
import information.InformationNonConforme;


public class Emetteur extends Transmetteur<Boolean,Float> {
	private Encoder encoder;

	public Emetteur(Encoder encoder) {
        this.encoder = encoder;
	}
    
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
	    informationEmise = encoder.codage(informationRecue);
        emettre();
    }

    /**
     * Pour chaque destination connectée, on transmet les informations reçues
     * @throws InformationNonConforme Cas d'erreur remonté par l'information
     */
    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Float> destination : destinationsConnectees) {
        	destination.recevoir(informationEmise);
        }
    }
}
