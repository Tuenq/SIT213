package convertisseurs;

import destinations.DestinationInterface;
import filtres.*;
import information.*;
import transmetteurs.Transmetteur;


public class Emetteur extends Transmetteur<Boolean,Float> {
	private FiltreMiseEnForme filtreMiseEnForme;

	public Emetteur(FiltreMiseEnForme.forme formeOnde, int nombreEchantillon, float amplitudeMin, float amplitudeMax) {
        configureFiltre(formeOnde, nombreEchantillon, amplitudeMin, amplitudeMax);
	}

    /**
     * Permet de creer le filtre de mise en forme a partir de la forme souhaitee et des caracteristiques du filtre souhaitees
     * @param fo choix de la forme
     * @param ne nombre d'echantillons
     * @param ami amplitude minimale
     * @param ama amplitude maximale
     */
	private void configureFiltre(FiltreMiseEnForme.forme fo, int ne, float ami, float ama) {
        switch (fo) {
            case RZ:
                filtreMiseEnForme = new FiltreMiseEnFormeRZ(ne, ami, ama);
                break;

            case NRZ:
                filtreMiseEnForme = new FiltreMiseEnFormeNRZ(ne, ami, ama);
                break;

            case NRZT:
                filtreMiseEnForme = new FiltreMiseEnFormeNRZT(ne, ami, ama);
                break;
        }
    }

    /**
     * @return le filtre de mise en forme
     */
    public FiltreMiseEnForme getFiltreMiseEnForme() {
	    return filtreMiseEnForme;
    }
    /**
     * Permet de recuperer l'information, la code et retransmet l'information aux destinations connectees
     * @param information  l'information  reçue
     * @throws InformationNonConforme Cas d'erreur remonté par l'information
     */
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
        conversionInformation();
        emettre();
    }

    private void conversionInformation() {
        float[] data_conversion = filtreMiseEnForme.echantillonage(informationRecue);
        filtreMiseEnForme.appliquer(data_conversion);
        informationEmise = new InformationAnalogique(data_conversion);
        informationEmise.setPuissanceMoyenne(filtreMiseEnForme.getPuissanceMoyenneSortieFiltre());
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
