package convertisseurs;

import org.junit.Test;
import static org.junit.Assert.*;

import destinations.*;
import information.*;
import sources.*;


public class DecodeurCanalTest {
    @Test
    public void nominalTest() throws InformationNonConforme {
        // ARRANGE : expected - actual
        String emitedBoolean = "010101010101010010101101";
        Information<Boolean> emitedInfo = new InformationBooleen(emitedBoolean);
        String expectedBoolean = "01010011";
        Information<Boolean> expectedInfo = new InformationBooleen(expectedBoolean);

        Source<Boolean> source = new SourceFixe(emitedInfo);
        DecodeurCanal codageCanal = new DecodeurCanal();
        Destination<Boolean> destination = new DestinationFinale();
        // Connecting
        source.connecter(codageCanal);
        codageCanal.connecter(destination);

        // ACT
        source.emettre();
        Information<Boolean> actualInfo = destination.getInformationRecue();

        // ASSERT
        assertEquals(expectedInfo, actualInfo);
    }
}

