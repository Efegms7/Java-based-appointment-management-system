/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.finalodev;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KullanıcıKayit {
    
    // Kullanıcıyı kaydetme fonksiyonu
    public boolean kullanıcıKaydet(String kullanıcıAdi, String şifre,  String soyad, String email, String kullanıcıTipi) {
        String query = "INSERT INTO Kullanıcılar (kullanıcı_adı, kullanıcı_soyadı, kullanıcı_email, kullanıcı_sifre, kullanıcı_türü) " +
                       "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, kullanıcıAdi);
            pstmt.setString(2, soyad);
            pstmt.setString(3, email); // Artık hata vermez
            pstmt.setString(4, şifre);
            pstmt.setString(5, kullanıcıTipi);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // Eğer kayıt başarılıysa true döner
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
            return false;
        }
    }
}