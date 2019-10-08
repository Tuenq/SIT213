package convertisseurs;

import destinations.DestinationInterface;
import filtres.*;
import information.*;
import transmetteurs.Transmetteur;

import java.util.Arrays;

import static filtres.Filtre.miseEnForme.*;


public class Emetteur extends Transmetteur<Boolean,Float> {
	private Filtre filtreMiseEnForme;

	public Emetteur(Filtre.miseEnForme formeOnde, int nombreEchantillon, float amplitudeMin, float amplitudeMax) {
        switch (formeOnde) {
            case RZ:
                filtreMiseEnForme = new FiltreRZ(nombreEchantillon, amplitudeMin, amplitudeMax);
                break;

            case NRZ:
                filtreMiseEnForme = new FiltreNRZ(nombreEchantillon, amplitudeMin, amplitudeMax);
                break;

            case NRZT:
                filtreMiseEnForme = new FiltreNRZT(nombreEchantillon, amplitudeMin, amplitudeMax);
                break;
        }
	}
    
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
        conversionInformation();
        emettre();
    }

    private void conversionInformation() {
        Float[] data_conversion = echantillonage(informationRecue);
        informationEmise = applicationFiltreMiseEnForme(data_conversion);
    }

    private Float[] echantillonage(Information<Boolean> data) {
	    int cpt = 0;
	    Float[] dataOut = new Float[data.nbElements() * filtreMiseEnForme.nbEch];

        for (Boolean datum : data) {
            final float value_ech = datum ? filtreMiseEnForme.amplMax : filtreMiseEnForme.amplMin;
            final int index_start = cpt++ * filtreMiseEnForme.nbEch;
            final int index_stop = index_start + filtreMiseEnForme.nbEch;

            Arrays.fill(dataOut, index_start, index_stop, value_ech);
        }
        return dataOut;
    }

    private Information<Float> applicationFiltreMiseEnForme(Float[] data) {
        filtreMiseEnForme.appliquerMiseEnForme(data);
        return new Information<>(data);
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
