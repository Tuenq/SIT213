import java.io.FileWriter;
import java.io.IOException;

public class extractionCSV {
    public static void sauvegardeData(String file, float TEB, float snr){
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            String stringTEB = Float.toString(TEB).replace(".", ",");
            String stringSnr = Float.toString(snr).replace(".", ",");
            fileWriter.write(stringSnr + ";" + stringTEB + "\n");
            fileWriter.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
