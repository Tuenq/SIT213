package filtres;

import information.Information;
import visualisations.VueCourbe;

public class FiltreRZ extends Filtre {
    public FiltreRZ(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
    }

    /**
     * Symbole du filtre RetourZero<br>
     * <img src="./doc-files/filtreRZ.png" alt="FiltreRZ"></img><br>
     *
     *  Chronogramme du filtre multiplié avec un enchainement 1-0<br>
     * <img src="./doc-files/filtreRZ_chrono.png"></img>
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
        Filtre visu = new FiltreRZ(30, 1, 0);
        new VueCourbe(visu.donneeFiltre, "Filtre RZ");
    }
}

