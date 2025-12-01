package com.mycompany.finalodev;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KullanıcıGiris {
    
    // Kullanıcı girişini doğrulama fonksiyonu
    public boolean kullanıcıGirişYap(String kullanıcıAdi, String şifre, String kullanıcıTürü) {
        String query = "SELECT * FROM Kullanıcılar WHERE kullanıcı_adı = ? AND kullanıcı_sifre = ? AND kullanıcı_türü = ?";
        
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, kullanıcıAdi);
            pstmt.setString(2, şifre);
            pstmt.setString(3, kullanıcıTürü);  // Burada kullanıcı türü parametre olarak alınıyor
            
            ResultSet rs = pstmt.executeQuery();
            
            // Eğer sonuç varsa giriş başarılıdır
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
            return false;
        }
    }

    // Kullanıcının türünü (Öğrenci / Öğretim Üyesi) döndüren fonksiyon
    public String getKullanıcıTürü(String kullanıcıAdi) {
        String query = "SELECT kullanıcı_türü FROM Kullanıcılar WHERE kullanıcı_adı = ?";
        
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, kullanıcıAdi);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("kullanıcı_türü");
            }
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası (kullanıcı türü): " + e.getMessage());
        }
        
        return null; // Kullanıcı bulunamazsa null döner
    }
}