package encoders;

import information.Information;

public class EncoderNRZ extends Encoder {

    public EncoderNRZ(int nbEch, float amplMin, float amplMax) {
        super(nbEch, amplMin, amplMax);
    }

    public Information<Float> codage(Information<Boolean> data) {
        Information<Float> dataOut = echantilloneSymbole(data);
        return dataOut;
    }
}

