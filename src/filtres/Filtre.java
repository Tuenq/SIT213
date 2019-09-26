package filtres;

public abstract class Filtre {

	public enum encoders {
		RZ, NRZ, NRZT;
	}

	public int nbEch;
	public float amplMax;
	public float amplMin;
	float[] donneeFiltre;


	Filtre(int nbEch, float amplMin, float amplMax) {
		this.nbEch = nbEch;
		this.amplMin = amplMin;
		this.amplMax = amplMax;

		initialisationFiltre();
	}

	abstract void initialisationFiltre();

	public void appliquerMiseEnForme(Float[] data) {
		for (int ech = 0; ech < data.length; ech++) {
			data[ech] *= donneeFiltre[ech % nbEch];
		}
	}
}

