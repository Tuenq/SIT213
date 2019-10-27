package convertisseurs;

import communs.Outils;
import destinations.DestinationInterface;
import filtres.FiltreAdapte;
import filtres.FiltreMiseEnForme;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

public class Recepteur extends Transmetteur<Float,Boolean> {
    private FiltreAdapte filtreAdapte;

	public Recepteur(FiltreMiseEnForme filtre) {
	    filtreAdapte = new FiltreAdapte(filtre);
	}

    /**
     * Permet de recuperer l'information, la code et retransmet l'information aux destinations connectees
     * @param information  l'information  reçue
     * @throws InformationNonConforme Cas d'erreur remonté par l'information
     */
    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
	    informationRecue = information;
        decodage();
        emettre();
    }

    private void decodage(){
	    informationEmise = new Information<>();
	    Float[] dataIn = informationRecue.getArray();
	    double[] signal = Outils.convertToDoubleArray(dataIn);

	    Boolean[] dataOut = filtreAdapte.appliquer(signal);

	    informationEmise = new Information<>(dataOut);
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


