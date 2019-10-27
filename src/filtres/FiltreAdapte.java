package filtres;

import communs.Outils;
import org.apache.commons.math3.util.MathArrays;
import visualisations.VueCourbe;

import java.util.Arrays;

/**
 * <p>Implémentation du filtre adapté pour la réception des informations analogique.</p>
 * <p><b>COUVERTURE</b> : Couvert par <i>SimulateurTest</i>.</p>
 */
public class FiltreAdapte {
    private boolean sameAmplSign;
    private double amplRatio;

    private boolean zeroingAmpl;
    private boolean positiveZeroing;

    private double seuilSymbole;
    private double[] symbole;

    private int nombreEchantillon;
    private double[] donneesSignal;


    /**
     * <p>Récupère le symbole unitaire du filtre de mise en forme utilisé.</p>
     *
     * <p>Cas gérés :</p>
     * <p>- Amplitudes et signes différents.</p>
     * <p>- Une des amplitudes est nulle.</p>
     * <p>- Les amplitudes sont de même signe.</p>
     *
     * <p>Le seuil du symbole est fixé à 85% de la valeur max (valeur arbitraire).</p>
     *
     * @param filtre Forme d'onde utilisée pour la transmission sur le canal.
     */
    public FiltreAdapte(FiltreMiseEnForme filtre) {
        nombreEchantillon = filtre.nbEch;
        symbole = Outils.convertToDoubleArray(filtre.donneeFiltre);

        seuilSymbole = Outils.getMaximumValue(MathArrays.convolve(symbole, symbole));
        seuilSymbole -= seuilSymbole * 0.25d;  // Seuil fixé à 85%

        sameAmplSign = Outils.checkSameSign(filtre.amplMin, filtre.amplMax);
        if (sameAmplSign) {
            if (filtre.amplMax > 0)
                amplRatio = (filtre.amplMax / filtre.amplMin);
            else
                amplRatio = (filtre.amplMin / filtre.amplMax) * -1f;
        }

        zeroingAmpl = (filtre.amplMax == 0f) || (filtre.amplMin == 0f);
        if (zeroingAmpl)
            positiveZeroing = (filtre.amplMax == 0f);
    }

    /**
     * Permet d'utiliser le signal pour l'appliquer avec le filtre adapté.
     *
     * @param signal Informations analogiques à convoluer.
     * @return Informations booléens estimées à partir de la convolution.
     */
    public Boolean[] appliquer(double[] signal) {
        donneesSignal = signal;
        return correlation();
    }

    /**
     * Permet de corréler les données analogiques entrante et la convolution avec le symbole de mise en forme.
     *
     * @return Valeurs booléens estimées à partir de la convolution avec le symbole.
     */
    private Boolean[] correlation() {
        double[] convolution = MathArrays.convolve(symbole, donneesSignal);
        return estimationSymbole(convolution);
    }

    /**
     * <p>Permet d'estimer à partir de la convolution (signal * symbole) la valeur booléen tous les n échantillons.</p>
     * <p>- Si l'une des amplitudes est nulles, on vérifie que l'on dépasse le seuil à 85% en prennant en compte le signe.</p>
     * <p>- Sinon si les amplitudes sont de même signes, on vérifie que l'on dépasse le seuil à 85% multiplié par le
     * ratio des amplitudes (prennant aussi en compte le signe).</p>
     * <p>- Sinon, nous vérifions uniquement si l'on dépasse 85% du seuil positif.</p>
     *
     * @param convolution Données de convolution entre le signal et le symbole unitaire du filtre de mise en forme.
     *
     * @return Estimation des valeurs booléens.
     */
    private Boolean[] estimationSymbole(double[] convolution) {
        int nombreSymbole = donneesSignal.length / nombreEchantillon;
        Boolean[] dataOut = new Boolean[nombreSymbole];

        for (int part = 0; part < nombreSymbole; part++) {
            int index = part * nombreEchantillon + nombreEchantillon;
            double niveauSymbole = convolution[index];

            if (zeroingAmpl) {  // > Cas où une amplitude = 0
                dataOut[part] = niveauSymbole >= seuilSymbole * (positiveZeroing ? -1 : 1);
            } else if (sameAmplSign) {  // > Cas amplitude de même signe
                dataOut[part] = niveauSymbole >= seuilSymbole * amplRatio;
            } else {  // > Cas amplitudes et signes différents
                dataOut[part] = niveauSymbole >= seuilSymbole;
            }
        }
        return dataOut;
    }
}
