package filtres;

import communs.Outils;
import org.apache.commons.math3.util.MathArrays;

public class FiltreAdapte {
    private int nombreEchantillon;
    private double[] donneesSignal;

    private boolean zeroTrue = false, zeroFalse = false;
    private double[] symbole_true;
    private double[] symbole_false;

    public FiltreAdapte(FiltreMiseEnForme filtre) {
        nombreEchantillon = filtre.nbEch;
        try {
            symbole_true = Outils.convertToDoubleArray(filtre.genererSymbole(true));
        } catch (SymboleNulException e) {
            zeroTrue = true;
        }
        try {
            symbole_false = Outils.convertToDoubleArray(filtre.genererSymbole(false));
        } catch (SymboleNulException e) {
            zeroFalse = true;
        }
    }

    public Boolean[] appliquer(double[] signal) {
        donneesSignal = signal;
        return correlation();
    }

    private Boolean[] correlation() {
        double[] convolution_true = new double[0];
        if (!zeroTrue) {
            convolution_true = MathArrays.convolve(symbole_true, donneesSignal);
//            new VueCourbe(Outils.convertToFloatArray(convolution_true), "Convolution symbole TRUE");
        }

        double[] convolution_false = new double[0];
        if (!zeroFalse) {
            convolution_false = MathArrays.convolve(symbole_false, donneesSignal);
//            new VueCourbe(Outils.convertToFloatArray(convolution_false), "Convolution symbole FALSE");
        }

        if (zeroTrue)
            convolution_true = new double[]{ Outils.getMaximumValue(convolution_false) };
        else if (zeroFalse)
            convolution_false = new double[]{ Outils.getMaximumValue(convolution_true) };

        return estimationSymbole(convolution_true, convolution_false);
    }

    private Boolean[] estimationSymbole(double[] convTrue, double[] convFalse) {
        int nombreSymbole = donneesSignal.length / nombreEchantillon;
        Boolean[] dataOut = new Boolean[nombreSymbole];

        for (int part = 0; part < nombreSymbole; part++) {
            int index = part * nombreEchantillon + nombreEchantillon;

            double niveauSymboleTrue = 0d;
            double niveauSymboleFalse = 0d;

            if (zeroTrue)  // Cas où la limite haute est centrée
                niveauSymboleTrue = convTrue[0];
            else
                niveauSymboleTrue = convTrue[index];

            if (zeroFalse)  // Cas où la limite basse est centrée
                niveauSymboleFalse = convFalse[0];
            else
                niveauSymboleFalse = convFalse[index];

            if (zeroTrue) {  // Niveau inférieur à 50% du minimum
                dataOut[part] = niveauSymboleFalse < niveauSymboleTrue / 2d;
            } else if (zeroFalse) {  // Niveau supérieur à 50% du maximum
                dataOut[part] = niveauSymboleTrue > niveauSymboleFalse / 2d;
            } else {
                dataOut[part] = niveauSymboleTrue > niveauSymboleFalse;
            }
        }

        return dataOut;
    }
}
