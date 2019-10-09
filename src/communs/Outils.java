package communs;

import information.Information;

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

    public static float puissanceMoyenne(Information<Float> signal) {
        float power = 0;  // Initialisation somme des échantillons
        for (Float datum: signal) {
            power += Math.pow(datum, 2);
        }
        return power / signal.nbElements();
    }

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

    public static boolean isEven(int n) {
        return (n % 2 == 0);
    }

    public static boolean isOdd(int n) {
        return (n % 2 == 1);
    }

    public static double[] convertToDoubleArray(float[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    public static double[] convertToDoubleArray(Float[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    public static float[] convertToFloatArray(double[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) floatArray[i] = (float) array[i];
        return floatArray;
    }

    public static double getMaximumValue(double[] array) {
        double max = 0;
        for (double datum:array) {
            if (datum > max)
                max = datum;
        }
        return max;
    }

    public static double getMinimumValue(double[] array) {
        double min = 0;
        for (double datum:array) {
            if (datum < min)
                min = datum;
        }
        return min;
    }
}
