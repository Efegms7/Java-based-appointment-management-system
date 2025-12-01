/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalodev;

import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author efe
 */
public class MusaitlikSaatleriPanel {
    private JPanel musaitlikPanel;
    private JTextField tarihField;
    private JTextField baslangicSaatiField;
    private JTextField bitisSaatiField;
    private JButton kaydetButton;
    private String ogretimAdi;

    public MusaitlikSaatleriPanel(String ogretimAdi) {
        this.ogretimAdi = ogretimAdi;
        musaitlikPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // UI bileşenleri
        tarihField = new JTextField(); // Tarih
        baslangicSaatiField = new JTextField(); // Başlangıç saati
        bitisSaatiField = new JTextField(); // Bitiş saati
        kaydetButton = new JButton("Müsaitlik Kaydet");

        // Panel üzerine eklemeler
        musaitlikPanel.add(new JLabel("Tarih (YYYY-MM-DD):"));
        musaitlikPanel.add(tarihField);
        musaitlikPanel.add(new JLabel("Başlangıç Saati (HH:MM):"));
        musaitlikPanel.add(baslangicSaatiField);
        musaitlikPanel.add(new JLabel("Bitiş Saati (HH:MM):"));
        musaitlikPanel.add(bitisSaatiField);
        musaitlikPanel.add(kaydetButton);

        // Butona tıklanma olayı
        kaydetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tarih = tarihField.getText();
                String baslangicSaati = baslangicSaatiField.getText();
                String bitisSaati = bitisSaatiField.getText();

                try (Connection conn = SQLiteConnection.connect()) {
                    int ogretimId = getOgretimId(conn, ogretimAdi);
                    String sql = "INSERT INTO MusaitlikSaatleri (kullanici_id, tarih, baslangic_saat, bitis_saat) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                  ps.setInt(1, ogretimId);
                  ps.setString(2, tarih);
                  ps.setString(3, baslangicSaati); // ✅ sadece başlangıç saati
                     ps.setString(4, bitisSaati);     // ✅ sadece bitiş saati
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Müsaitlik saati kaydedildi.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Müsaitlik saati kaydedilemedi.");
                }
            }
        });
    }

    private int getOgretimId(Connection conn, String ogretimAdi) throws SQLException {
        String sql = "SELECT kullanıcı_id FROM Kullanıcılar WHERE kullanıcı_adı = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ogretimAdi);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("kullanıcı_id") : -1;
    }

    public JPanel getMusaitlikPanel() {
        return musaitlikPanel;
    }
}
