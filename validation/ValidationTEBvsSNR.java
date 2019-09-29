import communs.Outils;
import information.Information;
import visualisations.*;

public class ValidationTEBvsSNR {
    private String[] args;

    private float pas;
    private float snrMax;
    private Information<Float> data;

    /**
     * @param pas utilisé pour incrémenter le SNR dans l'intervale [0;snrMax]
     * @param snrMax utilisé pour spécifier l'intervale [0;snrMax]
     */
    private ValidationTEBvsSNR(float pas, float snrMax) {
        this.pas = pas;
        this.snrMax = snrMax;
    }

    /**
     * Récolte les TEB, avec le pas spécifié, jusqu'au TEB max spécifié
     * @param args Paramètre du simulateur (Sans spécifier le SNR !)
     */
    private void sampling(String[] args) throws Exception {
        data = new Information<>();
        this.args = args;

        for (float snr = 0.0f; snr < snrMax; snr += pas) {
            String[] snrSimu = {"-snr", Float.toString(snr)};
            String[] argsSimu = Outils.concatenate(args, snrSimu);

            Simulateur simu = new Simulateur(argsSimu);
            simu.execute();
            float teb = simu.calculTauxErreurBinaire();

            data.add(teb);

//            System.out.println("SNR - TEB : " + snr + " - " + teb);
        }
    }

    private void display() {
        String titre = "TEB vs SNR " + "| pas : " + pas  + " | intervale : [0.0 ; " + snrMax + "] | \"" + String.join(" ", args) + "\"";
        Sonde dataDisplay = new SondeAnalogique(titre);
        dataDisplay.recevoir(data);
    }

    public static void main(String[] args) throws Exception {
        ValidationTEBvsSNR valid = new ValidationTEBvsSNR(0.01f, 10f);
        valid.sampling(args);
        valid.display();
    }
}
