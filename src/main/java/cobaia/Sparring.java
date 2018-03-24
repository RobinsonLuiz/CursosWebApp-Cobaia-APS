package cobaia;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class Sparring {

  public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    System.out.println(UUID.randomUUID().toString().split("-")[0].length());    
    String md5 = DigestUtils.md5Hex("test");
    System.out.println(md5);
    System.out.println("marcio.torres@riogrande.ifrs.edu.br".matches("[\\w._]+@\\w+(\\.\\w+)+"));
  }

}
