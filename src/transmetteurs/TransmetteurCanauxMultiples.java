package transmetteurs;

import canaux.CanalIndirect;
import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class TransmetteurCanauxMultiples extends Transmetteur<Float,Float> {

    private int nombreTrajet;
    private CanalIndirect[] trajets;

    public TransmetteurCanauxMultiples(Integer[] distancesTemporelles, Float[] amplitudesRelatives) {
        if (distancesTemporelles.length != amplitudesRelatives.length)
            throw new IllegalArgumentException("Les tableaux de données doivent être de même taille");

        nombreTrajet = distancesTemporelles.length;
        trajets = new CanalIndirect[nombreTrajet];

        for (int trajet_i = 0; trajet_i < distancesTemporelles.length; trajet_i++) {
            trajets[trajet_i] = new CanalIndirect(distancesTemporelles[trajet_i], amplitudesRelatives[trajet_i]);
        }
    }

    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        informationRecue = information;
        simulationTrajetsMultiples();
    }

    private void simulationTrajetsMultiples() {
        Float[][] trajetsIndirects = new Float[informationRecue.nbElements()][nombreTrajet];

        // Simulation des différents trajets
        for (int trajet_i = 0; trajet_i < nombreTrajet; trajet_i++) {
            trajetsIndirects[trajet_i] = trajets[trajet_i].simulationTrajet(informationRecue.getArray());
        }

        Float[] dataOut = informationRecue.getArray();

        // Addition des différents trajets
        for (int i = 0; i < dataOut.length; i++) {
            for (Float[] trajetIndirect: trajetsIndirects) {
                dataOut[i] += trajetIndirect[i];
            }
        }

        informationEmise = new Information<>(dataOut);
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
