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
import expoferia.pagos.gestionpagos.gui.HomeAdmin;
import expoferia.pagos.gestionpagos.gui.PanelRound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC
 */
public class FormularioEntidad extends JPanel {
    String modulo;
    Map<String, JComponent> listaComponentes=new HashMap<>();

        public FormularioEntidad(String tipo, List<String> campos) {
            modulo=tipo;
            initComponents(campos);
            setSize(810, 600);
            // ... más inicialización según el tipo
            setVisible(true);
            //setAlwaysOnTop(true); // Para que se superponga siempre al frente

        }

        // CODIGO DE PRUEBQA AGREGADO

    public boolean validarFormulario() {
        for (Map.Entry<String, JComponent> entry : listaComponentes.entrySet()) {
            String campo = entry.getKey().toLowerCase();
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
                JOptionPane.showMessageDialog(this, "El campo \"" + entry.getKey() + "\" no puede estar vacío.");
                return false;
            }

            if (campo.contains("cedula") && !valor.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "La cédula debe contener solo números.");
                return false;
            }

            if (campo.replaceAll("\\s+", "").toLowerCase().contains("telefono") &&
                    !valor.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(this, "El número de teléfono debe tener exactamente 11 dígitos.");
                return false;
            }

            if (campo.contains("correo") &&
                    !valor.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|ve|org)$")) {
                JOptionPane.showMessageDialog(this, "El correo debe terminar en .com, .ve o .org.");
                return false;
            }

            if (campo.contains("fecha") && !valor.matches("\\d{4}/\\d{2}/\\d{2}")) {
                JOptionPane.showMessageDialog(this, "La fecha debe tener el formato yyyy/MM/dd.");
                return false;
            }
        }

        return true;
    }

    // CODIGO DE PRUEBA AGREGADO


    private void initComponents(List<String> campos) {
        PanelRound panelContenedor = new PanelRound(); // 🔥 Panel blanco que contiene todo
        panelContenedor.setBackground(new java.awt.Color(255, 255, 255));
        panelContenedor.setRoundBottomLeft(30);
        panelContenedor.setRoundBottomRight(30);
        panelContenedor.setRoundTopLeft(30);
        panelContenedor.setRoundTopRight(30);
        panelContenedor.setLayout(new java.awt.BorderLayout());
        panelContenedor.setPreferredSize(new Dimension(810, 400));

        JPanel panelComponentes = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panelComponentes.setBackground(Color.WHITE);

        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes entre elementos
        gbc.fill = GridBagConstraints.NONE; // Expansión horizontal
        gbc.weightx = 1.0; // Permitir que los elementos crezcan
        gbc.anchor = GridBagConstraints.CENTER; // Alineación a la izquierda

        PanelRound panelTitulo = new PanelRound();
        panelTitulo.setBackground(new java.awt.Color(255, 255, 255));
        panelTitulo.setRoundTopLeft(30);
        panelTitulo.setRoundTopRight(30);
        panelTitulo.setLayout(new java.awt.FlowLayout(FlowLayout.LEFT));

        JLabel btnRetroceder = new JLabel();
        btnRetroceder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetroceder.setIcon(new ImageIcon(getClass().getResource("/imagenes/retroceder.png")));

        int fila = 1;
        JLabel titulo = new JLabel("Registrar " + modulo);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18)); // 🔥 Asegurar negrita y tamaño
        titulo.setForeground(new Color(10, 72, 162)); // 🔥 Color azul
        titulo.setHorizontalAlignment(SwingConstants.LEFT); // 🔥 Asegurar alineación izquierda
        panelTitulo.add(new JLabel("   "));
        panelTitulo.add(btnRetroceder);
        panelTitulo.add(new JLabel("  "));
        panelTitulo.add(titulo);

        panelContenedor.add(panelTitulo, BorderLayout.NORTH); // 🔥 Agregar título

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

        panelContenedor.add(panelComponentes, BorderLayout.CENTER); // 🔥 Agregar componentes

        PanelRound panelBotones = new PanelRound();
        panelBotones.setBackground(new java.awt.Color(255, 255, 255));
        panelBotones.setRoundBottomLeft(30);
        panelBotones.setRoundBottomRight(30);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnGuardar = new JButton("Guardar");
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 🔥 Agregar eventos a los botones
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnGuardar.addActionListener(e -> guardarDatos());
        btnRetroceder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("hola");
                PanelDefault panelDefault;
                if (modulo.equals("Empleados")) {
                    panelDefault=new PanelDefault("ID Cedula Nombres Apellidos Rol Detalles Acciones",
                            "Empleados");
                    HomeAdmin.panelCambiante.add(panelDefault, "Empleados");
                    HomeAdmin.card.show(HomeAdmin.panelCambiante, modulo);
                    HomeAdmin.panelCambiante.revalidate();
                    HomeAdmin.panelCambiante.repaint();
                } else if (modulo.equals("Tipos de Pagos")) {
                    panelDefault= new PanelDefault("ID Concepto Categoría Costo Estado Acciones",
                            "Tipos de Pagos");
                    HomeAdmin.panelCambiante.add(panelDefault, "Tipos de Pagos");
                    HomeAdmin.card.show(HomeAdmin.panelCambiante, modulo);
                    HomeAdmin.panelCambiante.revalidate();
                    HomeAdmin.panelCambiante.repaint();
                } else if (modulo.equals("Estudiantes")) {
                    panelDefault= new PanelDefault("ID Cedula Nombres Apellidos Detalles Acciones",
                            "Estudiantes");
                    HomeAdmin.panelCambiante.add(panelDefault, "Estudiantes");
                    HomeAdmin.card.show(HomeAdmin.panelCambiante, modulo);
                    HomeAdmin.panelCambiante.revalidate();
                    HomeAdmin.panelCambiante.repaint();
                } else if (modulo.equals("Representantes")) {
                    panelDefault= new PanelDefault("ID Cedula Nombres Apellidos Detalles Acciones",
                            "Representantes");
                    HomeAdmin.panelCambiante.add(panelDefault, "Representantes");
                    HomeAdmin.card.show(HomeAdmin.panelCambiante, modulo);
                    HomeAdmin.panelCambiante.revalidate();
                    HomeAdmin.panelCambiante.repaint();
                }

            }
        });

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelContenedor.add(panelBotones, BorderLayout.SOUTH); // 🔥 Agregar botones

        add(panelContenedor); // 🔥 Agregar el contenedor principal
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
            añadirRegistro(datos);
        }
    }

    //private void guardarDatos() {
        //Map<String, String> datos = obtenerValores();
        //añadirRegistro(datos);
    //} (este era el codigo original)

}