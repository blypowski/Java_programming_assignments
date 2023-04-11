/**
 * Obiektowa reprezentacja wektora liczb.
 */
public interface Vector {
	/**
	 * Zwraca wartość zapisaną na pozycji idx. Metoda bezpieczna w użyciu przez
	 * wiele wątków jednocześnie. Dozwolone wartości idx od 0 do getSize()-1
	 * włącznie.
	 * 
	 * @param idx pozycja odczytu
	 * @return wartość zapisana na pozycji idx. Od 0 do bins-1 włączenie (patrz
	 *         metoda setup intefejsu Histogram).
	 */
	public int getValue(int idx);

	/**
	 * Rozmiar wektora. Determinuje poprawne wartości indeksów (od 0 do getSize()-1
	 * włącznie).
	 * 
	 * @return rozmiar wektora.
	 */
	public int getSize();
}
