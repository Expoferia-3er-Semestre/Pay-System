/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expoferia.pagos.gestionpagos.util;

import expoferia.pagos.gestionpagos.gui.frmlogin.*;
import javax.swing.JTextField;
/**
 *
 * @author Suglin
 */
public class Validar {
    
    public boolean validarTxt(JTextField txtBox){
        if (!txtBox.getText().isBlank()) {
            return true;
        } else return false;
    }
    
    
}
