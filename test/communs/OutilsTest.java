package communs;

import org.junit.Test;

import static org.junit.Assert.*;

public class OutilsTest {

    @Test
    public void booleanDistance_limite_haute_nominal() {
        // Arrange
        final float max = 1;
        final float min = -1;
        final boolean expected_value = true;

        // Act
        final boolean actual_value = Outils.booleanDistance(max, min, max);

        // Assert
        assertEquals(expected_value, actual_value);
    }

    @Test
    public void booleanDistance_limite_basse_nominal() {
        // Arrange
        final float max = 1;
        final float min = -1;
        final boolean expected_value = false;

        // Act
        final boolean actual_value = Outils.booleanDistance(min, min, max);

        // Assert
        assertEquals(expected_value, actual_value);
    }

    @Test
    public void booleanDistance_equidistance_nominal() {
        // Arrange
        final float max = 1f;
        final float min = -1f;
        final boolean expected_value = false;

        // Act
        final boolean actual_value = Outils.booleanDistance(min-max, min, max);

        // Assert
        assertEquals(expected_value, actual_value);
    }

}