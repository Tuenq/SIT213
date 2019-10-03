import communs.Outils;
import information.Information;
import visualisations.*;

public class generationValeursTEB {
    private String[] args;

    private float pas;
    private float snrMin;
    private float snrMax;
    private Information<Float> data;

    /**
     * @param pas utilisé pour incrémenter le SNR dans l'intervale [snrMin;snrMax]
     * @param snrMin utilisé pour spécifier l'intervale [snrMin;snrMax]
     * @param snrMax utilisé pour spécifier l'intervale [snrMin;snrMax]
     */
    private generationValeursTEB(float pas, float snrMin, float snrMax) {
        this.pas = pas;
        this.snrMin = snrMin;
        this.snrMax = snrMax;
    }

    /**
     * Récolte les TEB, avec le pas spécifié, jusqu'au TEB max spécifié
     * @param args Paramètre du simulateur (Sans spécifier le SNR !)
     */
    private void sampling(String[] args) throws Exception {
        data = new Information<>();
        this.args = args;

        for (float snr = snrMin; snr < snrMax; snr += pas) {
            String[] snrSimu = {"-mute", "-snr", Float.toString(snr), "-csv", "/Users/lucas/test.csv"};
            String[] argsSimu = Outils.concatenate(args, snrSimu);

            Simulateur simu = new Simulateur(argsSimu);
            simu.execute();
            float teb = simu.calculTauxErreurBinaire();

            data.add(teb);

            System.out.println("SNR - TEB : " + snr + " - " + teb);
        }
    }

    /**
     * Génération de la courbe pour un pas de 0.5 allant de -100 à 0
     * @param args Paramètres du simulateur
     * @throws Exception Simulateur en défaut
     */
    public static void main(String[] args) throws Exception {
        generationValeursTEB valid = new generationValeursTEB(1f, -100f, 0f);
        valid.sampling(args);
    }
}