import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    Scanner sc = new Scanner(System.in);

    void menu() {
        KNN k = new KNN();
        System.out.println("Hello. Welcome to Knn classyffication.");
        System.out.println(" Chcesz pracować na pliku (1), czy na jednym przykładzie(2)?");
        int ch = sc.nextInt();
        if (ch == 1){
            System.out.println("Enter two path: train (collection to classyfication \n" +
                    "                test  (collection to test");
            String[] pp = pathFinding();
            k.kNN(pp[0],pp[1], knndlugosc());
        } else if (ch ==2) {
            k.kNN(pathFinding(true),jedenElement(),knndlugosc());
        }else{
            System.out.println("rozumiem chcesz zakończyć.");
        }


    }

    public IrisTest jedenElement(){
        System.out.println("zaczynam sekcje tworzenia vectora...");
        IrisTest ir = new IrisTest(null,null,null);
        while (true){
            List<Double> vec = new ArrayList<>();
            int i = 0;
            while (true){
                System.out.println("Wprowadz wspolrzedna nr: " + i+1);
                vec.add(sc.nextDouble());
                i++;
                System.out.println("Kolejny punkt (byle co) czy koniec (1)?");
                int choi = sc.nextInt();
                if (choi == 1){
                    break;
                }
            }
            System.out.println("twoj wektor to: " + vec);
            System.out.println("wprowadzam do kwiatu");
            ir.setVector(vec);
            System.out.println("podaj nazwe prawdziwą");
            ir.setNameTrue(sc.nextLine());

            System.out.println("czy wszystko sie zgadza? 1 - nie, whatever- exit");
            if (sc.nextInt() != 1){
                return  ir;
            }
        }
    }

    public int knndlugosc() {
        int dlugosc = 0;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.println("Wprowadź jaka jest badana długość knn:");
                dlugosc = sc.nextInt();
                sc.nextLine(); // Konsumowanie nowej linii
                valid = true; // Jeśli wszystko jest OK, ustawiamy valid na true
            } catch (InputMismatchException e) {
                System.out.println("Błąd: Wprowadzono nieprawidłową wartość. Proszę wprowadzić liczbę całkowitą.");
                sc.nextLine(); // Konsumowanie nieprawidłowego wejścia
            }
        }

        return dlugosc;
    }

    String[] pathFinding() {
        String[] pathMap = new String[2];

        while (true) {
            try {
                System.out.print("Train Path: ");
                String pathTrain = sc.nextLine();
                System.out.println("Wprowadzono path: " + pathTrain);

                System.out.print("Test Path: ");
                String pathTest = sc.nextLine();
                System.out.println("Wprowadzono path: " + pathTest);

                System.out.println("Czy oba path są poprawne?\n" +
                        "1. tak, kontynuuj\n" +
                        "default. nie, wprowadź ponownie");

                int choice = sc.nextInt();
                sc.nextLine(); // Konsumowanie nowej linii pozostałej po nextInt()

                if (choice == 1) {
                    pathMap[0] = pathTrain;
                    pathMap[1] = pathTest;
                    break;
                } else {
                    System.out.println("Zaczynamy od nowa");
                }
            } catch (InputMismatchException e) {
                System.out.println("Błąd: Wprowadzono nieprawidłową wartość. Proszę wprowadzić liczbę całkowitą.");
                sc.nextLine(); // Konsumowanie nieprawidłowego wejścia
            } catch (Exception e) {
                System.out.println("Wystąpił nieoczekiwany błąd: " + e.getMessage());
                sc.nextLine(); // Konsumowanie nieprawidłowego wejścia, jeśli wystąpił inny błąd
            }
        }

        return pathMap;
    }
    String pathFinding(boolean element) {

        while (true) {
            try {
                System.out.print("Train Path: ");
                String pathTrain = sc.nextLine();
                System.out.println("Wprowadzono path: " + pathTrain);

                System.out.println("Czy path jest poprawny?\n" +
                        "1. tak, kontynuuj\n" +
                        "default. nie, wprowadź ponownie");

                int choice = sc.nextInt();
                sc.nextLine(); // Konsumowanie nowej linii pozostałej po nextInt()

                if (choice == 1) {
                    return pathTrain;
                } else {
                    System.out.println("Zaczynamy od nowa");
                }
            } catch (InputMismatchException e) {
                System.out.println("Błąd: Wprowadzono nieprawidłową wartość. Proszę wprowadzić liczbę całkowitą.");
                sc.nextLine(); // Konsumowanie nieprawidłowego wejścia
            } catch (Exception e) {
                System.out.println("Wystąpił nieoczekiwany błąd: " + e.getMessage());
                sc.nextLine(); // Konsumowanie nieprawidłowego wejścia, jeśli wystąpił inny błąd
            }
        }
    }
}
