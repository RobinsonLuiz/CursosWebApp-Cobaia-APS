package cobaia.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cobaia.Validations.EmailValidator;

@Constraint(validateby = EmailValidator.class)
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})

public @interface ValidaEmail {
	
	String verificar() default "[a-z._-]+@[a-z.]+";
	int vazio() default 0;
	String erro() default "valor inserido não atende as requisitos";
}
