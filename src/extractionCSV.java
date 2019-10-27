import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.IOException;

/**
 * La classe extractionCSV permet d'extraire les donnée de TEB obtenu par rapport à un SNR spécifique et de les
 * enregistrer dans un fichier csv
 */
class extractionCSV {
    static void sauvegardeData(String file, float[] teb, float[] snr){
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            String stringTEB = StringUtils.join(ArrayUtils.toObject(teb), ",");
            String stringSNR = StringUtils.join(ArrayUtils.toObject(snr), ",");
            fileWriter.write(stringSNR + "\n" + stringTEB + "\n");
            fileWriter.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
