package encoders;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class EncoderRZTest {

    @Test
    public void booleanDistance_limite_haute_nominal() {
        // Arrange
        final float max = 1;
        final float min = -1;
        final boolean expected_value = true;
        Encoder enc = new EncoderRZ(1, min, max);

        // Act
        final boolean actual_value = enc.booleanDistance(max);

        // Assert
        assertEquals(expected_value, actual_value);
    }

    @Test
    public void booleanDistance_limite_basse_nominal() {
        // Arrange
        final float max = 1;
        final float min = -1;
        final boolean expected_value = false;
        Encoder enc = new EncoderRZ(1, min, max);

        // Act
        final boolean actual_value = enc.booleanDistance(min);

        // Assert
        assertEquals(expected_value, actual_value);
    }

    @Test
    public void booleanDistance_equidistance_nominal() {
        // Arrange
        final float max = new Random().nextFloat();
        final float min = new Random().nextFloat() * -1f;
        final boolean expected_value = false;
        Encoder enc = new EncoderRZ(1, min, max);

        // Act
        final boolean actual_value = enc.booleanDistance(min-max);

        // Assert
        assertEquals(expected_value, actual_value);
    }
}