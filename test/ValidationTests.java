import java.util.Scanner;

public class ValidationTests {

    private static void validationEtape1() {
        String[] args1 = {"-s", "-seed", "123"};
        String[] args2 = {"-s", "-mess", "000000111111"};

        try {
            Simulateur simulateur = new Simulateur(args1);
            simulateur.execute();

            simulateur = new Simulateur(args2);
            simulateur.execute();
        }
        catch (Exception exception){
            exception.printStackTrace();
            System.out.println("Erreur durant le test de validation etape 1");
        }
    }

    private static void validationEtape2() {
        String[] args1 = {"-s", "-seed", "123", "-nbEch", "1000", "-form", "RZ"};
        String[] args2 = {"-s", "-seed", "123", "-nbEch", "1000", "-form", "NRZ"};
        String[] args3 = {"-s", "-seed", "123", "-nbEch", "1000", "-form", "NRZT"};
        try {
            Simulateur simulateur = new Simulateur(args1);
            simulateur.execute();

            simulateur = new Simulateur(args2);
            simulateur.execute();

            simulateur = new Simulateur(args3);
            simulateur.execute();
        }
        catch (Exception exception){
            System.out.println("Erreur durant le test de validation etape 2");
        }
    }

    private static void validationEtape3() {
        String[] args = {"-s", "-seed", "123", "-snr", "10", "-nbEch", "1000", "-form", "RZ"};
        try {
            Simulateur simulateur = new Simulateur(args);
            simulateur.execute();
        }
        catch (Exception exception){
            System.out.println("Erreur durant le test de validation etape 3 affichage signal bruité");
        }
    }

    public static void main(String[] args) {
        System.out.println("Pour quelle étape voulez vous lancer un test de validation ?");

        Scanner scanner = new Scanner(System.in);

        switch (scanner.nextLine()){
            case "1":
                validationEtape1();
                break;
            case "2":
                validationEtape2();
                break;
            case "3":
                validationEtape3();
                break;
            default:
                System.out.println("Etape non reconue");
        }

    }
}