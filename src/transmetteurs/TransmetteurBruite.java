package transmetteurs;

import bruit.*;
import communs.*;
import destinations.DestinationInterface;
import information.*;

/**
 * La classe TransmetteurBruite héritant de Transmetteur permet de specifier un transmetteur ajoutant un bruit
 * gaussien à un signal le traverssant
 */
public class TransmetteurBruite extends Transmetteur<Float,Float> {

    private float snr;
    private BruitGaussien bruit;

    /**
     * Ajoute un bruit Gaussien au signal
     * @param snr rapport signal a bruit souhaite
     */
    public TransmetteurBruite(float snr) {
        this.snr = snr;
        bruit = new BruitGaussien();
    }

    /**
     * Ajoute un bruit Gaussien au singal
     * @param snr rapport singal a bruit souhaite
     * @param seed sequence aleatoire choisie
     */
    public TransmetteurBruite(float snr, int seed) {
        this.snr = snr;
        bruit = new BruitGaussien(seed);
    }
    /**
     * Permet de recuperer l'information, applique le bruit et retransmet l'information aux destinations connectees
     * @param information  l'information  reçue
     * @throws InformationNonConforme Cas d'erreur remonté par l'information
     */
    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        informationRecue = information;
        appliquerBruit();
        emettre();
    }

    /**
     * Permet d'ajouter un bruit a un signal en fonction du snr et de la formule entre le rapport Eb/N0 et l'ecart type
     */
    private void appliquerBruit() {
        int data_length = informationRecue.nbElements();
        float puissanceMoyenne = informationRecue.getPuissaanceMoyenne();
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
