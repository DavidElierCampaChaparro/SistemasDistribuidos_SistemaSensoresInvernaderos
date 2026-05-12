/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package frontend.ui;

import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import java.io.IOException;
import requests.authentication;

/**
 *
 * @author David Campa 245178
 */
public class main {

    public static void main(String[] args) {
        
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainScreen.class.getName());
        FlatDarkLaf.setup();
        UIManager.installLookAndFeel("Flat Dark","com.formdev.flatlaf.FlatDarkLaf");
//        UIManager.installLookAndFeel("Flat Darcula","com.formdev.flatlaf.FlatDarculaLaf");
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("FlatDarkLaf".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println("No se pudo poner un tema chilo");
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        
//        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//            System.out.println("Name: " + info.getName());
//            System.out.println("Class: " + info.getClassName());
//        }        
        

//        try { // Call Web Service Operation
//            wsc.Login_Service service = new wsc.Login_Service();
//            wsc.Login port = service.getLoginPort();
//            // TODO initialize WS operation arguments here
//            java.lang.String username = "admin";
//            java.lang.Integer password = Integer.valueOf(1234);
//            // TODO process result here
//            boolean result = port.auth(username, password);
//            System.out.println("Result = "+result);
//        } catch (Exception ex) {
//            // TODO handle custom exceptions here
//        }


        Login login = new Login();
        login.setVisible(true);
        
        
        
    }
    
}
