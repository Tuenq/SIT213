package convertisseurs;

import destinations.DestinationInterface;
import filtres.*;
import information.*;
import transmetteurs.Transmetteur;

/**
 * La classe Emeteur permet de specifier un élément qui sera ensuite ajouté dans une chaine de transmission.
 *
 * L'émetteur récupére en entrer une suite de symbole binaire 0 ou 1 et les sors modulé sous une forme specifier,
 * RZ, NRZ, ou NRZT. Le nombre d'échantillon par symbole est spécifié ainsi que les amplitudes maximale et minimale.
 */
public class Emetteur extends Transmetteur<Boolean,Float> {
	private FiltreMiseEnForme filtreMiseEnForme;

    /**
     * Constructeur de la classe spécifiant les paramétres de l'émetteur
     * @param formeOnde forme de la modulation
     * @param nombreEchantillon le nombre d'échantillon par symbole
     * @param amplitudeMin l'amplitude minimale des formes
     * @param amplitudeMax l'amplitude maximale des formes
     */
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

    /**
     * Convertis les informations reçus en fonction des paramétres spécifier pour l'émetteur
     */
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
