package filtres;

import visualisations.VueCourbe;

import java.util.Arrays;

public class FiltreMiseEnFormeNRZ extends FiltreMiseEnForme {

    public FiltreMiseEnFormeNRZ(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
        puissanceFiltre = 1;
    }

    /**
     * Symbole du filtre NonRetourZero<br>
     * <img src="doc-files/filtreNRZ.png" alt="Filtre"><br>
     *
     * Chronogramme du filtre multiplié avec un enchainement 1-0<br>
     * <img src="doc-files/filtreNRZ_chrono.png" alt="Chronogramme">
     */
    @Override
    void initialisationFiltre() {
        donneeFiltre = new float[nbEch];
        Arrays.fill(donneeFiltre, 1f);
    }

    public static void main(String[] args) throws SymboleNulException {
        FiltreMiseEnForme visu = new FiltreMiseEnFormeNRZ(100, -10f, 10f);
        new VueCourbe(visu.donneeFiltre, "Filtre NRZ");

        float[] symbole_0 = visu.genererSymbole(false);
        new VueCourbe(symbole_0, "Filtre RZ - Symbole 0");

        float[] symbole_1 = visu.genererSymbole(true);
        new VueCourbe(symbole_1, "Filtre RZ - Symbole 1");
    }
}

