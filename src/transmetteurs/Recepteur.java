package transmetteurs;

import destinations.DestinationInterface;
import encoders.Encoder.encoders;
import encoders.EncoderNRZ;
import encoders.EncoderNRZT;
import encoders.EncoderRZ;
import information.Information;
import information.InformationNonConforme;

public class Recepteur extends Transmetteur<Float,Boolean> {

    public Information<Float> informationCodee;
    encoders forme;
    int nbEch;
    float amplMin;
    float amplMax;
    
	public Recepteur(encoders forme, int nbEch, float amplMin, float amplMax) {
        this.forme=forme;
        this.nbEch=nbEch;
        this.amplMin=amplMin;
        this.amplMax=amplMax;
	}
    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        informationRecue = information;
        
        switch(forme) {
        case NRZ:
        	EncoderNRZ decodeurNRZ=new EncoderNRZ();
            informationEmise = decodeurNRZ.decodageNRZ(information, nbEch);
            break;
        case NRZT:
        	EncoderNRZT decodeurNRZT=new EncoderNRZT();
            informationEmise = decodeurNRZT.decodageNRZT(information, nbEch);
            break;
        default:
        	EncoderRZ decodeurRZ=new EncoderRZ();
            informationEmise = decodeurRZ.decodageRZ(information, nbEch, amplMin, amplMax);
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

}


