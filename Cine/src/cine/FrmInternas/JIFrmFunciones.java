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
    String [] encabezado = {"ID","Titulo","Sala","Inicio","Fin","Precio"};
    
    public JIFrmFunciones() {
        initComponents();
        conn = new ConexionBD();
        LlenarComboPeliculas(ComboPelicula, pelicula);
        LlenarComboSala(ComboSala, sala);
    }
    
    
    public void mostrarInfo(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(encabezado);
        int peliculaId = 0;
        LlenarTablaFuncion(TableFuncion, modelo,peliculaId);
    }
    
    public void AgregarGuia(){
        if (TxtInicio.getText().isEmpty()) {
            TxtInicio.setForeground(Color.GRAY);
            TxtInicio.setText("HH:mm:ss");
        }
    }
    
    public void QuitarGuia(){
        if (TxtInicio.getText().equals("HH:mm:ss")) {
            TxtInicio.setText("");
            TxtInicio.setForeground(Color.BLACK);
        }
    }
    
    
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ComboPelicula = new javax.swing.JComboBox<>();
        ComboSala = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        TxtInicio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TxtPrecio = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableFuncion = new javax.swing.JTable();
        BtnAgregarFuncion = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        DateFecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jLabel1.setText("Pelicula:");

        jLabel2.setText("Sala:");

        ComboPelicula.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ComboSala.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText(" fecha de Inicio:");

        TxtInicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TxtInicioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                TxtInicioFocusLost(evt);
            }
        });

        jLabel5.setText("Precio:");

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

        BtnAgregarFuncion.setText("Agregar");
        BtnAgregarFuncion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAgregarFuncionActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setText("Agregar Funcion");
        jLabel6.setToolTipText("");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel4.setText("Hora inicio");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(TxtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(ComboPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(ComboSala, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(DateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(BtnAgregarFuncion, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel6)
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ComboPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(ComboSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(DateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(BtnAgregarFuncion)
                    .addComponent(TxtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(44, 44, 44)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
