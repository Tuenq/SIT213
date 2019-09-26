package common;

public class Tools {
    /**
     * Permet de déterminer, à partir des amplitudes théoriques, le symbole en fonction de la moyenne passé.
     *
     * @param mean Valeur moyenne du signal.
     * @return Symbole décidé selon la distance entre les amplitudes maximum et minimum.<br>
     * Dans le cas où la valeur moyenne est à équidistance des limites, la valeur false est retournée.
     */
    public static boolean booleanDistance(float mean, float amplMin, float amplMax) {
        float maxDistance = Math.abs(mean - amplMax);
        float minDistance = Math.abs(mean - amplMin);

        return maxDistance < minDistance;
    }
}
