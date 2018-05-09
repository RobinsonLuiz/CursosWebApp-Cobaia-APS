package cobaia.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Controlador {
	
	String nome() default "";
}
