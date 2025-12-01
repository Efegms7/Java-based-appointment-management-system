/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalodev;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Veritabanı tablolarını oluştur
        DatabaseSetup.createTables();
       
        

        // Giriş ekranını göster
        JFrame frame = new JFrame("Akademik Randevu Sistemi");
        frame.setContentPane(new LoginScreen().getLoginPanel());  // LoginScreen panelini gösteriyoruz
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}