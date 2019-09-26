package convertisseurs;

import destinations.DestinationInterface;
import filtres.Filtre;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

import java.util.Arrays;


public class Emetteur extends Transmetteur<Boolean,Float> {
	private Filtre filtre;

	public Emetteur(Filtre filtre) {
        this.filtre = filtre;
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
	    Float[] dataOut = new Float[data.nbElements() * filtre.nbEch];

        for (Boolean datum : data) {
            final float value_ech = datum ? filtre.amplMax : filtre.amplMin;
            final int index_start = cpt++ * filtre.nbEch;
            final int index_stop = index_start + filtre.nbEch;

            Arrays.fill(dataOut, index_start, index_stop, value_ech);
        }
        return dataOut;
    }

    private Information<Float> applicationFiltreMiseEnForme(Float[] data) {
        filtre.appliquerMiseEnForme(data);
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
