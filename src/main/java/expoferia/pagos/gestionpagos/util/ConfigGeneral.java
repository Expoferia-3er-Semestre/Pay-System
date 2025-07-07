package expoferia.pagos.gestionpagos.util;

import java.io.*;
import java.util.Properties;

public class ConfigGeneral {

    private final Properties config = new Properties();
    private final String RUTA = "src/main/resources/config.properties";

    public ConfigGeneral() {
        cargar();
    }

    public void cargar() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("⚠️ No se pudo encontrar el archivo config.properties");
                return;
            }
            config.load(input);
        } catch (IOException e) {
            System.out.println("Error al cargar configuración: " + e.getMessage());
        }
    }


    public String get(String clave) {
        return config.getProperty(clave);
    }

    public double getDouble(String clave) {
        String valor = config.getProperty(clave);

        if (valor == null || valor.trim().isEmpty()) {
            System.out.println("⚠️ Advertencia: la clave \"" + clave + "\" no existe o está vacía.");
            return 0.0; // o algún valor por defecto
        }

        try {
            return Double.parseDouble(valor.trim());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Error: el valor de \"" + clave + "\" no es un número válido → \"" + valor + "\"");
            return 0.0;
        }
    }


    public void set(String clave, String valor) {
        config.setProperty(clave, valor);
        guardar();
    }

    private void guardar() {
        try (OutputStream output = new FileOutputStream(RUTA)) {
            config.store(output, "Valores Generales del Sistema");
        } catch (IOException e) {
            System.out.println("Error al guardar configuración: " + e.getMessage());
        }
    }
}

