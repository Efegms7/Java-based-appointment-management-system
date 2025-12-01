package com.mycompany.finalodev;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RandevuVeritabaniHelper {

    public static boolean randevuOlustur(int ogrenciId, int ogretimId, String tarih, String saat) {
        String sql = "INSERT INTO Randevular (öğrenci_id, öğretim_üyesi_id, randevu_tarihi, randevu_saati) VALUES (?, ?, ?, ?)";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ogrenciId);
            pstmt.setInt(2, ogretimId);
            pstmt.setString(3, tarih);
            pstmt.setString(4, saat);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Gelişmiş hata yönetimi
            System.err.println("Veritabanı hatası: " + e.getMessage());
            e.printStackTrace();  // Detaylı hata bilgisi
            return false;
        }
    }
}