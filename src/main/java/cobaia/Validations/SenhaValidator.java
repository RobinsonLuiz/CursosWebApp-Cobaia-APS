package cobaia.Validations;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cobaia.Annotations.ValidaLength;
import cobaia.Annotations.ValidaSenha;
import cobaia.Modelo.AbstractModel;

public class SenhaValidator implements Validator {
	private ArrayList<String> senhas = new ArrayList<>();
	@Override
	public void validate(AbstractModel v) {
		for (Field f : v.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(ValidaSenha.class)) {
				f.setAccessible(true);
				try {
					senhas.add((String) f.get(v));
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					e1.printStackTrace();
				}
				try {
					if (f.get(v) == null) {
						v.addErro(f.getName(),f.getAnnotation(ValidaSenha.class).erro());
						return;
					}
					if ((f.get(v).toString().length() > f.getAnnotation(ValidaSenha.class).max()) 
					 	 || (f.get(v).toString().length() < f.getAnnotation(ValidaSenha.class).min())) 
					{
						v.addErro(f.getName(),f.getAnnotation(ValidaLength.class).erro());
					}
					int i = 0;
					int c = 0;
					while (i < f.get(v).toString().length())
					{	
						if ((f.get(v).toString().charAt(i) >= 'A') && (f.get(v).toString().charAt(i) <= 'Z'))
						{	
							c = c+1;
						}
						i = i+1;
					}
					if (c < f.getAnnotation(ValidaSenha.class).maiusculas()) {
						v.addErro(f.getName(), "por favor preencha uma letra maiscula");
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				f.setAccessible(false);
			}
		}
		if (!senhas.get(0).equals(senhas.get(1))) {
			v.addErro("senha", "as senhas devem ser iguais");
		}
	}

}
