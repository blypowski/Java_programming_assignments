import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Interfejs systemu inspekcji pól klasy.
 */
public interface FieldInspectorInterface {
	/**
	 * Typ wyliczeniowy reprezentujący wybrane typy danych 
	 * w Java.  Uwaga typy pasują zarówno do typów prymitywnych jak i ich
	 * wrapperów: np. typ Type.INT odpowiada zarówno 
	 * typowi int jak i Integer. OTHER należy użyć w przypadku braku
	 * możliwości użycia lepszego (dokładniejszego) opisu. 
	 */
	public enum Type {
		INT, LONG, FLOAT, DOUBLE, OTHER;
	}

	/**
	 * Klasa opisu pola klasy.
	 */
	public class FieldInfo {
		/**
		 * Typ pola.
		 */
		public final Type type;
		/**
		 * Nazwa pola
		 */
		public final String name;
		/**
		 * Numer wersji przepisany z adnotacji @FieldVersion
		 */
		public final int version;

		/**
		 * Konstruktor używany do przedstawienia pól znaczonych adnotacją
		 * FieldVersion
		 * @param type typ pola
		 * @param name nazwa pola
		 * @param version numer wersji
		 */
		public FieldInfo(Type type, String name, int version) {
			this.type = type;
			this.name = name;
			this.version = version;
		}

		/**
		 * Konstruktor używany do przedstawienia pól, które nie są oznaczone
		 * adnotacją FieldVersion
		 * @param type typ
		 * @param name nazwa pola
		 */
		public FieldInfo(Type type, String name) {
			this(type, name, -1);
		}
	}

	/**
	 * Adnotacja z numerem wersji.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface FieldVersion {
		public int version();
	}

	/**
	 * Metoda wykonuje inspekcję klasy o podanej nazwie. Inspekcja polega na
	 * sporządzeniu kolekcji obiektów FieldInfo. Kolekcja ma zawierać wszystkie
	 * publiczne pola zadeklarowane w analizowanej klasie. Pola obdarzone adnotacją
	 * typu FieldVersion mają mieć ustawioną w tej adnotacji wartość version (dla
	 * innych version ma mieć wartość domyślną).
	 * 
	 * @param className nazwa analizowanej klasy
	 * @return kolekcja obiektów opisujacych publiczne pola zadeklarowane w
	 *         analizowanej klasie.
	 */
	public Collection<FieldInfo> inspect(String className);
}
