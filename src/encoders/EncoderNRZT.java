package encoders;

import information.Information;

public class EncoderNRZT extends Encoder {

    private state current_state = state.GO_UP;
    private enum state {
        GO_UP, UP, GO_DOWN;
    }

    private int compteur = 0;
    private final float pas = 1f/nbEch;
    private final float tier1 = (1f/3f) * (float)nbEch;
    private final float tier2 = (2f/3f) * (float)nbEch;
    private final float tier3 = (float) nbEch;

    public EncoderNRZT(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
    }

    /**
     * PAS = 1/Nb<br/>
     * Y = 3 * (CPT * PAS)<br/>
     * CPT = 0 : 3 * (0 * PAS)<br/>
     * CPT = NbEch/3 : 3 * (NbEch/3 * PAS)<br/>
     */
    private float symbol(float datum) {
        float x, y;
        switch (current_state) {
            case GO_UP:
                x = compteur * pas;
                y = 3f * x + 0f;
                return datum * y;

            case UP:
                return datum;

            case GO_DOWN:
                x = compteur * pas;
                y = -3f * x + 3f;
                return datum * y;
        }
        return 0f;
    }

    /**
     * Symbole du filtre NonRetourZeroTrapezoïdale<br>
     * <img src="./doc-files/filtreNRZT.png" alt="FiltreNRZT"></img><br>
     *
     *  Chronogramme du filtre multiplié avec un enchainement 1-0<br>
     * <img src="./doc-files/filtreNRZT_chrono.png"></img>
     */
    public Information<Float> codage(Information<Boolean> data) {
        Information<Float> dataInBetween = echantilloneSymbole(data);
        Information<Float> dataOut = new Information<>();

        for (Float datum : dataInBetween) {
            // Mise à jour de la machine à état
            // Codage de la donnée
            switch (current_state) {
                case GO_UP:
                    if (compteur >= tier1)
                        current_state = state.UP;
                    break;
                case UP:
                    if (compteur >= tier2)
                        current_state = state.GO_DOWN;
                    break;

                case GO_DOWN:
                    if (compteur == 0)
                        current_state = state.GO_UP;
                    break;
            }

            // Ajout de la donnée
            dataOut.add(symbol(datum));

            // Mise à jour compteur
            compteur++;
            if (compteur >= nbEch)
                compteur = 0;
        }
        return dataOut;
    }
}

