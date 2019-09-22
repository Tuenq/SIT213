package filtres;

import information.Information;

public abstract class Filtre implements FiltreInterface {
	public Information<Float> informationCodee=new Information<Float>();
	public Float[] infoRecuperee;
	public Information <Boolean> infoATransmettre=new Information <Boolean>();
	float amplMax=1;
	float amplMin=0;
	  public void echantilloneSymbole(float echantillon, float incrementation, float pasEch, float debut, float limite){
	        float x=debut;
	        while(x<limite || x==1) {
	            informationCodee.add(echantillon);
	            x+=pasEch;
	            echantillon+=incrementation;
	        }
	    }
	  public Information<Boolean> decodageBinaire(Information<Float> info, int nbEch){
	        int nbSymbole = info.nbElements()/nbEch;
	        amplMax=info.amplMax;
	        amplMin=info.amplMin;
	        
	        for(int i=0;i<nbSymbole;i++) {
	            // On rÃ©cupÃ¨re l'Ã©chantillon situé à la position t + 1/3T : 
	        	//ce qui correspond au premier echantillon codé à 1 si le symbole est true
	            float valeurRetournee = Math.max(info.iemeElement((nbEch*i)+(nbEch/3)+1), info.iemeElement((nbEch*i)+(nbEch/3)));
	            infoRecuperee[i]=valeurRetournee;
	            if((valeurRetournee == amplMax) && (amplMax!=amplMin))
			        infoATransmettre.add(true);
			    else
			    	infoATransmettre.add(false);
	            }
	      
		    return infoATransmettre;
	 
	    }
}

