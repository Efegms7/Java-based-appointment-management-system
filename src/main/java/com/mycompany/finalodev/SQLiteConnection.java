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
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    private static final String DATABASE_URL = "jdbc:sqlite:akademik_randevu.db"; // Veritabanı dosyanızın adı

    // Veritabanı bağlantısını oluşturma
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}
