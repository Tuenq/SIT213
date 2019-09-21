package transmetteurs;

import javax.xml.transform.Source;

import destinations.DestinationInterface;
import filtres.Filtre;
import filtres.FiltreNRZ;
import filtres.FiltreNRZT;
import filtres.FiltreRZ;
import information.Information;
import information.InformationNonConforme;
import sources.SourceFixe;
import visualisations.Sonde;
import visualisations.SondeAnalogique;

public class Emetteur extends Transmetteur<Boolean,Float> {

    public Information<Float> informationCodee=new Information<Float>();
   

    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
        //Par defaut on effectue un codage RZ du signal numérique
        FiltreRZ codeur=new FiltreRZ();
        informationEmise = codeur.CodageRZ(information, 30, 0f, 1f);
    }

    /**Reception d'un message avec un nombre d'échantillons par bit spécifié
     **/
    public void recevoir(Information<Boolean> information, int nbEch, String form) throws InformationNonConforme{
        informationRecue = information;
        if (form.contentEquals("RZ")) {
        	FiltreRZ codeur=new FiltreRZ();
            informationEmise = codeur.CodageRZ(information, nbEch, 0, 1);
        }
        if (form.contentEquals("NRZ")) {
        	FiltreNRZ codeur=new FiltreNRZ();
            informationEmise = codeur.CodageNRZ(information, nbEch);
        }
        if (form.contentEquals("NRZT")) {
        	FiltreNRZT codeur=new FiltreNRZT();
            informationEmise = codeur.CodageNRZT(information, nbEch);
        }
    }
    /**Reception d'un message avec un nombre d'échantillons par bit spécifié,
     * ainsi que les amplitudes max et min
     **/
    public void recevoir(Information<Boolean> information, int nbEch, float amplMin, float amplMax, String form) throws InformationNonConforme{
        informationRecue = information;
        FiltreRZ codeur=new FiltreRZ();
        informationEmise = codeur.CodageRZ(information, nbEch, amplMin, amplMax);
    }

    @Override
    public void emettre() throws InformationNonConforme {
        //informationEmise
        //Pour chaque destination connectÃ©e on envoie les informations Ã  Ã©mettre
        for (DestinationInterface<Float> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }

    }



    
    public static void main(String[] args) throws InformationNonConforme {
   	 Information<Boolean> expectedMessage = Information.stringToBoolean("100110101011");
   	 FiltreNRZT testfiltre=new FiltreNRZT();
   	 System.out.println(expectedMessage);
   	 Information<Float> inf=testfiltre.CodageNRZT(expectedMessage, 30);
   	 System.out.println(inf);
   	 SondeAnalogique stest=new SondeAnalogique("CODAGE NRZT");
   	 stest.recevoir(inf);
   	 

   }
}
