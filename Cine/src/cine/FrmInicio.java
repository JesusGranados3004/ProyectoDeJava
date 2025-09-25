/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cine;

import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author 57300
 */
public class FrmInicio extends javax.swing.JFrame {

    /**
     * Creates new form FrmInicio
     */
    private FrmVentanaPelicula VentanaPelicula;
    private FrmTablaPelicula tabla;
    private FrmAdministrador administrador;
    
    ArrayList<Peliculas> ListPelicula;
    
    public FrmInicio() {
        initComponents();
        ListPelicula = new ArrayList();
        VentanaPelicula = new FrmVentanaPelicula();
        tabla = new FrmTablaPelicula();
        administrador = new FrmAdministrador();
        
        ListPelicula.add(new Peliculas(1, "Titanic", "Romance", "3h 15min", 1997, "James Cameron"));
        ListPelicula.add(new Peliculas(2, "Avatar", "Ciencia ficción", "2h 42min", 2009, "James Cameron"));
        ListPelicula.add(new Peliculas(3, "Inception", "Acción", "2h 28min", 2010, "Christopher Nolan"));
        ListPelicula.add(new Peliculas(4, "The Godfather", "Crimen", "2h 55min", 1972, "Francis Ford Coppola"));
        ListPelicula.add(new Peliculas(5, "Interstellar", "Ciencia ficción", "2h 49min", 2014, "Christopher Nolan"));
        ListPelicula.add(new Peliculas(6, "Parasite", "Drama", "2h 12min", 2019, "Bong Joon-ho"));
        
        llenarCombo();
        ComboRomances();
        ComboAccion();
        ComboCrimen();
        ComboDrama();
        
        configurarCombos();
         
    }
    
    public void setlblUser(String user)
    {
        lblUser.setText(user);
    }
    
    public void setBtnAdministrador(boolean rango)
    {
        BtnAdministrador.setVisible(rango);
    }
    
    public void comprovar()
    {
        for (int i = 0; i < ListPelicula.size(); i++) 
        {
            if(TxtBuscar.getText().equalsIgnoreCase(ListPelicula.get(i).getTitulo()))
            {
                JOptionPane.showMessageDialog(rootPane, "Titulo: " + ListPelicula.get(i).getTitulo()
                + "\n Genero: " + ListPelicula.get(i).getGenero() + "\n Duracion: " + ListPelicula.get(i).getDuracion()
                + "\n Año: " + ListPelicula.get(i).getAnioEstreno() + "\n Director: " + ListPelicula.get(i).getDirector());
            }
        }
    }
    
    public void llenarCombo()
    {
        ComboRecomendados.removeAllItems();
        ComboRecomendados.addItem("Seleccione...");
        for (int i = 0; i < ListPelicula.size(); i++){
            Peliculas item = ListPelicula.get(i);
            if(item.getGenero().equals("Ciencia ficción"))
            {
                ComboRecomendados.addItem(item.getTitulo());
            }
            
        }
    }

    public void ComboRomances()
    {
        ComboRomance.removeAllItems();
        ComboRomance.addItem("Seleccione...");
        for (int i = 0; i < ListPelicula.size(); i++){
            Peliculas item = ListPelicula.get(i);
            if(item.getGenero().equals("Romance"))
            {
                ComboRomance.addItem(item.getTitulo());
            }
            
        }
    }
    
    public void ComboAccion()
    {
        ComboAccion.removeAllItems();
        ComboAccion.addItem("Seleccione...");
        for (int i = 0; i < ListPelicula.size(); i++){
            Peliculas item = ListPelicula.get(i);
            if(item.getGenero().equals("Acción"))
            {
                ComboAccion.addItem(item.getTitulo());
            }
            
        }
    }
    
    public void ComboCrimen()
    {
        ComboCrimen.removeAllItems();
        ComboCrimen.addItem("Seleccione...");
        for (int i = 0; i < ListPelicula.size(); i++){
            Peliculas item = ListPelicula.get(i);
            if(item.getGenero().equals("Crimen"))
            {
                ComboCrimen.addItem(item.getTitulo());
            }
            
        }
    }
    
    public void ComboDrama()
    {
        ComboDrama.removeAllItems();
        ComboDrama.addItem("Seleccione...");
        for (int i = 0; i < ListPelicula.size(); i++){
            Peliculas item = ListPelicula.get(i);
            if(item.getGenero().equals("Drama"))
            {
                ComboDrama.addItem(item.getTitulo());
            }
            
        }
    }
    
    private void registrarEvento(JComboBox<String> combo) {
        combo.addActionListener(e -> {
            if (combo.getItemCount() == 0 || 
                combo.getSelectedItem().toString().isEmpty()) {
                return;
            }
            seleccionado(combo);
        });
    }
    
    private void configurarCombos() {
        registrarEvento(ComboRecomendados);
        registrarEvento(ComboRomance);
        registrarEvento(ComboAccion);
        registrarEvento(ComboCrimen);
        registrarEvento(ComboDrama);
    }
    
    public void seleccionado(JComboBox<String> combo) {
        String peliculaSeleccionada = combo.getSelectedItem().toString();

        boolean encontrada = false;
        for (int i = 0; i < ListPelicula.size(); i++) {
            if (peliculaSeleccionada.equals(ListPelicula.get(i).getTitulo())) {
                Peliculas id = ListPelicula.get(i);
                VentanaPelicula.setInicio(this);
                VentanaPelicula.setTitulo(id.getTitulo());
                VentanaPelicula.setGenero(id.getGenero());
                VentanaPelicula.setDirector(id.getDirector());
                VentanaPelicula.setDuracion(id.getDuracion());
                VentanaPelicula.setVisible(true);
                this.setVisible(false);

                encontrada = true;
                break;
            }
        }

        if (!encontrada) {
            JOptionPane.showMessageDialog(rootPane, "La película seleccionada no está disponible.");
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

        TxtBuscar = new javax.swing.JTextField();
        BtnBuscar = new javax.swing.JButton();
        lblUser = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ComboRecomendados = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ComboRomance = new javax.swing.JComboBox<>();
        ComboAccion = new javax.swing.JComboBox<>();
        ComboCrimen = new javax.swing.JComboBox<>();
        ComboDrama = new javax.swing.JComboBox<>();
        BtnAdministrador = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 153));

        BtnBuscar.setText("Buscar");
        BtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarActionPerformed(evt);
            }
        });

        lblUser.setText("jLabel1");

        jLabel2.setText("Ciencia ficcion");

        ComboRecomendados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Romance");

        jLabel3.setText("Acción");

        jLabel4.setText("Crimen");

        jLabel5.setText("Drama");

        ComboRomance.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ComboAccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ComboCrimen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ComboDrama.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        BtnAdministrador.setText("Administrador");
        BtnAdministrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAdministradorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BtnBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblUser, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnAdministrador)
                        .addGap(43, 43, 43))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ComboRecomendados, 0, 257, Short.MAX_VALUE)
                            .addComponent(ComboRomance, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ComboAccion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ComboCrimen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ComboDrama, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnBuscar)
                    .addComponent(lblUser)
                    .addComponent(BtnAdministrador))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboRecomendados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ComboRomance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(ComboAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ComboCrimen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(ComboDrama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed
        comprovar();        // TODO add your handling code here:
    }//GEN-LAST:event_BtnBuscarActionPerformed

    private void BtnAdministradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAdministradorActionPerformed
        // TODO add your handling code here:
        administrador.setInicio(this);
        administrador.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_BtnAdministradorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmInicio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAdministrador;
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JComboBox<String> ComboAccion;
    private javax.swing.JComboBox<String> ComboCrimen;
    private javax.swing.JComboBox<String> ComboDrama;
    private javax.swing.JComboBox<String> ComboRecomendados;
    private javax.swing.JComboBox<String> ComboRomance;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblUser;
    // End of variables declaration//GEN-END:variables
}
