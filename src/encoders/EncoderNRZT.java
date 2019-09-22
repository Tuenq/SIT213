package encoders;

import information.Information;

public class EncoderNRZT extends Encoder {

    /** Codage NRZT du signal
     * @param info
     * @param nbEch
     * @return
     */
    public Information<Float> codageNRZT(Information<Boolean> info, int nbEch) {

        for(Boolean symbole : info) {
            if((boolean) symbole) {
                echantilloneSymbole(0f, (float)3/nbEch, (float)1/nbEch, 0, (float)1/3);
                echantilloneSymbole(1f, 0, (float)1/nbEch, (float)1/3, (float)2/3);
                echantilloneSymbole(1f, (float)-3/nbEch, (float)1/nbEch, (float)2/3, 1);
            }
            else
                echantilloneSymbole(0f, 0, (float)1/nbEch, 0, 1);
        }
        return informationCodee;
    }
    
    /**Decodage NRZT
     * 
     * @param info
     * @param nbEch
     * @return
     */
    public Information<Boolean> decodageNRZT(Information<Float> info, int nbEch) {
    decodageBinaire(info, nbEch);
    return infoATransmettre;
    }
}

