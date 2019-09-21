package filtres;

import information.Information;

public abstract class Filtre implements FiltreInterface {
	Information<Float> informationCodee=new Information<Float>();
	
	  public void echantilloneSymbole(float echantillon, float incrementation, float pasEch, float debut, float limite){
	        float x=debut;
	        while(x<limite || x==1) {
	            informationCodee.add(echantillon);
	            x+=pasEch;
	            echantillon+=incrementation;
	        }
	    }
}
