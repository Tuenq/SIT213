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
    public Information<Boolean> Decodage(Information<Float> info, int nbEch) {
        int nbSymbole = info.nbElements()/nbEch;
        Information<Boolean> InfoATransmettre = new Information<Boolean>();
        float amplMax = info.iemeElement(0);
        for(int i=0;i<nbSymbole;i++) {
            // Le premier caractère est l'amplitude max envoyée (d'où le +1)
            // On récupère l'échantillon du centre du symbole :
            float valeurRetournee = info.iemeElement(nbEch*i+nbEch-1)+1;
            if(valeurRetournee == amplMax){
                InfoATransmettre.add(true);
            }
            else
                    InfoATransmettre.add(false);
        }
        return InfoATransmettre;
    }
}

