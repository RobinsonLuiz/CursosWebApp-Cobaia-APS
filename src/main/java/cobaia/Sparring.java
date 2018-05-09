package cobaia;


import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import cobaia.Modelo.Area;
import cobaia.Modelo.Curso;
import cobaia.Modelo.Usuario;
import cobaia.Validations.EmailValidator;
import cobaia.view.helper.MoedaFilters;
import cobaia.Validations.ConstructorValidator;

public class Sparring {

  public static void main(String[] args) throws Exception {
    /* cria a area, comentado por que ele fica criando muitas
    Area a = new Area();
    a.setNome("amore");
    System.out.println(a.isPersistente());
    a.save();
    
    //delete uma area
    Area a1 = new Area();
    a1.setId(2);
    GenericDAO dao = new GenericDAO();
    dao.delete(a1);
	System.out.println(a1.getNome());
 	
    //seleciona todos
    
    AreaDAO teste = new AreaDAO();
    IList<Area> areas = teste.seleciona();
    for (int i = 0; i < areas.count(); i++) {
		System.out.println(areas.get(i).getNome());
	}
	//cria um novo curso GENERICDAO
    Area a1 = new Area();
    a1.setId(3);
    Curso c1 = new Curso();
    c1.setArea(a1);
    c1.setNome("TADS TOTAL 2");
    c1.setId_area(a1.getId());
    c1.setResumo("nois que voa");
    c1.setVaga(20);
    c1.setData_inicio(new Date(System.currentTimeMillis()));
    c1.setData_termino(new Date(System.currentTimeMillis()));
    c1.setCarga_horaria(20);
    c1.setDias("segunda");
    c1.setHorario_inicio(new Time(System.currentTimeMillis()));
    c1.setHorario_termino(new Time(System.currentTimeMillis()));
    c1.save();
    

    Curso c2 = new Curso();
    try {
		c2.load(2);
		System.out.println(c2.getNome());
	} catch (ObjectNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
    /*
    Usuario c = new Usuario();
    c.setEmail("robinsontads@outlook.com");
    c.setNome("robinson");
    c.setSenha("325140A");
    c.setStatus(Status.ATIVADO);
    c.setVerificaSenha("325140A");
    c.save();
    
    /*
    Usuario c = new Usuario();
    try {
		c.load(2);
	} catch (ObjectNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    if (c.isPersistente()) c.delete();
    
    //update pelo GENERICDAO
    GenericDAO generico = new GenericDAO();
    Area a = new Area();
    a.setNome("TADS");
    generico.persiste(a);
    a.setNome("Robinilson");
    generico.persiste(a);
    */
    //Usuario u = new Usuario().load(1);
    //Curso c = new Curso().load(1);
    //GenericDAO dao = new GenericDAO();
    //u.setNome("Joilson");
    //dao.persiste(u);
  }
}
