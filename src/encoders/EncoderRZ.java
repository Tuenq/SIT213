package encoders;

import information.Information;

public class EncoderRZ extends Encoder {
    public EncoderRZ(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
    }

    private state current_state = state.DOWN;
    private enum state {
        DOWN, UP;
    }

    private int compteur = 0;
    private final float tier1 = (1f/3f) * (float)nbEch;
    private final float tier2 = (2f/3f) * (float)nbEch;

    /**
     * Symbole du filtre RetourZero<br>
     * <img src="./doc-files/filtreRZ.png" alt="FiltreRZ"></img><br>
     *
     *  Chronogramme du filtre multiplié avec un enchainement 1-0<br>
     * <img src="./doc-files/filtreRZ_chrono.png"></img>
     */
    @Override
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
                case DOWN:
                    if (compteur >= tier1 && compteur <= tier2)
                        current_state = state.UP;
                    out = datum * 0f;
                    break;
                case UP:
                    if (compteur >= tier2)
                        current_state = state.DOWN;
                    out = datum * 1f;
                    break;
            }
            dataOut.add(out);
        }
        return dataOut;
    }
}

