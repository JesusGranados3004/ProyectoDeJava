/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package cine.FrmInternas;
import cine.Clases.Funcion;
import cine.Clases.Salas;
import cine.Clases.Peliculas;
import cine.Clases.ConexionBD;
import java.awt.Color;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 57300
 */
public class JIFrmFunciones extends javax.swing.JInternalFrame {

    /**
     * Creates new form JIFrmFunciones
     */
    Salas sala;
    Peliculas pelicula;
    ConexionBD conn;
    public final ArrayList<Integer> idsFuncion = new ArrayList<>();
    public final ArrayList<Integer> idsSala    = new ArrayList<>();
    String [] encabezado = {"Titulo","Sala","Inicio","Fin","Precio"};
    
    public JIFrmFunciones() {
        initComponents();
        conn = new ConexionBD();
        LlenarComboPeliculas(ComboPelicula, pelicula);
        LlenarComboSala(ComboSala, sala);
    }
    
    // aqui se ejecuta el metodo que consulta los datos necesaruos para 
    // llenar la tabla y posterior se llena la tabla
    public void mostrarInfo(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(encabezado);
        int peliculaId = 0;
        LlenarTablaFuncion(TableFuncion, modelo,peliculaId);
    }
    
    // esto es para que el usuario tenga encuenta de como ingresar la hora
    // de inicio de la funcion
    public void AgregarGuia(){
        if (TxtInicio.getText().isEmpty()) {
            TxtInicio.setForeground(Color.GRAY);
            TxtInicio.setText("HH:mm:ss");
        }
    }
    
    // y esta posteriormente es para quitar la guia ya sea cuando unda 
    // para ingresar los datos
    public void QuitarGuia(){
        if (TxtInicio.getText().equals("HH:mm:ss")) {
            TxtInicio.setText("");
            TxtInicio.setForeground(Color.BLACK);
        }
    }
    
    //aqui se le mandan ya los datos recolectados a la base de datos para 
    //que guarde la funcion que se quiere programar 
    public void AgrgarFuncion(){
        Date fecha = DateFecha.getDate();
        if(ComboPelicula.getSelectedIndex() > 0 && ComboSala.getSelectedIndex() > 0 
            && !TxtInicio.getText().isEmpty() && !TxtPrecio.getText().isEmpty())
        {
            if(!TxtInicio.getText().equals("HH:mm:ss")){
                Peliculas pelicula = (Peliculas) ComboPelicula.getSelectedItem();
                int idpelicula = pelicula.getId();
                Salas sala = (Salas) ComboSala.getSelectedItem();
                int idsala = sala.getId();
                String inicio = TxtInicio.getText();
                String Fecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha);
                String Fechainicio = Fecha + " " + inicio;
                double precio = Double.parseDouble(TxtPrecio.getText());

                try {
                    Connection con = conn.Conexion();

                    String sql = "call GuardarFuncion(?, ?, ?, ?, ?)";
                    Timestamp ts = Timestamp.valueOf(Fechainicio);
                    
                    CallableStatement cstmt = con.prepareCall(sql);
                    cstmt.setInt(1, idpelicula);
                    cstmt.setInt(2, idsala);
                    cstmt.setTimestamp(3, ts);
                    cstmt.setDouble(4, precio);
                    cstmt.registerOutParameter(5, Types.INTEGER);


                     cstmt.execute();
                     int idFuncion = cstmt.getInt(5);
                     
                     if (idFuncion != 0) {
                        JOptionPane.showMessageDialog(null, "Función guardada correctamente (ID: " + idFuncion + ")");
                     } else {
                         JOptionPane.showMessageDialog(null, "Ya existe una función en ese horario o no cumple con los 20 minutos de separación");
                     }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al guardar función: " + e.getMessage());
                }
            }else{
                JOptionPane.showMessageDialog(rootPane, "Ingrese la hora en formato HH:mm:ss");
            }
        }else
        {
            JOptionPane.showMessageDialog(rootPane, "Faltan parámetros");
        }
    }
    
    
    // aqui se llena el combo con peliula para asi guardar todos sus atributos
    // y la mostramos con el nombre para la seleccion de la pelicula que se
    // quiere programar
    public void LlenarComboPeliculas(JComboBox Combo,Peliculas pelicula){
        try {
            Connection con = conn.Conexion();
            String sql = "call listar_peliculas_combo()";
            
            CallableStatement  cstmt  = con.prepareCall(sql);
            ResultSet resultado = cstmt .executeQuery();
            
            Combo.removeAllItems();
            Peliculas placeholder = new Peliculas(0, "Seleccione una película", "", "", "", "", "", "");
            Combo.addItem(placeholder);
            
            while(resultado.next()){
                int id = resultado.getInt("pelicula_id");
                String titulo = resultado.getString("titulo");
                String genero = resultado.getString("nombre_genero");
                String duracion = resultado.getString("duracion_minutes");
                String estreno = resultado.getString("fecha_estreno");
                String director = resultado.getString("director");
                String sinopsis = resultado.getString("sinopsis");
                String idioma = resultado.getString("idioma");
                pelicula = new Peliculas(id, titulo, genero, duracion, estreno, director, sinopsis, idioma);
                Combo.addItem(pelicula);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la pelicula" + e);
        }
    }
    
    
    // aqui es lo mismo pero en ves de las peliculas es la sala
    public void LlenarComboSala(JComboBox Combo,Salas sala){
        try {
            Connection con = conn.Conexion();
            String sql = "call listar_salas()";
            
            CallableStatement cstmt  = con.prepareCall(sql);
            ResultSet resultado = cstmt.executeQuery();
            
            Combo.removeAllItems();
            Salas placeholder = new Salas(0,"Seleccione una sala",0);
            Combo.addItem(placeholder);
            
            while(resultado.next()){
                int id = resultado.getInt("sala_id");
                String nombre = resultado.getString("nombre");
                int cantidad = resultado.getInt("capacidad");
                sala = new Salas (id, nombre, cantidad);
                Combo.addItem(sala);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la pelicula" + e);
        }
    }
    
    
    // aqui por medio de una validacion que compruebe si la variable esta vacia o no se consultan las funciones programadas
    // y mostramos en la tabla
    public void LlenarTablaFuncion(JTable tabla, DefaultTableModel modelo, int peliculaId) {
        modelo.setRowCount(0);          
        idsFuncion.clear();
        idsSala.clear();

        try {
            Connection con = conn.Conexion();
            String sql = "call ObtenerFunciones(?)";
            CallableStatement cs = con.prepareCall(sql);
            
            if (peliculaId == 0){
                cs.setNull(1, Types.INTEGER);
            }else{
                cs.setInt(1, peliculaId);
            }

            ResultSet rs = cs.executeQuery();
            
            while (rs.next()) {
                idsFuncion.add(rs.getInt("funcion_id"));
                idsSala.add(rs.getInt("sala_id"));

                modelo.addRow(new Object[]{
                    rs.getString("titulo"),
                    rs.getString("sala"),
                    rs.getString("inicio"),
                    rs.getString("fin"),
                    rs.getString("precio_base")
                });
            }
            TableFuncion.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar funciones: " + e);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        TxtPrecio = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableFuncion = new javax.swing.JTable();
        BtnAgregarFuncion = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        DateFecha = new com.toedter.calendar.JDateChooser();
        ComboPelicula = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        ComboSala = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        TxtInicio = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jPanel1.setBackground(new java.awt.Color(139, 220, 224));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Precio:");

        TxtPrecio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        TableFuncion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        TableFuncion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(TableFuncion);

        BtnAgregarFuncion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        BtnAgregarFuncion.setText("Agregar");
        BtnAgregarFuncion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAgregarFuncionActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Pelicula:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setText("Agregar Funcion");
        jLabel6.setToolTipText("");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Sala:");

        ComboPelicula.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ComboPelicula.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Hora inicio");

        ComboSala.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ComboSala.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText(" fecha de Inicio:");

        TxtInicio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        TxtInicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TxtInicioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                TxtInicioFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 26, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(ComboPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(16, 16, 16))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TxtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ComboSala, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)
                                        .addComponent(TxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(41, 41, 41)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(DateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(BtnAgregarFuncion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(253, 253, 253)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel6)
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ComboPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(ComboSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(TxtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(BtnAgregarFuncion)))
                    .addComponent(DateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtInicioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TxtInicioFocusGained
        // TODO add your handling code here:
        QuitarGuia();
    }//GEN-LAST:event_TxtInicioFocusGained

    private void TxtInicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TxtInicioFocusLost
        // TODO add your handling code here:
        AgregarGuia();
    }//GEN-LAST:event_TxtInicioFocusLost
    // para ejecutar los metodos que guardan y muestran la informacion de la funcion
    private void BtnAgregarFuncionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAgregarFuncionActionPerformed
        // TODO add your handling code here:
        AgrgarFuncion();
        mostrarInfo();
    }//GEN-LAST:event_BtnAgregarFuncionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAgregarFuncion;
    private javax.swing.JComboBox<String> ComboPelicula;
    private javax.swing.JComboBox<String> ComboSala;
    private com.toedter.calendar.JDateChooser DateFecha;
    private javax.swing.JTable TableFuncion;
    private javax.swing.JTextField TxtInicio;
    private javax.swing.JTextField TxtPrecio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
