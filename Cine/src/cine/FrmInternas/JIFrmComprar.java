/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package cine.FrmInternas;

import cine.Clases.Cuenta;
import cine.Clases.ConexionBD;
import cine.Clases.Funcion;
import cine.Clases.Peliculas;
import cine.Clases.Sesion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author 57300
 */
public class JIFrmComprar extends javax.swing.JInternalFrame {

    /**
     * Creates new form JIFrmComprar
     */
    
    int id,ids , asientoId;
    String titulo,Sala,Inicio,Fin;
    double precio,total;
    Peliculas pelicula;
    Funcion funcion;
    
    ArrayList<JRadioButton> listabotones = new ArrayList();
    ArrayList<Integer> asientosId = new ArrayList();
    private final ArrayList<Integer> idsFuncion = new ArrayList<>();
    private final ArrayList<Integer> idsSala    = new ArrayList<>();
    String [] encabezado = {"Titulo","Sala","Inicio","Fin","Precio"};
    int cantidad;
    ConexionBD conn;
    public JIFrmComprar(int sala) {
        initComponents();
        cantidad = sala;
        conn = new ConexionBD();
        AgregarAsientos();
    }
    
    public void mostrarInfo(Peliculas pelicula) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(encabezado);
        this.pelicula = pelicula;
        int peliculaId = pelicula.getId();

        LlenarTablaFuncion(TableFuncion, modelo, peliculaId); 
    }
    
    public void AgregarAsientos() {
        PanelSilla.removeAll();

        int x = 10;
        int y = 10;
        int ancho = 80;
        int alto = 25;
        int columnas = 5;
        int separacion = 10;
        if(cantidad > 20){
            columnas = 7;
        }
        for (int i = 1; i <= cantidad; i++) {
            JRadioButton silla = new JRadioButton("Silla " + i);

            int fila = (i - 1) / columnas;
            int col = (i - 1) % columnas;
            int posX = x + col * (ancho + separacion);
            int posY = y + fila * (alto + separacion);

            silla.setBounds(posX, posY, ancho, alto);
            
            silla.addItemListener(e -> calcularTotal());
            
            PanelSilla.add(silla);
            listabotones.add(silla);
        }
        
        PanelSilla.revalidate();
        PanelSilla.repaint();
    }
    
    public void calcularTotal(){
        int seleccionadas = 0;
        for(int i = 0; i < listabotones.size(); i++){
            JRadioButton rb = listabotones.get(i);  
            if(rb.isSelected()){ 
                seleccionadas++;
            }
        }
        
        total = seleccionadas * precio;
    }
    
    private ArrayList<Integer> actualizarListaSillas(JIFrmTicket ticket) {
        ArrayList<Integer> seleccionadas = new ArrayList<>();
        for (int i = 0; i < listabotones.size(); i++) {
            JRadioButton rb = listabotones.get(i);
            if (rb.isSelected()) {
                int num = Integer.parseInt(rb.getText().substring(6));
                seleccionadas.add(num);
               
                try {
                    Connection con = conn.Conexion();

                    String sql = "CALL InsertarAsiento(?, ?, ?, ?)";
                    CallableStatement cstmt = con.prepareCall(sql);
                    cstmt.setInt(1, num);
                    cstmt.setInt(2, ids);
                    cstmt.setInt(3, id);
                    cstmt.registerOutParameter(4, Types.INTEGER);
                    
                    cstmt.execute();
                    asientosId.clear();
                    asientoId = cstmt.getInt(4);
                    asientosId.add(asientoId);
                    
                    JOptionPane.showMessageDialog(null, "Silla reservada correctamente.");
                    getDesktopPane().add(ticket);
                    ticket.setVisible(true);
                    this.setVisible(false);

                } catch (SQLException e) {
                    if ("45000".equals(e.getSQLState())) {
                        JOptionPane.showMessageDialog(
                                null,
                                "La silla " + num + " ya está ocupada.",
                                "Duplicado",
                                JOptionPane.WARNING_MESSAGE
                               
                        );
                        break;
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Error al guardar: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        break;
                    }
                }
            }
        }
        return seleccionadas;   
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
    
    public int buscarSala(String sala){
        int cantidad = 0;
        try {
            Connection con = conn.Conexion();
            String sql = "call Obtenersala (?)";
            CallableStatement cstmt =  con.prepareCall(sql);
            cstmt.setString(1, sala);
            
            ResultSet resultado = cstmt.executeQuery();
            while(resultado.next()){
                cantidad = resultado.getInt("capacidad");  
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return cantidad;          
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableFuncion = new javax.swing.JTable();
        PanelSilla = new javax.swing.JPanel();
        BtnReservar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

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
        TableFuncion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableFuncionMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableFuncion);

        javax.swing.GroupLayout PanelSillaLayout = new javax.swing.GroupLayout(PanelSilla);
        PanelSilla.setLayout(PanelSillaLayout);
        PanelSillaLayout.setHorizontalGroup(
            PanelSillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        PanelSillaLayout.setVerticalGroup(
            PanelSillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 232, Short.MAX_VALUE)
        );

        BtnReservar.setText("Reservar");
        BtnReservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnReservarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(PanelSilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnReservar)
                .addGap(319, 319, 319))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelSilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(BtnReservar)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TableFuncionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableFuncionMouseClicked
        // TODO add your handling code here:
        int fila = TableFuncion.getSelectedRow();
        if (fila == -1){
            return;
        }
        titulo  = TableFuncion.getValueAt(fila, 0).toString();
        Sala    = TableFuncion.getValueAt(fila, 1).toString();
        Inicio  = TableFuncion.getValueAt(fila, 2).toString();
        Fin     = TableFuncion.getValueAt(fila, 3).toString();
        precio  = Double.parseDouble(TableFuncion.getValueAt(fila, 4).toString());

        id  = idsFuncion.get(fila);
        ids = idsSala.get(fila);

        System.out.println("Fila " + fila + " -> funcion: " + id + " sala: " + ids);

        cantidad = buscarSala(Sala);
        AgregarAsientos();
    }//GEN-LAST:event_TableFuncionMouseClicked

    private void BtnReservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnReservarActionPerformed
        // TODO add your handling code here:

        JIFrmTicket ticket = new JIFrmTicket(this.id, titulo, Sala, Inicio, Fin, total,Sesion.usuarioActivo, asientosId);
        ArrayList<Integer> sillasElegidas = actualizarListaSillas(ticket); 
        ticket.silla.addAll(sillasElegidas);
        ticket.cambiardatos();          

    }//GEN-LAST:event_BtnReservarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnReservar;
    private javax.swing.JPanel PanelSilla;
    private javax.swing.JTable TableFuncion;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
