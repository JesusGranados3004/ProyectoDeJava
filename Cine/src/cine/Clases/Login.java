/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cine.Clases;

/**
 *
 * @author 57300
 */
public class Login {

    private String user;
    private String password;
    private int rango;

    public Login(String user, String password,int rango) {
        this.user = user;
        this.password = password;
        this.rango = rango;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getRango() {
        return rango;
    }
    
    
    
}
