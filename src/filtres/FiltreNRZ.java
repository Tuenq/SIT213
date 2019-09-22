package filtres;

import information.Information;

public class FiltreNRZ extends Filtre {
    /** Codage NRZ du signal
     * @param info
     * @param nbEch
     * @return
     */
    public Information<Float> codageNRZ(Information<Boolean> info, int nbEch)
    {
        for(Boolean symbole : info) {
            if((boolean) symbole)
                echantilloneSymbole(1f, 0, (float)1/nbEch, 0, 1);
            else
                echantilloneSymbole(0f, 0, (float)1/nbEch, 0, 1);
        }
        return informationCodee;
    }
    
    /**Decodage NRZ
     * 
     * @param info
     * @param nbEch
     * @return
     */
    public Information<Boolean> decodageNRZ(Information<Float> info, int nbEch) {
    decodageBinaire(info, nbEch);
    return infoATransmettre;
    }
}

