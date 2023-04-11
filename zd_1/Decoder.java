package decoder;
/**
 * Klasa dekodera.
 * @author Jakub Marczyk
 */
public class Decoder {
	
	/**
	 * Do rozwiązania zadania zaimplementowana została maszyna stanów, zawierająca pola state, tempStr, str, repNumber.
	 */
	
	/**
	 * string zawierający komunikat zwrotny, który zostanie przesłany po wywołaniu funkcji output().
	 */
	String str="";
	
	/**
	 * string przechowujący dotychczas wczytaną zawartość części danych
	 */
	String tempStr="";
	
	/**
	 * zmienna do przechowywania liczby znajdującej się w części powtórzeń
	 */
	double repNumber;
	
	/**
	 * Zmienna przechowująca zawartość maszyny stanów.
	 * stany:
	 * 9 - stan początkowy, wczytywanie liczb składających się na część danych.
	 * 0 - wczytano "0" - przechodzimy do wczytywania czteroznakowej części powtórzeń.
	 * 1 - wczytano pierwszy znak liczby powtórzeń, można wczytać drugi.
	 * 2 - wczytano drugi znak liczby powtórzeń, można wczytać trzeci.
	 * 3 - wczytano trzeci znak liczby powtórzeń, można wczytać czwarty.
	 */
	int state=9;
	
	/**
	 * Metoda pozwalająca na wprowadzanie danych.
	 * @param value dostarczona wartość
	 */
	public void input( byte value ) {
		if(value>=0 && value<=9) {	//pomijamy wczytywanie niewłaściwych znaków, przejścia następują jedynie dla liczb z zakresu od 0 do 9.
			if(state==9) {	//wczytywanie części danych
				if(value>=1 && value<=9)
					tempStr+=value; //wczytywanie następuje "liczba po liczbie"
				else if(value==0) //wczytano znak oddzielający część danych od części powtórzeń
					state = 0; //następna wczytana liczba będzie pierwszą cyfrą sekcji powtórzeń
			}
			else if(state>=0 && state<=2) { //wczytywanie pierwszych trzech cyfr sekcji powtórzeń
				repNumber= repNumber*10+value;
				state++;
			}
			else if(state==3) {	//wczytywanie ostatniej cyfry sekcji powtórzeń
				repNumber=repNumber*10+value;
				for(int i=0;i<repNumber;i++) {
					str+=tempStr; //odpowiedź składa się z części danych powielonej tyle razy, ile wynosi liczba znajdująca się w częsci powtórzeń
				}
				state=9;	//po wczytaniu sekcji należy przywrócić domyślny stan automatu i zresetować zmienne - można wczytać od początku kolejną sekcję.
				tempStr="";
				repNumber=0;
			}
		}
	}
	
	/**
	 * Metoda pozwalająca na pobranie wyniku dekodowania danych.
	 * @return wynik działania
	 */
	public String output() {
		return str;
	}
	
	/**
	 * funkcja przywraca maszynę stanów do stanu początkowego (state=9)
	 * oraz zeruje zmienne tempStr i repNumber.
	 * Można rozpocząć wczytywanie nowej sekcji.
	 */
	public void reset() {
		state = 9;
		tempStr = "";
		repNumber=0;
		str="";
	}
}
