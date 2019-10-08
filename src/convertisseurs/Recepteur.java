package convertisseurs;

import communs.Outils;
import destinations.DestinationInterface;
import filtres.Filtre;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

public class Recepteur extends Transmetteur<Float,Boolean> {

    private int nombreEchantillon;
    private float amplMinBruitee = 0;
    private float amplMaxBruitee = 0;

	public Recepteur(int nombreEchantillon) {
	    this.nombreEchantillon = nombreEchantillon;
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
            if (datum > amplMaxBruitee)
                amplMaxBruitee = datum;
            else if (datum < amplMinBruitee)
                amplMinBruitee = datum;
        }
    }

    private void decodage(){
	    informationEmise = new Information<>();
        final int nbEch = nombreEchantillon;
        int compteur = 0;
        float sum = 0;

        Float[] ampl = Outils.calculAmplitude(informationRecue);
        amplMinBruitee = ampl[Outils.amplIndex.MIN.ordinal()];
        amplMaxBruitee = ampl[Outils.amplIndex.MAX.ordinal()];

        float tier1 = (1/3f) * nbEch;
        float tier2 = (2/3f) * nbEch;

        for (Float datum: informationRecue) {
            if (compteur >= tier1 && compteur < tier2)
                sum += datum;

            if (++compteur == nbEch) {
                float mean = sum/tier1;
                boolean EstimatedValue = Outils.booleanDistance(mean, amplMinBruitee, amplMaxBruitee);
                informationEmise.add(EstimatedValue);  // FIXME: PREALLOCATE ARRAY
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


