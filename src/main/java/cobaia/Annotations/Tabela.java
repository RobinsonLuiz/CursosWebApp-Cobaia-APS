package cobaia.Annotations;
import java.lang.annotation.*;

@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Tabela {
	
	String nome() default "";
}
