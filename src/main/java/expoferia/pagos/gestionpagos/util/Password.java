package expoferia.pagos.gestionpagos.util;

import org.mindrot.jbcrypt.BCrypt;

public class Password {

    private Password() {}

    public static String hashContrasena(String contrasenaP) {
        return BCrypt.hashpw(contrasenaP, BCrypt.gensalt(12));
    }

    public static boolean verificarContrasena(String contrasenaP, String contrasenaH) {
        try {
            return BCrypt.checkpw(contrasenaP, contrasenaH);
        } catch (Exception e) {
            System.err.println("Error al verificar la contraseña: formato de hash invalido."
                    +e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        String contrasenaH=hashContrasena("admin123");
        if (verificarContrasena("admin123", contrasenaH)) {
            System.out.println("sesión iniciada");
        } else {
            System.out.println("error");
        }
        
    }
    
}
