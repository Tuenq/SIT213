package information;

/**
 * La classe InformationAnalogique hérite de la classe Information et stock des données de type float
 */
public class InformationAnalogique extends Information<Float> {

    /**
     * Constructeur de la classe, permet de cloner les donnée présente dans le tableau de float data et de les stocker
     * dans le nouvel object InformationAnalogique
     * @param data les data à cloner
     */
    public InformationAnalogique(float[] data) {
        super();
        for (float datum:data)
            this.add(datum);
    }
}
