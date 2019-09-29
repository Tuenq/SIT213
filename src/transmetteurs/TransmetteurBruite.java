package transmetteurs;

import bruit.*;
import communs.*;
import destinations.DestinationInterface;
import information.*;
import visualisations.SondeHistogramme;

public class TransmetteurBruite extends Transmetteur<Float,Float> {

    private float snr;
    private BruitGaussien bruit;

    public TransmetteurBruite(float snr) {
        this.snr = snr;
        bruit = new BruitGaussien();
    }

    public TransmetteurBruite(float snr, int seed) {
        this.snr = snr;
        bruit = new BruitGaussien(seed);
    }

    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        informationRecue = new Information<>(information);
        appliquerBruit();
        emettre();
    }

    private void appliquerBruit() {
        int data_length = informationRecue.nbElements();

        float puissanceMoyenne = Outils.puissanceMoyenne(informationRecue);
        float ecartType = Outils.ecartType(puissanceMoyenne, snr);

        bruit.initialiser(ecartType, data_length);
        Float[] donneesBruitee = bruit.appliquer(informationRecue, data_length);

        informationEmise = new Information<>(donneesBruitee);
    }

    /**
     * Permet l'émission des données vers les destinations connectées.
     * @throws InformationNonConforme Cas où l'information est invalide
     */
    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Float> destination : destinationsConnectees) {
                destination.recevoir(informationEmise);
        }
    }
}
