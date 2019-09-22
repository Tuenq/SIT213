package encoders;

import information.Information;

public class EncoderNRZ extends Encoder {

    public EncoderNRZ(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
    }

    /**
     * Symbole du filtre NonRetourZero<br>
     * <img src="./doc-files/filtreNRZ.png" alt="FiltreNRZ"></img><br>
     *
     *  Chronogramme du filtre multiplié avec un enchainement 1-0<br>
     * <img src="./doc-files/filtreNRZ_chrono.png"></img>
     */
    public Information<Float> codage(Information<Boolean> data) {
        Information<Float> dataOut = echantilloneSymbole(data);
        return dataOut;
    }
}

