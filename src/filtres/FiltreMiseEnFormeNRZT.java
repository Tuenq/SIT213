package filtres;

import visualisations.VueCourbe;

/**
 * Implémentation du filtre de mise en forme <b>NRZT</b> ("non-return-to-zero with trapezoidal shape").
 */
public class FiltreMiseEnFormeNRZT extends FiltreMiseEnForme {

    public FiltreMiseEnFormeNRZT(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
        puissanceFiltre = 2/3;
    }

    /**
     * Symbole du filtre NonRetourZeroTrapezoïdale<br>
     * <img src="doc-files/filtreNRZT.png" alt="Filtre"><br>
     *
     * Chronogramme du filtre multiplié avec un enchainement binaire [10]<br>
     * <img src="doc-files/filtreNRZT_chrono.png" alt="Chronogramme">
     */
    void initialisationFiltre() {
        final float pas = 1f/nbEch;
        final float tier1 = (1f/3f) * (float)nbEch;
        final float tier2 = (2f/3f) * (float)nbEch;

        donneeFiltre = new float[nbEch];
        for (int ech = 0; ech < nbEch; ech++) {
            if (ech >= tier2) {  // PENTE DECROISSANTE
                donneeFiltre[ech] = -3f * ech * pas + 3f;
            } else if (ech >= tier1) {  // PENTE NULLE
                donneeFiltre[ech] = 1f;
            } else {  // PENTE CROISSANTE
                donneeFiltre[ech] = 3f * ech * pas + 0f;
            }
        }
    }

    public static void main(String[] args) {
        FiltreMiseEnForme visu = new FiltreMiseEnFormeNRZT(100, -10f, 10);
        new VueCourbe(visu.donneeFiltre, "Filtre NRZT");
    }
}

