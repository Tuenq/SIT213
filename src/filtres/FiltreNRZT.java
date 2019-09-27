package filtres;

import visualisations.VueCourbe;

public class FiltreNRZT extends Filtre {


    public FiltreNRZT(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
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
        Filtre visu = new FiltreNRZT(30, 1, 0);
        new VueCourbe(visu.donneeFiltre, "Filtre NRZT");
    }
}

