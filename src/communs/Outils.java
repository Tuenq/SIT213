package communs;

import information.Information;

/**
 * Boite à outil pour réaliser différents calcul et méthode dans le reste du projet
 */
public class Outils {
    /**
     * Permet de déterminer, à partir des amplitudes théoriques, le symbole en fonction de la moyenne passé.
     *
     * @param mean Valeur moyenne du signal.
     * @param amplMin Valeur de la limite basse pour l'amplitude
     * @param amplMax Valeur de la limite haute pour l'amplitude
     * @return Symbole décidé selon la distance entre les amplitudes maximum et minimum.<br>
     * Dans le cas où la valeur moyenne est à équidistance des limites, la valeur false est retournée.
     */
    public static boolean booleanDistance(float mean, float amplMin, float amplMax) {
        float maxDistance = Math.abs(mean - amplMax);
        float minDistance = Math.abs(mean - amplMin);

        return maxDistance < minDistance;
    }

    /**
     * Calcul l'écart type d'une fonction gaussienne en fonction de la puissance moyenne du signal et du SNR
     * @param puissanceMoyenne puissance moyenne du signal
     * @param snr SNR fixé
     * @return écart type
     */
    public static float ecartType(float puissanceMoyenne, float snr) {
        double denom = Math.pow(10, snr / 10f);
        return (float) Math.sqrt(puissanceMoyenne / denom);
    }

    public enum amplIndex { MIN, MAX; }

    /**
     * Calcule l'amplitude minimum et maximum de l'information
     * @param data Information d'amplitude inconnue
     * @return Tableau de taille 2 avec min puis max
     */
    public static Float[] calculAmplitude(Information<Float> data) {
        Float[] ampl = new Float[]{0f,0f};  // [MIN,MAX]

        for (Float datum: data) {
            if (datum < ampl[amplIndex.MIN.ordinal()])
                ampl[amplIndex.MIN.ordinal()] = datum;
            else if (datum > ampl[amplIndex.MAX.ordinal()])
                ampl[amplIndex.MAX.ordinal()] = datum;
        }
        return ampl;
    }

    public static String[] concatenate(String[] ... parms) {
        // calculate size of target array
        int size = 0;
        for (String[] array : parms) {
            size += array.length;
        }

        String[] result = new String[size];

        int j = 0;
        for (String[] array : parms) {
            for (String s : array) {
                result[j++] = s;
            }
        }
        return result;
    }

    /**
     * Vérifie si la valeu en paramétre est un multiple de 2
     * @param n valeur
     * @return boolean
     */
    public static boolean isEven(int n) {
        return (n % 2 == 0);
    }

    /**
     * Vérifie si la valeu en paramétre n'est pas un multiple de 2
     * @param n valeur
     * @return boolean
     */
    public static boolean isOdd(int n) {
        return (n % 2 == 1);
    }

    /**
     * Converti un tableau de float en un tableau de double
     * @param array tableau de float
     * @return tableau de double
     */
    public static double[] convertToDoubleArray(float[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    /**
     * Converti un tableau de Float en un tableau de double
     * @param array tableau de Float
     * @return tableau de double
     */
    public static double[] convertToDoubleArray(Float[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    /**
     * Converti un tableau de double en un tableau de float
     * @param array tableau de double
     * @return tableau de float
     */
    public static float[] convertToFloatArray(double[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) floatArray[i] = (float) array[i];
        return floatArray;
    }

    /**
     * Renvoi la valeur max d'un tableau de double
     * @param array tableau de double
     * @return valeur max
     */
    public static double getMaximumValue(double[] array) {
        double max = 0;
        for (double datum:array) {
            if (datum > max)
                max = datum;
        }
        return max;
    }

    /**
     * Renvoi la valeur min d'un tableau de double
     * @param array tableau de double
     * @return valeur min
     */
    public static double getMinimumValue(double[] array) {
        double min = 0;
        for (double datum:array) {
            if (datum < min)
                min = datum;
        }
        return min;
    }

    /**
     * Verify if the two float value have the same sign
     * @param x Float value
     * @param y Float value
     * @return true if value are the same size, else false
     */
    public static boolean checkSameSign(float x, float y) {
        return (x >= 0) ^ (y < 0);
    }
}
