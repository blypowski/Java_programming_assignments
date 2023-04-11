/**
 * Interfejs systemu budującego proste zdania ze słów zapisanych w plikowej
 * bazie danych.
 *
 */
public interface GeneratorZdan {
	/**
	 * Metoda przekazuje położenie pliku z bazą danych SQLite.
	 * 
	 * @param filename plik z bazą danych
	 */
	public void plikBazyDanych(String filename);

	/**
	 * Metoda odtwarza zdanie na podstawie danych zapisanych w bazie danych.
	 * 
	 * @param zdanieID klucz zdania
	 * @return Zdanie wygenerowane na podstawie wpisów w bazie.
	 */
	public String zbudujZdanie(int zdanieID);
}
