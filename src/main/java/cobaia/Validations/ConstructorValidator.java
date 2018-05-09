package cobaia.Validations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import cobaia.Modelo.AbstractModel;

public class ConstructorValidator {
	
	public void validate(AbstractModel e) throws Exception {
		for (Field f : e.getClass().getDeclaredFields()) {
			for (Annotation c : f.getDeclaredAnnotations()) {
				for (Annotation c1 : c.annotationType().getAnnotations()) {
					if (c1.annotationType().getSimpleName().toLowerCase().equals("constraint")) {
						Annotation constraint = c1;
						for (Method m : constraint.annotationType().getDeclaredMethods()) {
							m.setAccessible(true);
							Class<?> clz = (Class<?>) m.invoke(constraint);
							Validator valida = (Validator) clz.newInstance();
							valida.validate(e);
						}
					}
				}
			}
		}
	}
}
