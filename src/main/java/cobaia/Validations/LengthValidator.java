package cobaia.Validations;

import java.lang.reflect.Field;

import cobaia.Annotations.ValidaLength;
import cobaia.Modelo.AbstractModel;

public class LengthValidator implements Validator {

	@Override
	public void validate(AbstractModel v) {
		for (Field f : v.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(ValidaLength.class)) {
				f.setAccessible(true);
				try {
					if (f.get(v) == null) {
						v.addErro(f.getName(),f.getAnnotation(ValidaLength.class).erro());
						return;
					}
					if ((f.get(v).toString().length() > f.getAnnotation(ValidaLength.class).max()) 
					 	 || (f.get(v).toString().length() < f.getAnnotation(ValidaLength.class).min())) 
					{
						v.addErro(f.getName(),f.getAnnotation(ValidaLength.class).erro());
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				f.setAccessible(false);
			}
		}
	}

}
