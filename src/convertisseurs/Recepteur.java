package convertisseurs;

import common.Tools;
import destinations.DestinationInterface;
import filtres.Filtre;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

public class Recepteur extends Transmetteur<Float,Boolean> {

    private Filtre filtre;

    private float amplMinBruitee = 0;
    private float amplMaxBruitee = 0;

	public Recepteur(Filtre filtre) {
	    this.filtre = filtre;
	}

    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
	    informationRecue = information;
        calculAmplBruite();
        decodage();
        emettre();
    }

    private void calculAmplBruite () {
        for (Float datum: informationRecue){
            if (datum > amplMaxBruitee){
                amplMaxBruitee = datum;
            }
            if (datum < amplMinBruitee){
                amplMinBruitee = datum;
            }
        }
    }

    private void decodage(){
	    informationEmise = new Information<>();
        final int nbEch = filtre.nbEch;
        int compteur = 0;
        float sum = 0;

        for (Float datum: informationRecue) {
            sum += datum;
            if (++compteur == nbEch) {
                float mean = sum/nbEch;
                boolean EstimatedValue = Tools.booleanDistance(mean, amplMinBruitee, amplMaxBruitee);
                informationEmise.add(EstimatedValue);
                sum = compteur = 0;
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


