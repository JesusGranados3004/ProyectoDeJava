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
        RBtnPeliculas = new javax.swing.JRadioButton();
        RBtnOcupacion = new javax.swing.JRadioButton();
        RBtnFecha = new javax.swing.JRadioButton();
        RBtnTopTaquillados = new javax.swing.JRadioButton();
        BtnRealizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableReportes = new javax.swing.JTable();
        dateInicio = new com.toedter.calendar.JDateChooser();
        dateFin = new com.toedter.calendar.JDateChooser();
        lblInicio = new javax.swing.JLabel();
        lblFinal = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        buttonGroup1.add(RBtnPeliculas);
        RBtnPeliculas.setText("Venta por Pelucula");
        RBtnPeliculas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RBtnPeliculasActionPerformed(evt);
            }
        });

        buttonGroup1.add(RBtnOcupacion);
        RBtnOcupacion.setText("Ocupacion pro Funcion");
        RBtnOcupacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RBtnOcupacionActionPerformed(evt);
            }
        });

        buttonGroup1.add(RBtnFecha);
        RBtnFecha.setText("Rango de Fechas");
        RBtnFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RBtnFechaActionPerformed(evt);
            }
        });

        buttonGroup1.add(RBtnTopTaquillados);
        RBtnTopTaquillados.setText("Peliculas Mas Taquillera");
        RBtnTopTaquillados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RBtnTopTaquilladosActionPerformed(evt);
            }
        });

        BtnRealizar.setText("Realizar");
        BtnRealizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRealizarActionPerformed(evt);
            }
        });

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

        lblInicio.setText("Inicio:");

        lblFinal.setText("Fin:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(RBtnPeliculas)
                        .addGap(53, 53, 53)
                        .addComponent(RBtnOcupacion)
                        .addGap(57, 57, 57)
                        .addComponent(RBtnFecha)
                        .addGap(53, 53, 53)
                        .addComponent(RBtnTopTaquillados))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lblInicio)
                        .addGap(29, 29, 29)
                        .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblFinal)
                        .addGap(18, 18, 18)
                        .addComponent(dateFin, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(BtnRealizar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(125, 141, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(RBtnPeliculas))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(RBtnTopTaquillados)))
                        .addComponent(RBtnOcupacion, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(RBtnFecha, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblInicio)
                                .addComponent(lblFinal)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dateFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(BtnRealizar)))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFinal;
    private javax.swing.JLabel lblInicio;
    // End of variables declaration//GEN-END:variables
}
