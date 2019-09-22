package encoders;

import information.Information;

public class EncoderRZ extends Encoder {
	
    /** codage RZ du signal
     * @param info
     * @param nbEch
     * @param amplMin
     * @param amplMax
     * @return
     */
    public Information<Float> codageRZ(Information<Boolean> info, int nbEch, float amplMin, float amplMax) {
 
    	float pasEch=(float)1/nbEch;
        System.out.println("Ok" + pasEch);
        for(Boolean symbole : info) {
            if((boolean) symbole) {
                echantilloneSymbole(amplMin, 0f, pasEch, 0f, (float)1/3);
                echantilloneSymbole(amplMax, 0, pasEch, (float)1/3, (float)2/3);
                echantilloneSymbole(amplMin, 0, pasEch, (float)2/3, (float)1);
            }
            else
                echantilloneSymbole(amplMin, 0,pasEch, 0, 1);
        }
        
		return informationCodee;
    }
    
    /**Decodage RZ
     * 
     * @param info
     * @param nbEch
     * @return
     */
    public Information<Boolean> decodageRZ(Information<Float> info, int nbEch) {
    decodageBinaire(info, nbEch);
    return infoATransmettre;
    }
  

}

