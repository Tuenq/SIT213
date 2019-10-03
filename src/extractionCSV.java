import java.io.FileWriter;
import java.io.IOException;

public class extractionCSV {
    public static void sauvegardeData(String file, float TEB, float snr){
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(snr + ";" + TEB + "\n");
            fileWriter.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
