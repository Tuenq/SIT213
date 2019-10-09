package convertisseurs;

import communs.Outils;
import destinations.DestinationInterface;
import filtres.Filtre;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;
import visualisations.SondeAnalogique;

public class Recepteur extends Transmetteur<Float,Boolean> {

    private int nombreEchantillon;
    private float amplMinBruitee = 10;
    private float amplMaxBruitee = -10;
    Float [] attenuationCanal;
    int[] retardCanal;
    int nbTrajetIndirect = 0;
    
	public Recepteur(int nombreEchantillon) {
	    this.nombreEchantillon = nombreEchantillon;
	}
   public Recepteur(int nombreEchantillon, Float [] attenuationCanal, int [] retardCanal){
	    this.nombreEchantillon = nombreEchantillon;
	    this.attenuationCanal = attenuationCanal;
	    this.retardCanal = retardCanal;
	    nbTrajetIndirect = retardCanal.length;
    }

    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
	    informationRecue = information;
	    if(nbTrajetIndirect != 0) 
	    	filtreTrajetMultiple();
        calculAmplBruite();
        decodage();
        emettre();
    }
 

    private void calculAmplBruite () {
  
        for (Float datum: informationRecue){
            if (datum > amplMaxBruitee)
                amplMaxBruitee = datum;
            else if (datum < amplMinBruitee)
                amplMinBruitee = datum;
        }
    }

    private void decodage(){
	    informationEmise = new Information<>();
        final int nbEch = nombreEchantillon;
        int compteur = 0;
        float sum = 0;

        Float[] ampl = Outils.calculAmplitude(informationRecue);
        amplMinBruitee = ampl[Outils.amplIndex.MIN.ordinal()];
        amplMaxBruitee = ampl[Outils.amplIndex.MAX.ordinal()];

        float tier1 = (1/3f) * nbEch;
        float tier2 = (2/3f) * nbEch;

        for (Float datum: informationRecue) {
            if (compteur >= tier1 && compteur < tier2)
                sum += datum;

            if (++compteur == nbEch) {
                float mean = sum/tier1;
                boolean EstimatedValue = Outils.booleanDistance(mean, amplMinBruitee, amplMaxBruitee);
                informationEmise.add(EstimatedValue);  // FIXME: PREALLOCATE ARRAY
                sum = compteur = 0;
            }
        }
    }
    
    public void filtreTrajetMultiple() {
    	// recupérer le nbElement d'origine
    	int nbTrajetIndirect = retardCanal.length;
    	int maxRetard = (int) retardCanal[nbTrajetIndirect - 1];
    	Float[] infomelangee = informationRecue.getArray();
    	Float[] infoDetectee = new Float[infomelangee.length - maxRetard];

    	int debut = 0;
    	int fin = retardCanal[0];
    	
		for (int j = debut; j<fin; j++)
			infoDetectee[j] = infomelangee[j];
		
		debut = fin;
		fin = (nbTrajetIndirect>1)? retardCanal[0] : infoDetectee.length; 
		for (int j = debut; j<fin; j++)
			infoDetectee[j] = infomelangee[j] - (attenuationCanal[0] * infoDetectee[j-retardCanal[0]]);
		
		debut = fin;
		fin = (nbTrajetIndirect>2)? retardCanal[1] : infoDetectee.length; 
		for (int j = debut; j<fin; j++)
			infoDetectee[j] = infomelangee[j] - (attenuationCanal[0] * infoDetectee[j-retardCanal[0]]) - (attenuationCanal[1] * infoDetectee[j-retardCanal[1]]);
		
		//TODO : renvoyer une donnée au lieu de modifier informationRecue
		informationRecue = new Information<Float>(infoDetectee); 
	
    }

    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Boolean> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }
    }
}


