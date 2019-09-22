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
     * PAS = 1/Nb
     * Y = 3 * (CPT * PAS)
     * CPT = 0 : 3 * (0 * PAS)
     * CPT = NbEch/3 : 3 * (NbEch/3 * PAS)
     */
    private float symbol(int cpt, boolean reverse) {
        float pente = reverse ? -3f : 3f;
        return pente * (cpt * pas);
    }

    public Information<Float> codage(Information<Boolean> data) {
        Information<Float> dataInBetween = echantilloneSymbole(data);
        Information<Float> dataOut = new Information<>();

        for (Float datum : dataInBetween) {
            float out = 0;

            // Mise à jour compteur
            compteur++;
            if (compteur >= nbEch)
                compteur = 0;

            // Mise à jour de la machine à état
            // Codage de la donnée
            switch (current_state) {
                case GO_UP:
                    if (compteur >= tier1)
                        current_state = state.UP;
                    out = datum * symbol(compteur, false);
                    break;
                case UP:
                    if (compteur >= tier2)
                        current_state = state.GO_DOWN;
                    out = datum * 1;
                    break;

                case GO_DOWN:
                    if (compteur >= tier3)
                        current_state = state.GO_UP;
                    out = datum * symbol(compteur - (int)tier2, true);
                    break;
            }
            dataOut.add(out);
        }
        return dataOut;
    }
}

