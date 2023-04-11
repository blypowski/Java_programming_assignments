import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//autor: Jakub Marczyk

public class FieldInspector implements FieldInspectorInterface {

	@Override
	public Collection<FieldInspectorInterface.FieldInfo> inspect(String className) {
		Class<?> c;
		try {
			c = Class.forName(className);
			List<FieldInfo> kolekcja = new ArrayList<FieldInfo>();
			
			//bo chcemy tylko publiczne
			Field[] fields = c.getDeclaredFields();
			
			
			
			for(Field f: fields) {
				
				if(Modifier.isPublic(f.getModifiers())) {
				
				Type t;
				
				String type = f.getType().getName();
				String name = f.getName();
				int version=-1;
				
				Annotation[] annotation = f.getDeclaredAnnotations();
				
				switch(type) {
					case "java.lang.Integer":
					case "int":
						t = Type.INT;
						break;
						
					case "long":
					case "java.lang.Long":
						t = Type.LONG;
						break;
						
					case "float":
					case "java.lang.Float":
						t = Type.FLOAT;
						break;
						
					case "double":
					case "java.lang.Double":
						t = Type.DOUBLE;
						break;
					
					default:
						t = Type.OTHER;
						break;
				}
				
				for(Annotation a: annotation) {
					if(a instanceof FieldVersion) {
						FieldVersion fieldV = (FieldVersion) a;
						version = fieldV.version();
					}
				}
				
				kolekcja.add(new FieldInfo(t, name, version));
				}
				
			}
			
			return kolekcja;
		} catch (ClassNotFoundException e) {
			System.out.println("Class " + className + " could not be found!");
		}
		
		return null;
	}

}
