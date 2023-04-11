package zd7;
import java.util.Map;

/**
 * Interfejs systemu wyliczającego histogram z
 * dostarczonych w wektorze danych 
 */
public interface Histogram {
	/**
	 * Metoda ustala sposób generowania histogramów. Metoda używana jednokrotnie
	 * przed pierwszym wykonaniem setVector.
	 * 
	 * @param threads liczba wątków, które należy użyć do wygenerowania jednego histogramu.
	 *                Dokładnie tyle, ani mniej, ani więcej.
	 * @param bins    maksymalna liczba koszyków, do których wpadają liczby odczytane z
	 *                wektora. Największa wartość odczytana z wektora będzie równa
	 *                co najwyżej bins-1. UWAGA: znajomość bins nie jest konieczna do policzenia
	 *                histogramu, ale może ułatwić to zadanie.
	 */
	public void setup(int threads, int bins);

	/**
	 * Metoda przekazuje wektor liczb całkowitych do przetworzenia na histogram.
	 * Metoda nie może zablokować pracy wątku, który ją wywoła. Ma spowodować
	 * asynchroniczne uruchomienie obliczeń i się zakończyć.
	 * 
	 * @param vector wektor liczb
	 */
	public void setVector(Vector vector);

	/**
	 * Pozwala na sprawdzenie, czy histogram jest gotowy do odbioru. Przyjmuje
	 * wartość false od chwili przekazania wektora z danymi do chwili przygotowania
	 * histogramu do odbioru. Przed wykonaniem setVector wartość nieustalona.
	 * 
	 * @return true - histogram gotowy, false - histogram w trakcie przetwarzania.
	 */
	public boolean isReady();

	/**
	 * Zwraca gotowy histogram. Przed wystąpieniem isReady==true, wartość
	 * nieustalona. Histogram to mapa wartość na liczba powtórzeń tej wartości w
	 * wektorze. UWAGA: mapa nie może zawierać kluczy, których liczba 
	 * wystąpień wynosi 0.
	 * 
	 * @return histogram danych zapisanych w przekazanym wektorze.
	 */
	public Map<Integer, Integer> histogram();
}
