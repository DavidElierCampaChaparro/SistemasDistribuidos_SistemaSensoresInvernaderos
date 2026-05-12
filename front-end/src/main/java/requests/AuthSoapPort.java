package requests;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://mycompany.com/auth")
public interface AuthSoapPort {

    @WebMethod
    AuthResponse autenticar(String username, String password);
}