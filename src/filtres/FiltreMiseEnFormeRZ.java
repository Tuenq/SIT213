package filtres;

import visualisations.VueCourbe;

/**
 * Implémentation du filtre de mise en forme <b>RZ</b> ("return-to-zero").
 */
public class FiltreMiseEnFormeRZ extends FiltreMiseEnForme {

    public FiltreMiseEnFormeRZ(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
    }

    /**
     * Symbole du filtre RetourZero<br>
     * <img src="./doc-files/filtreRZ.png" alt="Filtre"><br>
     *
     *  Chronogramme du filtre multiplié avec un enchainement 1-0<br>
     * <img src="./doc-files/filtreRZ_chrono.png" alt="Chronogramme">
     */
    @Override
    void initialisationFiltre() {
        final float tier1 = (1f/3f) * (float)nbEch;
        final float tier2 = (2f/3f) * (float)nbEch;

        donneeFiltre = new float[nbEch];
        for (int ech = 0; ech < nbEch; ech++) {
            if (ech >= tier2 || ech < tier1) {  // DOWN
                donneeFiltre[ech] = 0f;
            } else {  // UP
                donneeFiltre[ech] = 1f;
            }
        }
    }

    public static void main(String[] args) {
        FiltreMiseEnForme visu = new FiltreMiseEnFormeRZ(100, -10f, 10f);
        new VueCourbe(visu.donneeFiltre, "Filtre RZ");
    }
}

