package filtres;

import visualisations.VueCourbe;

public class FiltreMiseEnFormeRZ extends FiltreMiseEnForme {
    public FiltreMiseEnFormeRZ(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
        puissanceFiltre = 1/3;
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

    public static void main(String[] args) throws SymboleNulException {
        FiltreMiseEnForme visu = new FiltreMiseEnFormeRZ(100, -10f, 10f);
        new VueCourbe(visu.donneeFiltre, "Filtre RZ");

        float[] symbole_0 = visu.genererSymbole(false);
        new VueCourbe(symbole_0, "Filtre RZ - Symbole 0");

        float[] symbole_1 = visu.genererSymbole(true);
        new VueCourbe(symbole_1, "Filtre RZ - Symbole 1");
    }
}

