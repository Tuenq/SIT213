package filtres;

import information.Information;

public abstract class Filtre implements FiltreInterface {
	public Information<Float> informationCodee=new Information<Float>();
	public Float[] infoRecuperee;
	public Information <Boolean> infoATransmettre=new Information <Boolean>();
	float AmpMax=0;
	float AmpMin=0;
	  public void echantilloneSymbole(float echantillon, float incrementation, float pasEch, float debut, float limite){
	        float x=debut;
	        while(x<limite || x==1) {
	            informationCodee.add(echantillon);
	            x+=pasEch;
	            echantillon+=incrementation;
	        }
	    }
	    public Float[] RecupVal(Information<Float> info, int nbEch) {
	        int nbSymbole = info.nbElements()/nbEch;
	        for(int i=0;i<nbSymbole;i++) {
	            // On rÃ©cupÃ¨re l'Ã©chantillon situé à la position t + 1/3T : 
	        	//ce qui correspond au premier echantillon codé à 1 si le symbole est true
	            float valeurRetournee = Math.max(info.iemeElement((nbEch*i)+(nbEch/3)+1), info.iemeElement((nbEch*i)+(nbEch/3)));
	            infoRecuperee[i]=valeurRetournee;
	            if(valeurRetournee<AmpMin)
	            	AmpMin=valeurRetournee;
	            if(valeurRetournee>AmpMax)
	            	AmpMax=valeurRetournee;
	            }
	        return infoRecuperee;
	    }
	   public Information<Boolean> DecodageBinaire(Information<Float> info, int nbEch) {
	    Float [] valeurRetournee=RecupVal(info, nbEch);
	    for(int i=0; i<=valeurRetournee.length; i++) {
	    if(valeurRetournee[i] == AmpMax)
	        infoATransmettre.add(true);
	    else
	    	infoATransmettre.add(false);
	    }
	    return infoATransmettre;
	}
}

