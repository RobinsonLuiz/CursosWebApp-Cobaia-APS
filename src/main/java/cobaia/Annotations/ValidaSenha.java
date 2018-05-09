package cobaia.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import cobaia.Validations.SenhaValidator;

@Constraint(validateby = SenhaValidator.class)
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})

public @interface ValidaSenha {
	int min() default 3;
	int max() default 20;
	int maiusculas() default 1;
	String erro() default "o tamanho da palavra inserida não atende as requisitos";

}
