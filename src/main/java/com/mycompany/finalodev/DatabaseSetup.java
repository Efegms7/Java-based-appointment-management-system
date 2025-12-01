/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalodev;

/**
 *
 * @author efe
 */
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {

    // Tabloları oluşturma işlemi
    public static void createTables() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS Kullanıcılar (" +
                                  "kullanıcı_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                  "kullanıcı_adı TEXT NOT NULL, " +
                                  "kullanıcı_soyadı TEXT NOT NULL, " +
                                  "kullanıcı_email TEXT NOT NULL UNIQUE, " +
                                  "kullanıcı_sifre TEXT NOT NULL, " +
                                  "kullanıcı_türü TEXT CHECK(kullanıcı_türü IN ('Öğrenci', 'Öğretim Üyesi')) NOT NULL);";
        
        String createAvailabilityTable = "CREATE TABLE IF NOT EXISTS MusaitlikSaatleri (" +
            "musaitlik_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "kullanici_id INTEGER NOT NULL, " +
            "tarih DATE NOT NULL, " +
            "baslangic_saat TEXT NOT NULL, " +
            "bitis_saat TEXT NOT NULL, " +
            "FOREIGN KEY (kullanici_id) REFERENCES Kullanıcılar(kullanıcı_id));";
        
        String createAppointmentsTable = "CREATE TABLE IF NOT EXISTS Randevular (" +
                                        "randevu_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                        "öğrenci_id INTEGER NOT NULL, " +
                                        "öğretim_üyesi_id INTEGER NOT NULL, " +
                                        "randevu_tarihi DATE NOT NULL, " +
                                        "randevu_saati TEXT NOT NULL, " +
                                        "durum TEXT CHECK(durum IN ('Bekliyor', 'Onaylandı', 'Reddedildi')) DEFAULT 'Bekliyor', " +
                                        "FOREIGN KEY (öğrenci_id) REFERENCES Kullanıcılar(kullanıcı_id), " +
                                        "FOREIGN KEY (öğretim_üyesi_id) REFERENCES Kullanıcılar(kullanıcı_id));";
        
        try (Connection conn = SQLiteConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createAvailabilityTable);
            stmt.execute(createAppointmentsTable);
            System.out.println("Tablolar başarıyla oluşturuldu.");
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
        }
    }
}