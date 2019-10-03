		SondeAnalogique sonde=new SondeAnalogique("testinfoFloat ");
package transmetteurs;

import destinations.DestinationInterface;
import filtres.FiltreNRZT;
import information.Information;
import information.InformationNonConforme;
import visualisations.SondeAnalogique;

public class TrajetMultiples extends Transmetteur<Float,Float>{
	
	Information<Float> dataout=new Information<Float>();
	int nombreTrajet=0;
	float[] amplitude;
	int[] retard;

	public TrajetMultiples(float[] amplitude, int[] retard) {
		nombreTrajet=amplitude.length;
		this.amplitude=amplitude;
		this.retard=retard;
	}

	@Override
	public void recevoir(Information<Float> information) throws InformationNonConforme {
	    informationRecue = information;
       // genererSignalTrajetMultiple();
        emettre();
	}

	@Override
	public void emettre() throws InformationNonConforme {
		   for (DestinationInterface<Float> destination : destinationsConnectees) {
               destination.recevoir(informationEmise);
       }
		
	}
	
	private Information<Float> genererSignalTrajetMultiple(Information<Float> info) throws InformationNonConforme{
		informationRecue=info;
		
		Information<Float> infoTrajetMultiple=new Information<Float>();
		float[] signalTrajetDirect=getArray(informationRecue);
		float[] signalPrecedent = signalTrajetDirect;
		
		for(int i=0; i<nombreTrajet; i++) {
	
			float[] signalGenere = new float[signalTrajetDirect.length + retard[i]];
			float[] signalAttenueRetarde = new float[signalGenere.length];
			
			for(int j=0; j<retard[i];j++) 
				signalAttenueRetarde[j]=0f;
			
			for(int j=retard[i]; j<signalTrajetDirect.length + retard[i]; j++) 
				signalAttenueRetarde[j]=(amplitude[i]*signalTrajetDirect[j-retard[i]]);
			
			for(int j=0; j<signalPrecedent.length; j++) 
				signalGenere[j] = signalPrecedent[j] + signalAttenueRetarde[j];
				
			for(int j=signalPrecedent.length; j<signalTrajetDirect.length + retard[i];j++) 
				signalGenere[j] = signalAttenueRetarde[j-retard[i]];
			
			signalPrecedent=signalGenere;
			
			//test
			infoTrajetMultiple=tabtoList(signalAttenueRetarde);
			SondeAnalogique sonde=new SondeAnalogique("test tajet " + i);
			sonde.recevoir(infoTrajetMultiple);

		}
	
		informationEmise=tabtoList(signalPrecedent);
		//test
		SondeAnalogique sonde=new SondeAnalogique("test tajetMultiple ");
		sonde.recevoir(informationEmise);

		return informationEmise;
	}
	
	//FIX ME (a ajouter dans info)
	public Information<Float> tabtoList(float[] tab){
	Information<Float> info = new Information<Float>();
	for(int i = 0; i < tab.length; i++)
		info.add(tab[i]);
	return info;
	}
	
	public float[] getArray(Information<Float> info) {
		float[] array = new float[info.nbElements()];
		int i=0;
		for(float datum: info)
			array[i++]=datum;
		return array;
	}
	
	public static void main(String[] args) throws InformationNonConforme {
		float[] ampl= {0.5f, 0.2f, 0.9f};
		int[] retard= {10, 15, 18};
	
		//Information <Float> testinfoFloat = testfiltre.appliquerMiseEnForme(testinfo);
		Information <Float> testinfoFloat=new Information<Float>();
		for(int j=0; j<4; j++) {
		for(int i=0; i<5; i++) {
			testinfoFloat.add(1f);
		}
		for(int i=0; i<5; i++) {
			testinfoFloat.add(0.2f);
		}}
		SondeAnalogique sonde=new SondeAnalogique("testinfoFloat ");
		sonde.recevoir(testinfoFloat);
		TrajetMultiples trajetTest=new TrajetMultiples(ampl, retard);
		testinfoFloat=trajetTest.genererSignalTrajetMultiple(testinfoFloat);	
		}
}

