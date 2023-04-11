import java.util.Map;
import java.util.function.Consumer;

/**
 * Interfejs systemu wyliczającego histogram z dostarczonych w wektorze danych
 */
public interface Histogram {

	public record HistogramResult(int vectorID, Map<Integer, Integer> histogram) {
	}

	/**
	 * Metoda ustala sposób generowania histogramów. Metoda używana jednokrotnie
	 * przed pierwszym wykonaniem setVector.
	 * 
	 * @param bins              maksymalna liczba koszyków, do których wpadają
	 *                          liczby odczytane z wektora. Największa wartość
	 *                          odczytana z wektora będzie równa co najwyżej bins-1.
	 *                          UWAGA: znajomość bins nie jest konieczna do
	 *                          policzenia histogramu, ale może ułatwić to zadanie.
	 * @param histogramConsumer Obiekt, do którego należy przekazywać wyliczone
	 *                          histogramy. UWAGA: metodę accept nie może wykonać
	 *                          ten sam wątek, który generował histogram!!!
	 */
	public void setup(int bins, Consumer<HistogramResult> histogramConsumer);

	/**
	 * Metoda przekazuje wektor liczb całkowitych do przetworzenia na histogram.
	 * Metoda nie może zablokować pracy wątku, który ją wywoła. Ma spowodować
	 * asynchroniczne uruchomienie obliczeń i się zakończyć. Wraz z wektorem
	 * przekazywany jest unikalny numer vectorID - pozwala on na dopasowanie
	 * histogramu do wektora z danymi wejściowymi.
	 * 
	 * @param vectorID unikalny identyfikator wektora
	 * @param vector   wektor liczb
	 */
	public void addVector(int vectorID, Vector vector);
}
