/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package expoferia.pagos.gestionpagos.gui.modulos;

import com.toedter.calendar.JDateChooser;
import expoferia.pagos.gestionpagos.dao.EmpleadoDAO;
import expoferia.pagos.gestionpagos.dao.EstudianteDAO;
import expoferia.pagos.gestionpagos.dao.RepresentanteDAO;
import expoferia.pagos.gestionpagos.dao.TipoPagoDAO;
import expoferia.pagos.gestionpagos.entidades.Empleado;
import expoferia.pagos.gestionpagos.entidades.Estudiante;
import expoferia.pagos.gestionpagos.entidades.Representante;
import expoferia.pagos.gestionpagos.entidades.TipoPago;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// AGREGADO
import java.text.Normalizer; // AGREGADO
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.Period;
//import java.util.Set;
//import java.util.stream.Collectors;
// AGREGADO

/**
 *
 * @author PC
 */
public class FormularioEntidad extends JFrame {
    String modulo;
    Map<String, JComponent> listaComponentes=new HashMap<>();

        public FormularioEntidad(String tipo, List<String> campos) {
            setTitle("Registrar " + tipo);
            modulo=tipo;
            initComponents(campos);
            // ... más inicialización según el tipo
            setSize(820, 415);
            setUndecorated(true);
            setVisible(true);
            setLocation(375, 210);
            //setAlwaysOnTop(true); // Para que se superponga siempre al frente

        }

        // CODIGO DE PRUEBQA AGREGADO - INICIO

    public boolean validarFormulario() {
        for (Map.Entry<String, JComponent> entry : listaComponentes.entrySet()) {
            String campoOriginal = entry.getKey();

            // Normaliza eliminando tildes, espacios y pone todo en minúsculas
            String campoNormalizado = Normalizer.normalize(campoOriginal, Normalizer.Form.NFD)
                    .replaceAll("[\\p{InCombiningDiacriticalMarks}\\s+]", "")
                    .toLowerCase();

            JComponent componente = entry.getValue();
            String valor = "";

            if (componente instanceof JTextField) {
                valor = ((JTextField) componente).getText().trim();
            } else if (componente instanceof JPasswordField) {
                valor = new String(((JPasswordField) componente).getPassword()).trim();
            } else if (componente instanceof JComboBox) {
                Object seleccionado = ((JComboBox<?>) componente).getSelectedItem();
                valor = (seleccionado != null) ? seleccionado.toString().trim() : "";
            } else if (componente instanceof JDateChooser) {
                java.util.Date fecha = ((JDateChooser) componente).getDate();
                valor = (fecha != null) ? new SimpleDateFormat("yyyy/MM/dd").format(fecha) : "";
            }

            if (valor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo \"" + campoOriginal + "\" no puede estar vacío.");
                return false;
            }

            if (campoNormalizado.contains("cedula") && !valor.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "La cédula debe contener solo números.");
                return false;
            }

            if (campoNormalizado.contains("telefono")) {
                String valorNumerico = valor.replaceAll("\\D", "");
                System.out.println("🧪 Teléfono → Original: '" + valor + "' | Limpio: '" + valorNumerico +
                        "' | Coincide con 11 dígitos: " + valorNumerico.matches("\\d{11}"));

                if (!valorNumerico.matches("\\d{11}")) {
                    JOptionPane.showMessageDialog(this, "El número de teléfono debe tener exactamente 11 dígitos.");
                    return false;
                }
            }

            if (campoNormalizado.contains("correo")) {
                System.out.println("🧪 Correo → Valor: '" + valor + "' | ¿Válido? " +
                        valor.matches("^[\\p{L}0-9._%+-]+@(gmail|hotmail|outlook|yahoo)\\.com$"));

                if (!valor.matches("^[\\p{L}0-9._%+-]+@(gmail|hotmail|outlook|yahoo)\\.com$")) {
                    JOptionPane.showMessageDialog(this, "Solo se permiten correos de Gmail, Hotmail, Outlook o Yahoo terminados en .com.");
                    return false;
                }
            }

            if (campoNormalizado.contains("fecha") &&
                    !valor.matches("\\d{4}/\\d{2}/\\d{2}")) {
                JOptionPane.showMessageDialog(this, "La fecha debe tener el formato yyyy/MM/dd.");
                return false;
            }

            System.out.println("⏳ Campo original: " + campoOriginal + " → Valor: " + valor);
            System.out.println("🔎 Campo normalizado: " + campoNormalizado);
        }

        // 🔐 Validación de coincidencia entre contraseña y confirmación
        String contrasena = null;
        String confirmar = null;

        for (Map.Entry<String, JComponent> entry : listaComponentes.entrySet()) {
            String nombre = Normalizer.normalize(entry.getKey(), Normalizer.Form.NFD)
                    .replaceAll("[\\p{InCombiningDiacriticalMarks}\\s+]", "")
                    .toLowerCase();

            if (nombre.contains("contrasena") && !nombre.contains("confirmar")) {
                if (entry.getValue() instanceof JPasswordField) {
                    contrasena = new String(((JPasswordField) entry.getValue()).getPassword()).trim();
                }
            }

            if (nombre.contains("confirmarcontrasena")) {
                if (entry.getValue() instanceof JPasswordField) {
                    confirmar = new String(((JPasswordField) entry.getValue()).getPassword()).trim();
                }
            }
        }

        if (contrasena != null && confirmar != null && !contrasena.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return false;
        }

        return true;
    }

    // CODIGO DE PRUEBA AGREGADO - FIN


    private void initComponents(List<String> campos) {
        JPanel panelComponentes = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panelComponentes.setBackground(Color.WHITE);

        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes entre elementos
        gbc.fill = GridBagConstraints.NONE; // Expansión horizontal
        gbc.weightx = 1.0; // Permitir que los elementos crezcan
        gbc.anchor = GridBagConstraints.CENTER; // Alineación a la izquierda

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setLayout(new FlowLayout(FlowLayout.LEFT));

        int fila = 1;
        JLabel titulo = new JLabel("Registrar " + modulo);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18)); // 🔥 Asegurar negrita y tamaño
        titulo.setForeground(new Color(10, 72, 162)); // 🔥 Color azul
        titulo.setHorizontalAlignment(SwingConstants.LEFT);// 🔥 Asegurar alineación izquierda
        panelTitulo.add(titulo);
        add(panelTitulo, BorderLayout.NORTH);

        for (int i = 0; i < campos.size(); i += 2) { // 🔥 Procesar dos campos por fila
            // 🔹 Primera columna (Label arriba)
            gbc.gridx = 0; gbc.gridy = fila;
            panelComponentes.add(new JLabel(campos.get(i) + ":"), gbc);

            // 🔹 Segunda columna (Label arriba)
            if (i + 1 < campos.size()) { // Para evitar errores si hay un número impar de campos
                gbc.gridx = 1; gbc.gridy = fila;
                panelComponentes.add(new JLabel(campos.get(i + 1) + ":"), gbc);
            }

            // 🔹 Primera columna (Componente abajo)
            gbc.gridx = 0; gbc.gridy = fila + 1;
            gbc.gridwidth = 1; // Cada componente ocupa solo una columna
            panelComponentes.add(getInputComponent(campos.get(i)), gbc);

            // 🔹 Segunda columna (Componente abajo)
            if (i + 1 < campos.size()) {
                gbc.gridx = 1; gbc.gridy = fila + 1;
                panelComponentes.add(getInputComponent(campos.get(i + 1)), gbc);
            }

            fila += 2; // 🔥 Avanzar dos filas para la siguiente pareja de componentes
        }

        add(panelComponentes);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBackground(Color.WHITE);

        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnGuardar = new JButton("Guardar");

        // 🔥 Agregar eventos a los botones
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnGuardar.addActionListener(e -> guardarDatos());

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Método para devolver el componente correcto según el tipo de campo
    private JComponent getInputComponent(String campo) {
        JComponent inputField;

        switch (campo) {

            case "Nombres", "Apellidos", "Cedula", "Cedula Representante", "Teléfono", "Correo", "Dirección", "Concepto", "Costo":
                inputField = new JTextField(15);
                break;

            case "Rol":
                JComboBox<String> roles = new JComboBox<>();
                roles.addItem("Cajero");
                roles.addItem("Admin");
                inputField = roles;
                break;

            case "Categoría":
                JComboBox<String> categorias = new JComboBox<>();
                categorias.addItem("Mensualidad");
                categorias.addItem("Inscripción");
                categorias.addItem("Curso");
                categorias.addItem("Cuota Especial");
                inputField = categorias;
                break;

            case "Contraseña", "Confirmar contraseña":
                inputField = new JPasswordField(15);
                break;

            case "Fecha de Nacimiento":
                JDateChooser dateChooser = new JDateChooser();
                dateChooser.setDateFormatString("yyyy/MM/dd"); // Formato de fecha
                inputField=dateChooser;
                break;

            default:
                inputField = new JTextField(15);

        }

        // 🔥 Establecer tamaño máximo para evitar desbordamientos
        inputField.setPreferredSize(new Dimension(175, 25)); // Ajusta según el ancho disponible
        inputField.setMaximumSize(new Dimension(175, 25));// Fija un tamaño máximo
        inputField.setMinimumSize(new Dimension(175, 25)); // 🔥 Tamaño mínimo
        listaComponentes.put(campo, inputField);
        return inputField;
    }

    // Obtener los valores del Formulario
    public Map<String, String> obtenerValores() {
        Map<String, String> valores = new HashMap<>();

        for (Map.Entry<String, JComponent> entry : listaComponentes.entrySet()) {
            String campo = entry.getKey();
            JComponent componente = entry.getValue();
            String valor = "";

            if (componente instanceof JTextField) {
                valor = ((JTextField) componente).getText();
            } else if (componente instanceof JPasswordField) {
                valor = new String(((JPasswordField) componente).getPassword()); // Obtener contraseña
            } else if (componente instanceof JComboBox) {
                valor = ((JComboBox<?>) componente).getSelectedItem().toString();
            } else if (componente instanceof JDateChooser) {
                java.util.Date fechaUtil = ((JDateChooser) componente).getDate();
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); // Formato compatible con SQL
                valor = formato.format(fechaUtil); // Convertir la fecha a `String`

            }

            valores.put(campo, valor);
        }

        return valores;
    }

    // Enviar los valores a su DAO agregar respectivo
    public boolean añadirRegistro(Map<String, String> listaValores) {

            //codigo agreado
        if (listaValores == null || listaValores.isEmpty()) {
            System.out.println("⚠️ Los datos llegaron vacíos o inválidos.");
            return false;
        } //codigo agregado

            boolean valor = false;

        if (modulo.equals("Tipos de Pagos")) {
            TipoPagoDAO tpdao = new TipoPagoDAO();
            TipoPago tipoPago = new TipoPago();

            for (Map.Entry<String, String> entry : listaValores.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case "Concepto":
                        tipoPago.setConcepto(value); // Es String
                        break;
                    case "Costo":
                        tipoPago.setCosto(Double.parseDouble(value)); // 🔥 Convertir a Double
                        break;
                    case "Categoría":
                        tipoPago.setCategoria(value); // 🔥 Convertir a Date
                        break;

                }
                tipoPago.setEstado(true);

            }
            valor = tpdao.agregar(tipoPago); // 🔥 Guardar en BD
            return valor;
        }

        if (modulo.equals("Estudiantes")) {
            EstudianteDAO esdao = new EstudianteDAO();
            Estudiante estudiante = new Estudiante();

            for (Map.Entry<String, String> entry : listaValores.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case "Cedula Representante":
                        estudiante.setCedulaRep(value);
                        break;
                    case "Nombres":
                        String[] nombres=value.split(" ");
                        estudiante.setNombre1(nombres[0]);
                        estudiante.setNombre2(nombres[1]);
                        break;
                    case "Apellidos":
                        String[] apellidos=value.split(" ");
                        estudiante.setApellido1(apellidos[0]);
                        estudiante.setApellido2(apellidos[1]);
                        break;
                    case "Teléfono":
                        estudiante.setTelefono(value);
                        break;
                    case "Fecha de Nacimiento":
                        estudiante.setFechaN(Date.valueOf(value));
                        break;
                    case "Dirección":
                        estudiante.setDireccion(value);
                        break;
                }
                estudiante.setEstado(true);
            }

            valor = esdao.agregar(estudiante);
            return valor;
        }

        if (modulo.equals("Representantes")) {
            RepresentanteDAO rdao = new RepresentanteDAO();
            Representante representante = new Representante();

            for (Map.Entry<String, String> entry : listaValores.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case "Cedula":
                        representante.setCedula(value);
                        break;
                    case "Nombres":
                        String[] nombres=value.split(" ");
                        representante.setNombre1(nombres[0]);
                        representante.setNombre2(nombres[1]);
                        break;
                    case "Apellidos":
                        String[] apellidos=value.split(" ");
                        representante.setApellido1(apellidos[0]);
                        representante.setApellido2(apellidos[1]);
                        break;
                    case "Correo":
                        representante.setCorreo(value);
                        break;
                    case "Teléfono":
                        representante.setTelefono(value);
                        break;
                    case "Fecha de Nacimiento":
                        representante.setFechaN(Date.valueOf(value));
                        break;
                    case "Dirección":
                        representante.setDireccion(value);
                        break;
                }
                representante.setEstado(true);
            }

            valor = rdao.agregar(representante);
            return valor;
        }

        if (modulo.equals("Empleados")) {
            EmpleadoDAO rdao = new EmpleadoDAO();
            Empleado empleado = new Empleado();

            for (Map.Entry<String, String> entry : listaValores.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case "Cedula":
                        empleado.setCedula(value);
                        break;
                    case "Nombres":
                        String[] nombres=value.split(" ");
                        empleado.setNombre1(nombres[0]);
                        empleado.setNombre2(nombres[1]);
                        break;
                    case "Apellidos":
                        String[] apellidos=value.split(" ");
                        empleado.setApellido1(apellidos[0]);
                        empleado.setApellido2(apellidos[1]);
                        break;
                    case "Teléfono":
                        empleado.setTelefono(value);
                        break;
                    case "Correo":
                        empleado.setCorreo(value);
                        break;
                    case "Fecha de Nacimiento":
                        empleado.setFechaN(Date.valueOf(value));
                        break;
                    case "Contraseña":
                        empleado.setContrasena(value);
                        break;
                    case "Dirección":
                        empleado.setDireccion(value);
                        break;
                    case "Rol":
                        empleado.setRol(value.equals("Admin"));
                        break;
                }
                empleado.setEstado(true);
            }

            valor = rdao.agregar(empleado);
            return valor;
        }

        return valor;
    }

    // Método para limpiar los campos
    private void limpiarCampos() {
        for (Map.Entry<String, JComponent> entry : listaComponentes.entrySet()) {
            JComponent componente = entry.getValue();
            if (componente instanceof JTextField) {
                ((JTextField) componente).setText("");
            } else if (componente instanceof JPasswordField) {
                ((JPasswordField) componente).setText("");
            } else if (componente instanceof JComboBox) {
                ((JComboBox<?>) componente).setSelectedIndex(0);
            } else if (componente instanceof  JDateChooser) {
                ((JDateChooser) componente).setDate(null);
            }
        }
    }

    // Método para guardar los datos
    private void guardarDatos() {
        if (validarFormulario()) {
            Map<String, String> datos = obtenerValores();
            boolean guardado = añadirRegistro(datos);

            if (guardado) {
                JOptionPane.showMessageDialog(this, "¡Los datos se guardaron exitosamente!");
                limpiarCampos(); // Si tienes este método, para reiniciar el formulario
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un problema al guardar los datos.");
            }
        }
    }

    //private void guardarDatos() {
        //Map<String, String> datos = obtenerValores();
        //añadirRegistro(datos);
    //} (este era el codigo original)

}