import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


/**
 * Klasa implementująca algorytm k-NN (k najbliższych sąsiadów) do klasyfikacji kwiatów Iris.
 */
public class KNN {
//    private final String pathTest = "C:/Users/fenix/PycharmProjects/Knn/iris.test.data";
//    private final String pathTrain = "C:/Users/fenix/PycharmProjects/Knn/iris.data";
//    private final int knndlugosc = 3;

    /**
     * Metoda główna, uruchamiająca algorytm k-NN.
     */
    public static void main(String[] args) {
        Menu mm = new Menu();
        mm.menu();
//        KNN tt = new KNN();
//        tt.kNN();
    }

    /**
     * Metoda uruchamiająca algorytm k-NN.
     */
    public void kNN(String pathTrain, String pathTest, int knndlugosc){
        List<List<String>> knn = readCSV(pathTrain,",");
        List<List<String>> knn_test = readCSV(pathTest,",");
        List<Iris> knnList = prepareList(knn);
        List<IrisTest> knn_testList = prepareList(knn_test,true);

        for (IrisTest test: knn_testList) {
            test.setNameClassified(knnclassification(knnList,test,knndlugosc));
        }

        for (IrisTest ir: knn_testList) {
            System.out.println(ir);
        }
        accuracy(knn_testList);


    }
    public void kNN(String pathTrain, IrisTest element, int knndlugosc){
        List<List<String>> knn = readCSV(pathTrain,",");
        List<Iris> knnList = prepareList(knn);

        element.setNameClassified(knnclassification(knnList,element,knndlugosc));

        System.out.println(element);


    }

    /**
     * Metoda dokonująca klasyfikacji na podstawie algorytmu k-NN.
     * @param knnList Lista zawierająca obiekty Iris do wykorzystania w algorytmie.
     * @param testElement Testowany obiekt Iris.
     * @param knnDlugosc Liczba najbliższych sąsiadów branych pod uwagę w klasyfikacji.
     * @return Zwraca zaklasyfikowaną klasę dla testowanego obiektu.
     */
    private String knnclassification(List<Iris> knnList, IrisTest testElement, int knnDlugosc) {
        String classified = null;
        List<ClassificationEntry> CfE = new ArrayList<>();
        List<String> CName = new ArrayList<>();

        for (Iris obj : knnList) {
            double euklides = odleglosc(obj.getVector(), testElement.getVector());
            String name = obj.getNameTrue();
            CfE.add(new ClassificationEntry(euklides, name));
        }

        CfE.sort(Comparator.comparingDouble(ClassificationEntry::getDoubleValue));

        for (int i = 0; i < knnDlugosc; i++) {
            CName.add(CfE.get(i).getStringValue());
        }

        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String str : CName) {
            frequencyMap.put(str, frequencyMap.getOrDefault(str, 0) + 1);
        }

        int maxFrequency = 0;
        for (String str : CName) {
            int frequency = frequencyMap.get(str);
            if (frequency > maxFrequency || (frequency == maxFrequency && classified == null)) {
                classified = str;
                maxFrequency = frequency;
            }
        }
        return classified;
    }


    /**
     * Metoda obliczająca odległość euklidesową między dwoma wektorami cech.
     * @param vectorTrain Wektor cech zbioru treningowego.
     * @param vectorCheck Wektor cech testowanego obiektu.
     * @return Zwraca obliczoną odległość między wektorami.
     */
    private double odleglosc(List<Double> vectorTrain, List<Double> vectorCheck){
        double euklides = 0;
        if (vectorCheck.size() == vectorTrain.size()){
            for (int i = 0; i < vectorCheck.size(); i++) {
                euklides += pow(vectorCheck.get(i)-vectorTrain.get(i),2);
            }
            euklides = sqrt(euklides);
        }else{
            euklides = -100;
        }
        return  euklides;
    }

    /**
     * Metoda obliczająca dokładność klasyfikacji.
     * @param irisTests Lista testowanych obiektów Iris.
     */
    private void accuracy(List<IrisTest> irisTests) {
        int size = irisTests.size();
        double correct = 0, failure = 0;
        for (IrisTest test : irisTests) {
            if (test.getNameClassified().equals(test.getNameTrue())) {
                correct += 1;
            } else {
                failure += 1;
            }
        }

        System.out.println("Accuracy correct= " + String.format("%.2f", correct / size));
        System.out.println("Accuracy failure= " + String.format("%.2f", failure / size));
    }

    /**
     * Metoda przetwarzająca listę list łańcuchów na listę obiektów Iris.
     * @param knnTest Lista list łańcuchów zawierająca dane testowe.
     * @return Zwraca listę obiektów Iris przygotowaną na podstawie danych testowych.
     */
    public List<Iris> prepareList(List<List<String>> knnTest) {
        List<Iris> preparedList = new ArrayList<>();
        for (List<String> sourceList: knnTest) {
            List<Double> vector = new ArrayList<>();
            for (int i = 0; i < sourceList.size()-1; i++) {
                vector.add(Double.parseDouble(sourceList.get(i)));
            }
            preparedList.add(new Iris(vector, sourceList.get(sourceList.size()-1)));
        }
        return  preparedList;
    }

    /**
     * Metoda przetwarzająca listę list łańcuchów na listę obiektów IrisTest.
     * @param knnTest Lista list łańcuchów zawierająca dane testowe.
     * @param test Flaga określająca, czy dane są testowe.
     * @return Zwraca listę obiektów IrisTest przygotowaną na podstawie danych testowych.
     */
    public List<IrisTest> prepareList(List<List<String>> knnTest, boolean test) {
        List<IrisTest> preparedList = new ArrayList<>();
        for (List<String> sourceList: knnTest) {
            List<Double> vector = new ArrayList<>();
            for (int i = 0; i < sourceList.size()-1; i++) {
                vector.add(Double.parseDouble(sourceList.get(i)));
            }
            preparedList.add(new IrisTest(vector, sourceList.get(sourceList.size()-1),null));
        }
        return  preparedList;
    }

    public IrisTest prepareElement(String element) {

        String[] el = element.split(",");
        List<Double> vector = new ArrayList<>();
        for (int i = 0; i < el.length-1; i++) {
            vector.add(Double.parseDouble(el[i]));
        }
        IrisTest ir =new IrisTest(vector, el[el.length-1],null);
        return  ir;
    }

    /**
     * Metoda przetwarzająca plik CSV na listę list łańcuchów.
     * @param path Ścieżka do pliku CSV.
     * @param delimiter Separator użyty do oddzielenia danych w pliku CSV.
     * @return Zwraca listę list łańcuchów zawierającą dane z pliku CSV.
     */
    public List<List<String>> readCSV(String path, String delimiter){
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }
}



/**
 * Reprezentuje kwiat Iris, przechowując wektor cech oraz prawdziwą klasę kwiatu.
 */
class Iris {
    private final UUID id;
    private List<Double> vector;
    private String nameTrue;

    // Konstruktor
    public Iris(List<Double> vector, String nameTrue) {
        this.id = UUID.randomUUID();
        this.vector = vector;
        this.nameTrue = nameTrue;
    }

    // Getter for id
    public UUID getId() {
        return id;
    }

    // Getter for vector
    public List<Double> getVector() {
        return vector;
    }

    // Setter for vector
    public void setVector(List<Double> vector) {
        this.vector = vector;
    }

    // Getter for nameTrue
    public String getNameTrue() {
        return nameTrue;
    }

    // Setter for nameTrue
    public void setNameTrue(String nameTrue) {
        this.nameTrue = nameTrue;
    }

    // Metoda toString
    @Override
    public String toString() {
        // Pobieranie dwóch pierwszych cyfr z identyfikatora
        String shortId = id.toString().substring(0, 2);
        return "Iris{" +
                "id=" + shortId +
                ", vector=" + vector +
                ", nameTrue='" + nameTrue + '\'' +
                '}';
    }
}

/**
 * Reprezentuje testowany kwiat Iris, dziedzicząc po klasie Iris i przechowując przewidywaną klasę.
 */
class IrisTest extends Iris {
    private String nameClassified;

    // Konstruktor
    public IrisTest(List<Double> vector, String nameTrue, String nameClassified) {
        super(vector, nameTrue);
        this.nameClassified = nameClassified;
    }

    // Getter for nameClassified
    public String getNameClassified() {
        return nameClassified;
    }

    // Setter for nameClassified
    public void setNameClassified(String nameClassified) {
        this.nameClassified = nameClassified;
    }

    // Metoda toString
    @Override
    public String toString() {
        return "IrisTest{" +
                "vector=" + getVector() +
                ", nameTrue='" + getNameTrue() + '\'' +
                ", nameClassified='" + nameClassified + '\'' +
                '}';
    }
}

/**
 * Reprezentuje wpis klasyfikacyjny zawierający odległość oraz klasyfikację.
 */
class ClassificationEntry {
    private String id;
    private double doubleValue;
    private String stringValue;

    // Konstruktor
    public ClassificationEntry(double doubleValue, String stringValue) {
        this.id = generateShortId(); // Generowanie skróconego identyfikatora
        this.doubleValue = doubleValue;
        this.stringValue = stringValue;
    }

    // Metoda generująca skrócony identyfikator
    private String generateShortId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 2);
    }

    // Getter dla id
    public String getId() {
        return id;
    }

    // Getter dla doubleValue
    public double getDoubleValue() {
        return doubleValue;
    }

    // Setter dla doubleValue
    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    // Getter dla stringValue
    public String getStringValue() {
        return stringValue;
    }

    // Setter dla stringValue
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    // Metoda toString
    @Override
    public String toString() {
        return "ClassificationEntry{" +
                "id=" + id +
                ", doubleValue=" + doubleValue +
                ", stringValue='" + stringValue + '\'' +
                '}';
    }
}