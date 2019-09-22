package transmetteurs;


import destinations.DestinationInterface;
import encoders.Encoder.encoders;
import encoders.EncoderNRZ;
import encoders.EncoderNRZT;
import encoders.EncoderRZ;
import information.Information;
import information.InformationNonConforme;


public class Emetteur extends Transmetteur<Boolean,Float> {
	public Information<Float> informationCodee=new Information<Float>();
	encoders forme;
    int nbEch;
    float amplMin;
    float amplMax;
    
	public Emetteur(encoders formeOnde, int nbEch, float amplMin, float amplMax) {
        this.forme=formeOnde;
        this.nbEch=nbEch;
        this.amplMin=amplMin;
        this.amplMax=amplMax;
	}
    
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
 
        switch(forme){
        case NRZ:
        	EncoderNRZ codeurNRZ=new EncoderNRZ();
            informationEmise = codeurNRZ.codageNRZ(information, nbEch);
            break;
        case NRZT:
        	EncoderNRZT codeurNRZT=new EncoderNRZT();
            informationEmise = codeurNRZT.codageNRZT(information, nbEch);
            break;
        default:
        EncoderRZ codeurRZ=new EncoderRZ();
        informationEmise = codeurRZ.codageRZ(information, nbEch, amplMin, amplMax);
        break;
        }
        emettre();
        
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
