package transmetteurs;

import destinations.DestinationInterface;
import filtres.FiltreInterface;
import information.Information;
import information.InformationNonConforme;

public class Recepteur extends Transmetteur<Float,Boolean> {

    public Information<Float> informationCodee;

    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        informationRecue = information;
        //Par defaut on effectue un codage RZ du signal numérique
    }

    /**Reception d'un message avec un nombre d'échantillons par bit spécifié
     **/
    public void recevoir(Information<Float> information, int nbEch, String form) throws InformationNonConforme{
        informationRecue = information;

    }

    /**Reception d'un message avec un nombre d'échantillons par bit spécifié,
     * ainsi que les amplitudes max et min
     **/
    public void recevoir(Information<Float> information, int nbEch, float amplMin, float amplMax, String form) throws InformationNonConforme{
        informationRecue = information;
    }

    @Override
    public void emettre() throws InformationNonConforme {
        //informationEmise
        //Pour chaque destination connectÃ©e on envoie les informations Ã  Ã©mettre
        for (DestinationInterface<Boolean> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }

    }

    /** Decodage multiforme du signal
     * @param info
     * @param nbEch
     * @return
     */
    public Information<Float> CodageNRZT(Information<Float> info, int nbEch) {
        int nbSymbole = info.nbElements()/nbEch;
        int periode_echantillons= 0;
        for(int i=0;i<nbSymbole;i++) {

            periode_echantillons+=i*nbEch
        }

                float x=0;
                for (int i=nbEch/3; i<(nbEch*2)/3;i++) {
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
    public void DesechantilloneSymbole(float echantillon, float pasEch, float debut, float limite){
        float x=debut;
        while(x<limite || x==1) {
            informationCodee.add(echantillon);
            x+=pasEch;
        }
    }
}

