package transmetteurs;

import destinations.DestinationInterface;
import encoders.EncoderNRZ;
import encoders.EncoderNRZT;
import encoders.EncoderRZ;
import information.Information;
import information.InformationNonConforme;

public class Recepteur extends Transmetteur<Float,Boolean> {

    public Information<Float> informationCodee;
    String forme;
    int nbEch;
    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        informationRecue = information;
        forme=informationRecue.forme;
        nbEch=informationRecue.nbEch;
        
        if (forme.contentEquals("NRZ")) {
        	EncoderNRZ decodeur=new EncoderNRZ();
            informationEmise = decodeur.decodageNRZ(information, nbEch);
        }
        else if (forme.contentEquals("NRZT")) {
        	EncoderNRZT decodeur=new EncoderNRZT();
            informationEmise = decodeur.decodageNRZT(information, nbEch);
        }
      
        else{
        	EncoderRZ decodeur=new EncoderRZ();
            informationEmise = decodeur.decodageRZ(information, nbEch);
        }
        
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

