import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class SimulateurTest {

// region TEST ETAPE OPTIONNEL

    @Test
    public void utilisationSimulateur_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {""};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void utilisationSimulateur_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-o no"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test
    public void utilisationOption_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-h"};

        // Act & Assert
        new Simulateur(args);
    }

// endregion

// region TEST ETAPE CONSEILLE

    @Test
    public void accesPubliqueExecute() throws NoSuchMethodException {
        Method method = Simulateur.class.getMethod("execute");
        Assert.assertTrue(Modifier.isPublic(method.getModifiers()));
    }

    @Test
    public void accesPubliqueCalculTEB() throws NoSuchMethodException {
        Method method = Simulateur.class.getMethod("calculTauxErreurBinaire");
        Assert.assertTrue(Modifier.isPublic(method.getModifiers()));
    }

//    TODO: Implement !
//    @Test
//    public void calculTauxErreurBinaire_min() {
//
//    }

//    TODO: Implement !
//    @Test
//    public void calculTauxErreurBinaire_max() {
//
//    }

// endregion

// region TEST ETAPE 1

    // region TEST ETAPE 1 - OPTION : -mess m

    @Test
    public void messageOption_binaire_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-mess", "01010011"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test
    public void messageOption_decimaux_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-mess", "12345"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void messageOption_parametreVide_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-mess"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void messageOption_binaire_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-mess", "0101AA11"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void messageOption_decimaux_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-mess", "123A45"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void messageOption_taille_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-mess", "0"};

        // Act & Assert
        new Simulateur(args);
    }

    // endregion

    // region TEST ETAPE 1 - OPTION : -s

    @Test
    public void sondeOption_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-s"};

        // Act & Assert
        new Simulateur(args);
    }

    // endregion

    // region TEST ETAPE 1 - OPTION : -seed v

    @Test
    public void germeOption_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-seed", "123"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void germeOption_conversionInvalide_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-seed", "ABC"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void germeOption_parametreVide_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-seed"};

        // Act & Assert
        new Simulateur(args);
    }

    // endregion

// endregion

// region TEST ETAPE 2

    // region ETAPE 2 - OPTION : -form f

   @Test
   public void formeOnde_RZ_nominal() throws ArgumentsException {
       // Arrange
       String[] args = {"-form", "RZ"};

       // Act & Assert
       new Simulateur(args);
   }

    @Test
    public void formeOnde_NRZ_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-form", "NRZ"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test
    public void formeOnde_NRZT_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-form", "NRZT"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void formeOnde_parametreInvalid_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-form", "NONE"};

        // Act & Assert
        new Simulateur(args);
    }

    // endregion

    // region ETAPE 2 - OPTION : -nbEch ne

    @Test
    public void nombreEchantillon_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-nbEch", "100"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void nombreEchantillon_negatif_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-nbEch", "-100"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void nombreEchantillon_nulle_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-nbEch", "0"};

        // Act & Assert
        new Simulateur(args);
    }

    // endregion

    // region ETAPE 2 - OPTION : -ampl min max

    @Test
    public void amplitude_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-ampl", "-12", "12"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void amplitude_max_min_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-ampl", "12", "-12"};

        // Act & Assert
        new Simulateur(args);
    }

    @Test(expected = ArgumentsException.class)
    public void amplitude_same_value_erreur() throws ArgumentsException {
        // Arrange
        String[] args = {"-ampl", "0", "0"};

        // Act & Assert
        new Simulateur(args);
    }

    // endregion

// endregion

// region TEST ETAPE 3

    // region TEST ETAPE 3 - OPTION : -snr s
    @Test
    public void ratioSignalSurBruit_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-snr", "1"};

        // Act & Assert
        new Simulateur(args);
    }
    // endregion

// endregion

// region TEST ETAPE 4

    // region TEST ETAPE 4 - OPTION : -ti dt0 ar0
    @Test
    public void trajetIndirect_unique_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-ti", "0", "0.0"};

        // Act & Assert
        new Simulateur(args);
    }
    // endregion

    // region TEST ETAPE 4 - OPTION : -ti dt0 ar0 dt1 ar1
    @Test
    public void trajetIndirect_double_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-ti", "0", "0.0", "0", "0.0"};

        // Act & Assert
        new Simulateur(args);
    }
    // endregion

    // region TEST ETAPE 4 - OPTION : -ti dt0 ar0 dt1 ar1 dt2 ar2
    @Test
    public void trajetIndirect_triple_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-ti", "0", "0.0", "0", "0.0", "0", "0.0"};

        // Act & Assert
        new Simulateur(args);
    }
    // endregion

    // region TEST ETAPE 4 - OPTION : -ti dt0 ar0 dt1 ar1 dt2 ar2 dt3 ar3
    @Test
    public void trajetIndirect_quadruple_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-ti", "0", "0.0", "0", "0.0", "0", "0.0", "0", "0.0"};

        // Act & Assert
        new Simulateur(args);
    }
    // endregion

    // region TEST ETAPE 4 - OPTION : -ti dt0 ar0 dt1 ar1 dt2 ar2 dt3 ar3 dt4 ar4
    @Test
    public void trajetIndirect_pentuple_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-ti", "0", "0.0", "0", "0.0", "0", "0.0", "0", "0.0", "0", "0.0"};

        // Act & Assert
        new Simulateur(args);
    }
    // endregion

// endregion

// region TEST ETAPE 5

    // region TEST ETAPE 3 - OPTION : -snr s
    @Test
    public void codeur_nominal() throws ArgumentsException {
        // Arrange
        String[] args = {"-codeur"};

        // Act & Assert
        new Simulateur(args);
    }
    // endregion

// endregion
}