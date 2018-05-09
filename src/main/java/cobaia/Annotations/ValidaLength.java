package cobaia.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import cobaia.Validations.LengthValidator;

@Constraint(validateby = LengthValidator.class)
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidaLength {
	int min() default 3;
	int max() default 20;
	String erro() default "o tamanho da palavra inserida não atende as requisitos";

}
