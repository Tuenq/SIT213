
import org.junit.Test;
import java.io.*;


public class Step3 {
    @Test
    public void visualisation_NRZT() throws Exception {
        String[] args = {"-s", "-seed", "123", "-nbEch", "1000", "-form", "NRZT"};
        Simulateur simulateur = new Simulateur(args);
        simulateur.execute();
    }

    @Test
    public void visualisation_NRZ() throws Exception {
        String[] args = {"-s", "-seed", "123", "-snr", "40", "-nbEch", "1000", "-form", "NRZ"};
        Simulateur simulateur = new Simulateur(args);
        simulateur.execute();
    }

    @Test
    public void visualisation_RZ() throws Exception {
        String[] args = {"-s", "-seed", "123", "-snr", "100", "-nbEch", "1000", "-form", "RZ"};
        Simulateur simulateur = new Simulateur(args);
        simulateur.execute();
    }

    @Test
    public void visualisation_Gaussienne() throws Exception {
        for(int i=0;i<100;i++){
            String[] args = {"-s", "-mess", "00000000000", "-snr", "0", "-nbEch", "1000", "-form", "NRZ"};
            Simulateur simulateur = new Simulateur(args);
            simulateur.execute();
        }
    }
}


