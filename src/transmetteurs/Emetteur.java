package transmetteurs;


import destinations.DestinationInterface;
import encoders.EncoderNRZ;
import encoders.EncoderNRZT;
import encoders.EncoderRZ;
import information.Information;
import information.InformationNonConforme;


public class Emetteur extends Transmetteur<Boolean,Float> {

    public Information<Float> informationCodee=new Information<Float>();
    String forme;
    int nbEch;
    float amplMin;
    float amplMax;
    
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
        forme=informationRecue.forme;
        nbEch=informationRecue.nbEch;
        amplMin=informationRecue.amplMin;
        amplMax=informationRecue.amplMax;

        if (forme.equals("NRZ")) {
        	EncoderNRZ codeur=new EncoderNRZ();
            informationEmise = codeur.codageNRZ(information, nbEch);
        }
        else if (forme.equals("NRZT")) {
        	EncoderNRZT codeur=new EncoderNRZT();
            informationEmise = codeur.codageNRZT(information, nbEch);
        }
        else { //Par defaut on effectue un codage RZ du signal numérique
        EncoderRZ codeur=new EncoderRZ();
        informationEmise = codeur.codageRZ(information, nbEch, amplMin, amplMax);
        emettre();
        }
    }

    /**
     * Pour chaque destination connectée, on transmet les informations reçues
     * @throws InformationNonConforme Cas d'erreur remonté par l'information
     */
    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Float> destination : destinationsConnectees) {
        	destination.recevoir(informationEmise);
        	
        }

    }
}
