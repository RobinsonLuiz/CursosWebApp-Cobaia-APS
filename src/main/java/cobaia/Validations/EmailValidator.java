package cobaia.Validations;

import java.lang.reflect.Field;

import cobaia.Annotations.ValidaEmail;
import cobaia.Modelo.AbstractModel;

public class EmailValidator implements Validator {

	@Override
	public void validate(AbstractModel v) {
		for (Field f : v.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(ValidaEmail.class)) {
				f.setAccessible(true);
				try {
					if (f.get(v) == null) {
						v.addErro(f.getName(),f.getAnnotation(ValidaEmail.class).erro());
						return;
					}
					if(f.get(v).toString().length() == f.getAnnotation(ValidaEmail.class).vazio()) {
						v.addErro(f.getName(), f.getAnnotation(ValidaEmail.class).erro());
					}
					if(!f.get(v).toString().matches(f.getAnnotation(ValidaEmail.class).verificar())) {
						v.addErro(f.getName(), f.getAnnotation(ValidaEmail.class).erro());
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				f.setAccessible(false);
			}
		}
	}

}
