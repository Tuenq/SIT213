
package transmetteurs;

        import destinations.DestinationInterface;
        import information.Information;
        import information.InformationNonConforme;

public class Emetteur extends Transmetteur<Boolean,Float> {

    public Information<Float> informationCodee;

    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
        //Par defaut on effectue un codage RZ du signal numérique
        informationRecue = information;
    }

    /**Reception d'un message avec un nombre d'échantillons par bit spécifié
     **/
    public void recevoir(Information<Boolean> information, int nbEch, String form) throws InformationNonConforme{
        informationRecue = information;

    }

    /**Reception d'un message avec un nombre d'échantillons par bit spécifié,
     * ainsi que les amplitudes max et min
     **/
    public void recevoir(Information<Boolean> information, int nbEch, float amplMin, float amplMax, String form) throws InformationNonConforme{
        informationRecue = information;
    }

    @Override
    public void emettre() throws InformationNonConforme {
        //informationEmise
        //Pour chaque destination connectÃ©e on envoie les informations Ã  Ã©mettre
        for (DestinationInterface<Float> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }

    }
    /** Codage RZ du signal
     * @param info
     * @param nbEch
     * @param amplMin
     * @param amplMax
     * @return
     */
    public Information<Float> CodageRZ(Information<Boolean> info, int nbEch, float amplMin, float amplMax) {
        float pasEch=1/nbEch;
        for(Boolean symbole : info) {
            if(symbole==true) {
                echantilloneSymbole(amplMin, pasEch, 0, 1/3);
                echantilloneSymbole(amplMax, pasEch, 1/3, 2/3);
                echantilloneSymbole(amplMin, pasEch, 2/3, 1);
            }
            else
                echantilloneSymbole(amplMin, pasEch, 0, 1);
        }
        return informationCodee;
    }

    /** Codage NRZ du signal
     * @param info
     * @param nbEch
     * @return
     */
    public Information<Float> CodageNRZ(Information<Boolean> info, int nbEch)
    {
        for(Boolean symbole : info) {
            if(symbole==true)
                echantilloneSymbole(1f, nbEch);
            else
                echantilloneSymbole(0f, nbEch);
        }
        return informationCodee;
    }

    /** Codage NRZT du signal
     * @param info
     * @param nbEch
     * @return
     */
    public Information<Float> CodageNRZT(Information<Boolean> info, int nbEch) {

        for(Boolean symbole : info) {
            if(symbole==true) {
                float x=0;
                for (int i=0; i<nbEch/3;i++) {
                    echantilloneSymbole(x, 1);
                    x+=3/nbEch;
                }
                echantilloneSymbole(1f, nbEch/3);
                x=1;
                for (int i=0; i<nbEch/3;i++) {
                    echantilloneSymbole(x, 1);
                    x-=3/nbEch;
                }
            }
            else
                echantilloneSymbole(0f, nbEch);
        }
        return informationCodee;
    }

    /** Ajoute les differents echantillons d'un symbole dans l'information filtrée
     * @param echantillon
     * @param nbEch
     */
    public void echantilloneSymbole(float echantillon, float pasEch, float debut, float limite){
        float x=debut;
        while(x<limite || x==1) {
            informationCodee.add(echantillon);
            x+=pasEch;
        }
    }
}

