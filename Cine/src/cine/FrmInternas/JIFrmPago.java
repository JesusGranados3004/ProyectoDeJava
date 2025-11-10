/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package cine.FrmInternas;

import java.util.ArrayList;
import cine.Clases.ConexionBD;
import cine.Clases.Cuenta;
import java.sql.CallableStatement;
import java.sql.Connection;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author 57300
 */
public class JIFrmPago extends javax.swing.JInternalFrame {

    /**
     * Creates new form JIFrmPago
     */
    
    ConexionBD conn;
    ArrayList<String> metodos;
    ArrayList<Integer> asientoId = new ArrayList();
    int id,reserva;
    String respuesta;
    Cuenta cuenta;
    double total;
    
    public JIFrmPago(int reserva,int id,double total,Cuenta cuenta, ArrayList asientoId) {
        initComponents();
        conn = new ConexionBD();
        metodos = new ArrayList();
        metodos.add("Tarjeta");
        metodos.add("Efectivo");
        this.reserva= reserva;
        this.id= id;
        this.cuenta= cuenta;
        this.total= total;
        this.asientoId= asientoId;
        llenarCombo();
        String precio = Double.toString(total);
        TxtPrecio.setText(precio);
        ComboMetodoDePago.addActionListener(e -> {
            boolean esTarjeta = "Tarjeta".equals(ComboMetodoDePago.getSelectedItem());
            Lblmetodo.setVisible(esTarjeta);
            TxtMetodoDePago.setVisible(esTarjeta);
        });
        
        addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Cancelar la reserva?");
                if (respuesta == JOptionPane.YES_OPTION) {
                    cancelarAsiento();
                    cancelarReserva();
                    dispose();
                }
            }
        });
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public void llenarCombo(){
        ComboMetodoDePago.removeAllItems();
        ComboMetodoDePago.addItem("Seleccione...");
        for (int i = 0; i < metodos.size(); i++) {
            String metodo = metodos.get(i).toString();
            ComboMetodoDePago.addItem(metodo);
        }
    }
    
    public String seleccion(){
        String respuesta;
        if(ComboMetodoDePago.getSelectedItem().equals("Tarjeta")){
            String numeroCompleto = TxtMetodoDePago.getText().trim(); 
            respuesta = numeroCompleto.substring(numeroCompleto.length() - 4);
        }else{
            Lblmetodo.setVisible(false);
            ComboMetodoDePago.setVisible(false);
            respuesta = null;
        }
        return respuesta;
    }
    
    public void GuardarPago(String respuesta){
        try {
            Connection con = conn.Conexion();
            CallableStatement cstmt = con.prepareCall("call insertar_pago(?, ?, ?, ?)");
            cstmt.setInt(1, reserva);
            cstmt.setDouble(2, total);
            cstmt.setString(3, ComboMetodoDePago.getSelectedItem().toString());
            cstmt.setString(4, respuesta);
       
            
            cstmt.execute();
            
            JOptionPane.showMessageDialog(null, "Pago exitoso");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: al guardar " + e.getMessage());
            
        }
    }
    
    public void GuardarTicket(){
        int cantidad = asientoId.size();
        double precio = total / cantidad;
        for (int i = 0; i < asientoId.size(); i++) {
            int asiento = asientoId.get(i);
            try {
                Connection con = conn.Conexion();
                CallableStatement cstmt = con.prepareCall("call InsertarTicket(?, ?, ?, ?)");
               
                cstmt.setInt(1, reserva);
                cstmt.setInt(2, id);
                cstmt.setInt(3, asiento);
                cstmt.setDouble(4, precio);

                cstmt.execute();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: al guardar " + e.getMessage());
                break;
            }
        }
    }
    
    private void cancelarReserva() {
        try{
            Connection con = conn.Conexion();
            String sql = "call eliminar_reserva(?)";
            
            CallableStatement cstmt = con.prepareCall(sql);
            cstmt.setInt(1, reserva);
            int filas = cstmt.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Reserva cancelada");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró la reserva");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cancelar reserva: " + e.getMessage());
        }
    }
    
    private void cancelarAsiento() {
        for (int i = 0; i < asientoId.size(); i++) {
            int idAsiento = asientoId.get(i);
            try{
                Connection con = conn.Conexion();
                String sql = "call eliminar_asiento(?)";
                CallableStatement cstmt = con.prepareCall(sql);
               cstmt.setInt(1, idAsiento);
               int filas = cstmt.executeUpdate();

           } catch (Exception e) {
               JOptionPane.showMessageDialog(null, "Error al cancelar asiento: " + e.getMessage());
           }
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

        jTextField4 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        Lblmetodo = new javax.swing.JLabel();
        TxtPrecio = new javax.swing.JTextField();
        TxtMetodoDePago = new javax.swing.JTextField();
        BtnPagar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        ComboMetodoDePago = new javax.swing.JComboBox<>();

        jTextField4.setText("jTextField4");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jLabel1.setText("Monto a pagar:");

        Lblmetodo.setText("Targeta:");

        TxtPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtPrecioActionPerformed(evt);
            }
        });

        BtnPagar.setText("Pagar");
        BtnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPagarActionPerformed(evt);
            }
        });

        jLabel3.setText("Metodo de Pago:");

        ComboMetodoDePago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(ComboMetodoDePago, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(Lblmetodo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtMetodoDePago, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(ComboMetodoDePago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtMetodoDePago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Lblmetodo))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(33, 33, 33)
                .addComponent(BtnPagar)
                .addGap(36, 36, 36))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtPrecioActionPerformed

    private void BtnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPagarActionPerformed
        // TODO add your handling code here:
        if(ComboMetodoDePago.getSelectedIndex() > 0 ){
            respuesta = seleccion();
            GuardarTicket();
            GuardarPago(respuesta);
            this.dispose();
        }else{
            JOptionPane.showMessageDialog(rootPane, "Le faltan parametros");
        }
    }//GEN-LAST:event_BtnPagarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnPagar;
    private javax.swing.JComboBox<String> ComboMetodoDePago;
    private javax.swing.JLabel Lblmetodo;
    private javax.swing.JTextField TxtMetodoDePago;
    private javax.swing.JTextField TxtPrecio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
