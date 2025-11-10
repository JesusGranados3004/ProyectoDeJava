/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cine.Clases;

/**
 *
 * @author 57300
 */
public class Cuenta {
    private int id;
    private int ide;
    private String usuario;
    private String rol;

    public Cuenta(int id, int ide, String usuario, String rol) {
        this.id = id;
        this.ide = ide;
        this.usuario = usuario;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }
    
    public int getIde() {
        return ide;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getRol() {
        return rol;
    }

    
}
