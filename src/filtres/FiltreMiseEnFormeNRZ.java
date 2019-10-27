package filtres;

import visualisations.VueCourbe;

import java.util.Arrays;

/**
 * Implémentation du filtre de mise en forme <b>NRZ</b> ("non-return-to-zero").
 */
public class FiltreMiseEnFormeNRZ extends FiltreMiseEnForme {

    public FiltreMiseEnFormeNRZ(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
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

    public static void main(String[] args) {
        FiltreMiseEnForme visu = new FiltreMiseEnFormeNRZ(100, -10f, 10f);
        new VueCourbe(visu.donneeFiltre, "Filtre NRZ");
    }
}

