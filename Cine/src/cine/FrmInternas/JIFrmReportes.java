/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package cine.FrmInternas;

import java.sql.Connection;
import cine.Clases.ConexionBD;
import java.sql.CallableStatement;
import java.util.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 57300
 */
public class JIFrmReportes extends javax.swing.JInternalFrame {

    /**
     * Creates new form JIFrmReportes
     */
    
    ConexionBD conn;
    boolean vista;
    
    public JIFrmReportes() {
        initComponents();
        conn = new ConexionBD();
        ajustarVisibilidadFechas();
    }

    // se llena la tabla y se consultan los datos necesarios que la tabla requere
    // para asi mostrar el reporte de cada pelicula
    private void ejecutarReporteVentasPorPelicula() {
        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Película", "Boletos vendidos", "Ingresos"}, 0);

        try {
            Connection con = conn.Conexion();
            String sql = "call ReporteVentasPorPelicula()";
            CallableStatement cstmt = con.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("Pelicula"),
                    rs.getInt("BoletosVendidos"),
                    rs.getDouble("Ingresos")
                };
                modelo.addRow(fila);
            }

            TableReportes.setModel(modelo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Error al generar reporte: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // aqui se consulta la cantidad de asientos que se ocuparon por cada funcion
    private void ejecutarReporteOcupacionPorFuncion() {
        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Función ID", "Película", "Sala", "Capacidad", "Ocupados", "% Ocupación"}, 0);

        try{
            Connection con = conn.Conexion();
            String sql = "call ReporteOcupacionPorFuncion()";
            CallableStatement cstmt = con.prepareCall(sql);
                    
            ResultSet rs = cstmt.executeQuery();      
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("FuncionID"),
                    rs.getString("Pelicula"),
                    rs.getString("Sala"),
                    rs.getInt("CapacidadTotal"),
                    rs.getInt("AsientosOcupados"),
                    rs.getDouble("PorcentajeOcupacion")
                };
                modelo.addRow(fila);
            }

            TableReportes.setModel(modelo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Error al generar reporte: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // aqui se consulta las ventas de las peliculas por un rango de fechas 
    // y se lle na la tabla y se hace una validacion interna para ver si se a seleccionado 
    // el rango de fechas
    private void ejecutarVentasPorRangoConCalendar() {
       
        Date Inicio = dateInicio.getDate();
        Date fin = dateFin.getDate();
        
        if (Inicio == null || fin == null) {
            JOptionPane.showMessageDialog(this, "Selecciona ambas fechas.");
            return;
        }
        
        java.sql.Date sqlInicio = new java.sql.Date(Inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        
        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Fecha", "Boletos", "Ingresos"}, 0);
 
        try{
            
            Connection con = conn.Conexion();    
            
            String sql = "call ReporteVentasPorRangoFechasTabla(?, ?)";
            
            CallableStatement cstmt = con.prepareCall(sql);
            cstmt.setDate(1, sqlInicio);
            cstmt.setDate(2, sqlFin);
                
            ResultSet rs = cstmt.executeQuery();
            
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getDate("Fecha"),
                    rs.getInt("Boletos"),
                    rs.getDouble("Ingresos")
                });
            }
                
            TableReportes.setModel(modelo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // y aqui se consulta a la base de datos el top de peliculas mas taquilladas
    // y se llena la tabla
    private void ejecutarReportePeliculasMasTaquilleras() {
        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Película", "Boletos vendidos", "Ingresos totales"}, 0);

        try{
            
            Connection con = conn.Conexion();
            String sql = "call ReportePeliculasMasTaquilleras(?)";
            CallableStatement cstmt = con.prepareCall(sql);
            cstmt.setInt(1, 5);
            
            ResultSet rs = cstmt.executeQuery();
            
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("Pelicula"),
                    rs.getInt("BoletosVendidos"),
                    rs.getDouble("IngresosTotales")
                });
            }            

            TableReportes.setModel(modelo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // para ajustar la visivilidad de los componentes para seleccionar 
    // la fecha si el boton que ejecuta la consulta por rango de fechas es seleccionada 
    // se muestra si no no se muestran permanecen ocultas
    private void ajustarVisibilidadFechas() {
        if(RBtnFecha.isSelected()){
            dateInicio.setVisible(true);
            dateFin.setVisible(true);
            BtnRealizar.setVisible(true);
            lblInicio.setVisible(true);
            lblFinal.setVisible(true);
        }else{
            dateInicio.setVisible(false);
            dateFin.setVisible(false);
            BtnRealizar.setVisible(false);
            lblInicio.setVisible(false);
            lblFinal.setVisible(false);
        }
        
    }
    
    // para limpiar la tabla cuando se seleccione el rango de fecha 
    // para asi no mostrar informacion de otra
    private void limpiarTabla(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableReportes = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        RBtnPeliculas = new javax.swing.JRadioButton();
        RBtnOcupacion = new javax.swing.JRadioButton();
        RBtnFecha = new javax.swing.JRadioButton();
        RBtnTopTaquillados = new javax.swing.JRadioButton();
        BtnRealizar = new javax.swing.JButton();
        dateInicio = new com.toedter.calendar.JDateChooser();
        dateFin = new com.toedter.calendar.JDateChooser();
        lblInicio = new javax.swing.JLabel();
        lblFinal = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        TableReportes.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        TableReportes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(TableReportes);

        jPanel1.setBackground(new java.awt.Color(139, 220, 224));

        buttonGroup1.add(RBtnPeliculas);
        RBtnPeliculas.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        RBtnPeliculas.setText("Venta por Pelicula");
        RBtnPeliculas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RBtnPeliculasActionPerformed(evt);
            }
        });

        buttonGroup1.add(RBtnOcupacion);
        RBtnOcupacion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        RBtnOcupacion.setText("Ocupacion por Funcion");
        RBtnOcupacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RBtnOcupacionActionPerformed(evt);
            }
        });

        buttonGroup1.add(RBtnFecha);
        RBtnFecha.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        RBtnFecha.setText("Rango de Fechas");
        RBtnFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RBtnFechaActionPerformed(evt);
            }
        });

        buttonGroup1.add(RBtnTopTaquillados);
        RBtnTopTaquillados.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        RBtnTopTaquillados.setText("Peliculas Mas Taquillera");
        RBtnTopTaquillados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RBtnTopTaquilladosActionPerformed(evt);
            }
        });

        BtnRealizar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        BtnRealizar.setText("Realizar");
        BtnRealizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRealizarActionPerformed(evt);
            }
        });

        lblInicio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblInicio.setText("Inicio:");

        lblFinal.setText("Fin:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(RBtnPeliculas)
                        .addGap(53, 53, 53)
                        .addComponent(RBtnOcupacion)
                        .addGap(57, 57, 57)
                        .addComponent(RBtnFecha)
                        .addGap(53, 53, 53)
                        .addComponent(RBtnTopTaquillados))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(lblInicio)
                        .addGap(18, 18, 18)
                        .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(lblFinal)
                        .addGap(18, 18, 18)
                        .addComponent(dateFin, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(BtnRealizar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RBtnPeliculas)
                    .addComponent(RBtnTopTaquillados)
                    .addComponent(RBtnOcupacion)
                    .addComponent(RBtnFecha))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnRealizar)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblFinal)
                        .addComponent(dateFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblInicio)
                        .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // segun el boton que se seleccione se ejecutara uno de los metodos que le corresponda
    private void RBtnPeliculasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RBtnPeliculasActionPerformed
        // TODO add your handling code here:
        ejecutarReporteVentasPorPelicula();
        ajustarVisibilidadFechas();
    }//GEN-LAST:event_RBtnPeliculasActionPerformed

    private void RBtnOcupacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RBtnOcupacionActionPerformed
        // TODO add your handling code here:
        ejecutarReporteOcupacionPorFuncion();
        ajustarVisibilidadFechas();
    }//GEN-LAST:event_RBtnOcupacionActionPerformed

    private void RBtnFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RBtnFechaActionPerformed
        // TODO add your handling code here:
        limpiarTabla(TableReportes);
        ajustarVisibilidadFechas();
    }//GEN-LAST:event_RBtnFechaActionPerformed

    private void RBtnTopTaquilladosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RBtnTopTaquilladosActionPerformed
        // TODO add your handling code here:
        ejecutarReportePeliculasMasTaquilleras();
        ajustarVisibilidadFechas();
    }//GEN-LAST:event_RBtnTopTaquilladosActionPerformed

    private void BtnRealizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRealizarActionPerformed
        // TODO add your handling code here:
        ejecutarVentasPorRangoConCalendar();
    }//GEN-LAST:event_BtnRealizarActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnRealizar;
    private javax.swing.JRadioButton RBtnFecha;
    private javax.swing.JRadioButton RBtnOcupacion;
    private javax.swing.JRadioButton RBtnPeliculas;
    private javax.swing.JRadioButton RBtnTopTaquillados;
    private javax.swing.JTable TableReportes;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser dateFin;
    private com.toedter.calendar.JDateChooser dateInicio;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFinal;
    private javax.swing.JLabel lblInicio;
    // End of variables declaration//GEN-END:variables
}
