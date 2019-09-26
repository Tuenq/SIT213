package convertisseurs;

import common.Tools;
import destinations.DestinationInterface;
import filtres.Filtre;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

public class Recepteur extends Transmetteur<Float,Boolean> {

    private Filtre filtre;

	public Recepteur(Filtre filtre) {
	    this.filtre = filtre;
	}
    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        informationRecue = information;
        informationEmise = decodage(informationRecue);
        emettre();
    }

    private Information<Boolean> decodage(Information<Float> data){
        Information<Boolean> dataOut = new Information<>();
        final int nbEch = filtre.nbEch;
        int compteur = 0;
        float sum = 0;

        for (Float datum: data) {
            sum += datum;
            if (++compteur == nbEch) {
                float mean = sum/nbEch;
                boolean EstimatedValue = Tools.booleanDistance(mean, filtre.amplMin, filtre.amplMax);
                dataOut.add(EstimatedValue);
                sum = compteur = 0;
            }
        }
        return dataOut;
    }

    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Boolean> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }

    }

}


