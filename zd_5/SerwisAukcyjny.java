import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SerwisAukcyjny implements Aukcja {
	
	//mapy dla wygody
	Map<Integer, Przedmiot> przedmioty = new HashMap<Integer, Przedmiot>();
	
	Map<String, Użytkownik> użytkownicy = new HashMap<String, Użytkownik>();
	
	//powiadamiamy użytkowników o niższej ofercie jak została przebita. zatem musimy 
	//nie ma potrzeby, by użytkownik przechowywał info o wszystkich przedmiotach, które obserwuje
	//nie jest to niezbędne do realizacji zadania
	Map<Integer, Set<String>> powiadomienia = new HashMap<Integer, Set<String>>();
	
	//dla każdego id przedmiotu trzymam mapę ile użytkownik jest w stanie zapłacić
	Map<Integer, Map<String, Integer>> oferty = new HashMap<Integer, Map<String, Integer>>();
	

	@Override
	public void dodajUżytkownika(String username, Powiadomienie kontakt) {
		Użytkownik u = new Użytkownik(username, kontakt);
		użytkownicy.put(username, u);
	}

	@Override
	public void dodajPrzedmiotAukcji(PrzedmiotAukcji przedmiot) {
			int id = przedmiot.identyfikator();
			Przedmiot p = new Przedmiot(przedmiot);
			przedmioty.put(id, p);
	}

	@Override
	public void subskrypcjaPowiadomień(String username, int identyfikatorPrzedmiotuAukcji) {
		if(powiadomienia.containsKey(identyfikatorPrzedmiotuAukcji)) {
				Set<String> zbiór = powiadomienia.get(identyfikatorPrzedmiotuAukcji);
				zbiór.add(username);
				powiadomienia.put(identyfikatorPrzedmiotuAukcji, zbiór);
		}
		else {
			powiadomienia.put(identyfikatorPrzedmiotuAukcji, new HashSet<String>());
		}
	}

	@Override
	public void rezygnacjaZPowiadomień(String username, int identyfikatorPrzedmiotuAukcji) {
				Set<String> zbiór = powiadomienia.get(identyfikatorPrzedmiotuAukcji);
				
				if(zbiór!=null ) {
				zbiór.remove(username);
				powiadomienia.put(identyfikatorPrzedmiotuAukcji, zbiór);
				}
	}

	@Override
	public void oferta(String username, int identyfikatorPrzedmiotuAukcji, int oferowanaKwota) {
		
		//przedmiot musi być oferowany
		if(przedmioty.get(identyfikatorPrzedmiotuAukcji)!=null) {
			//aukcja musi wciąż trwać
			if(przedmioty.get(identyfikatorPrzedmiotuAukcji).wciążTrwa()) {
				Map<String, Integer> tempMapa = oferty.get(identyfikatorPrzedmiotuAukcji);
				if(tempMapa==null) {
					tempMapa = new HashMap<String, Integer>();
				}
				
				tempMapa.put(username, oferowanaKwota);
				oferty.put(identyfikatorPrzedmiotuAukcji, tempMapa);
				if(oferowanaKwota> przedmioty.get(identyfikatorPrzedmiotuAukcji).aktualnaCena) {
					przedmioty.get(identyfikatorPrzedmiotuAukcji).aktualnaCena = oferowanaKwota;
					przedmioty.get(identyfikatorPrzedmiotuAukcji).aktualnaOferta = oferowanaKwota;
					przedmioty.get(identyfikatorPrzedmiotuAukcji).wygrywającyUżytkownik = username;
				}
				subskrypcjaPowiadomień(username, identyfikatorPrzedmiotuAukcji);
				powiadom(identyfikatorPrzedmiotuAukcji, oferowanaKwota);
			}
		
		}
	}
	
	public void powiadom(int id, int cena) {
		for(String s: powiadomienia.get(id)) {
			if(oferty.get(id).get(s)<cena) {
				Przedmiot p = przedmioty.get(id);
				użytkownicy.get(s).powiadom(p);
			}
		}
		
	}

	@Override
	public void koniecAukcji(int identyfikatorPrzedmiotuAukcji) {
		//tutaj trzeba usunąć ze wszystkich map, również z tych gdzie od użytkownika mapuję do przedmiotów
		//nie!!!! trzeba zamknąć możliwość składania ofert!!! 
		przedmioty.get(identyfikatorPrzedmiotuAukcji).wciążTrwa = false;
		/*
		oferty.remove(identyfikatorPrzedmiotuAukcji);
		powiadomienia.remove(identyfikatorPrzedmiotuAukcji);
		*/
	}

	@Override
	public String ktoWygrywa(int identyfikatorPrzedmiotuAukcji) {
		Przedmiot przed = przedmioty.get(identyfikatorPrzedmiotuAukcji);
		return przed.wygrywającyUżytkownik();
	}

	@Override
	public int najwyższaOferta(int identyfikatorPrzedmiotuAukcji) {
		Przedmiot przed = przedmioty.get(identyfikatorPrzedmiotuAukcji);
		return przed.aktualnaOferta();
	}

}

class Przedmiot implements Aukcja.PrzedmiotAukcji {
	int identyfikator;
	String nazwaPrzedmiotu;
	int aktualnaCena;
	int aktualnaOferta;
	String wygrywającyUżytkownik;
	boolean wciążTrwa = true;
	
	public Przedmiot(Aukcja.PrzedmiotAukcji przedm) {
		this.identyfikator = przedm.identyfikator();
		this.nazwaPrzedmiotu = przedm.nazwaPrzedmiotu();
		this.aktualnaCena = przedm.aktualnaCena();
		this.aktualnaOferta = przedm.aktualnaOferta();
	}
	
	public boolean wciążTrwa() {
		return this.wciążTrwa;
	}
	
	@Override
	public int identyfikator() {
		return this.identyfikator;
	}
	@Override
	public String nazwaPrzedmiotu() {
		return this.nazwaPrzedmiotu;
	}
	@Override
	public int aktualnaOferta() {
		return aktualnaOferta;
	}
	@Override
	public int aktualnaCena() {
		return aktualnaCena;
	}
	
	public String wygrywającyUżytkownik() {
		return wygrywającyUżytkownik;
	}
	
	
}

class Użytkownik {
	String username;
	Aukcja.Powiadomienie powiadomienie;
	
	public Użytkownik(String uname, Aukcja.Powiadomienie pow){
		this.username = uname;
		this.powiadomienie = pow;
	}
	
	public String username() {
		return this.username;
	}
	
	public Aukcja.Powiadomienie powiadomienie() {
		return this.powiadomienie;
	}
	
	public void powiadom(Przedmiot przedmiot) {
		this.powiadomienie.przebitoTwojąOfertę(przedmiot);
	}
}
