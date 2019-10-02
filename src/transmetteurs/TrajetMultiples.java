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
		
		Information<Float> infoTrajetMultiple=informationRecue;
		
		for(int i=0; i<nombreTrajet; i++) {
			
			Information<Float> infoDecalee=new Information<Float>();
			
			for(int j=0; j<retard[i]; j++) {
				infoDecalee.add(0f);
				infoTrajetMultiple.add(0f);	
				
			}
			for(float datum : informationRecue)
				infoDecalee.add(datum);
			Information<Float> infoAmplifiee = infoDecalee.amplifierAttenuer(infoDecalee, amplitude[i]);
			infoTrajetMultiple=infoAmplifiee.additioner(infoTrajetMultiple, infoAmplifiee);
		SondeAnalogique sonde=new SondeAnalogique("test tajet " + i);
		sonde.recevoir(infoAmplifiee);
		
		System.out.println("tajet " + i+ " "+ infoAmplifiee);
		}
		informationEmise=infoTrajetMultiple;
		SondeAnalogique sonde=new SondeAnalogique("test tajetmumltiple ");
		sonde.recevoir(infoTrajetMultiple);
		return infoTrajetMultiple;
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

