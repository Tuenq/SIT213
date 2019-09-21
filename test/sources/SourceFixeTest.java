package sources;

import information.Information;
import information.InformationNonConforme;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SourceFixeTest {

    @Test
    public void emettreNominal() {
        Information<Boolean> expectedMessage = new Information<>();
        try {
            expectedMessage = Information.stringToBoolean("0100100100101100100100");
        }
        catch (InformationNonConforme exception){
            fail("Cast information invalide");
        }

        Source<Boolean> source = new SourceFixe(expectedMessage);

        try {
            source.emettre();
        }
        catch (Exception exception){
            fail("Exception: " + exception);
        }

        Information<Boolean> actualMessage = source.informationEmise;
        Assert.assertEquals(expectedMessage, actualMessage);
    }
    
    public static void main (String[] args) {
    	emettreNominal();
    }
}