package com.mycompany.finalodev;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputFilter.Config;
import java.sql.*;


public class OgretimUyesiPanel {
    private JPanel ogretimUyesiPanel;
    private JTable randevuTablosu;
    private JButton onaylaButton;
    private JButton reddetButton;
    private JButton musaitlikSaatleriButton;
    private String ogretimAdi;

    public OgretimUyesiPanel(String ogretimAdi) {
        this.ogretimAdi = ogretimAdi;
        ogretimUyesiPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"Öğrenci Adı", "Randevu Tarihi", "Randevu Saati", "Durum"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        randevuTablosu = new JTable(tableModel);

        ogretimUyesiPanel.add(new JScrollPane(randevuTablosu), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        onaylaButton = new JButton("Onayla");
        reddetButton = new JButton("Reddet");
        musaitlikSaatleriButton = new JButton("Müsaitlik Saatlerini Belirle");

        buttonPanel.add(onaylaButton);
        buttonPanel.add(reddetButton);
        buttonPanel.add(musaitlikSaatleriButton);
        ogretimUyesiPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Tabloyu başlatırken doldur
        randevuTablosunuGuncelle();

        musaitlikSaatleriButton.addActionListener(e -> {
            MusaitlikSaatleriPanel musaitlikPanel = new MusaitlikSaatleriPanel(ogretimAdi);
            JFrame frame = new JFrame("Müsaitlik Saatlerini Belirle");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(musaitlikPanel.getMusaitlikPanel());
            frame.pack();
            frame.setVisible(true);
        });

        onaylaButton.addActionListener(e -> {
            int selectedRow = randevuTablosu.getSelectedRow();
            if (selectedRow >= 0) {
                String ogrenciAdi = (String) randevuTablosu.getValueAt(selectedRow, 0);
                String ogrenciEmail = getOgrenciEmail(ogrenciAdi); // E-posta al
                String randevuTarihi = (String) randevuTablosu.getValueAt(selectedRow, 1);
                String randevuSaati = (String) randevuTablosu.getValueAt(selectedRow, 2);

                try (Connection conn = SQLiteConnection.connect()) {
                    String sql = "UPDATE Randevular SET durum = 'Onaylandı' " +
                                 "WHERE öğrenci_id = (SELECT kullanıcı_id FROM Kullanıcılar WHERE LOWER(kullanıcı_adı) = LOWER(?)) " +
                                 "AND randevu_tarihi = ? AND randevu_saati = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, ogrenciAdi);
                    ps.setString(2, randevuTarihi);
                    ps.setString(3, randevuSaati);
                    int updated = ps.executeUpdate();
                    if (updated > 0) {
                        JOptionPane.showMessageDialog(null, "Randevu onaylandı.");

                        // E-posta gönder
                        if (ogrenciEmail != null) {
                            String subject = "Randevunuz Onaylandı";
                            String message = "Merhaba " + ogrenciAdi + ",\n\nRandevunuz " + randevuTarihi +
                                             " tarihinde, " + randevuSaati + " saatinde onaylanmıştır.\n\nİyi günler dileriz.";
                            EmailSender.sendEmail(ogrenciEmail, subject, message);
                        }

                        randevuTablosunuGuncelle();
                    } else {
                        JOptionPane.showMessageDialog(null, "Randevu bulunamadı veya zaten onaylı.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Randevu onaylanamadı: " + ex.getMessage());
                }
            }
        });

        reddetButton.addActionListener(e -> {
            int selectedRow = randevuTablosu.getSelectedRow();
            if (selectedRow >= 0) {
                String ogrenciAdi = (String) randevuTablosu.getValueAt(selectedRow, 0);
                try (Connection conn = SQLiteConnection.connect()) {
                    String sql = "UPDATE Randevular SET durum = 'Reddedildi' " +
                                 "WHERE öğrenci_id = (SELECT kullanıcı_id FROM Kullanıcılar WHERE LOWER(kullanıcı_adı) = LOWER(?)) " +
                                 "AND randevu_tarihi = ? AND randevu_saati = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, ogrenciAdi);
                    ps.setString(2, (String) randevuTablosu.getValueAt(selectedRow, 1));
                    ps.setString(3, (String) randevuTablosu.getValueAt(selectedRow, 2));
                    int updated = ps.executeUpdate();
                    if (updated > 0) {
                        JOptionPane.showMessageDialog(null, "Randevu reddedildi.");
                        randevuTablosunuGuncelle();
                    } else {
                        JOptionPane.showMessageDialog(null, "Randevu bulunamadı veya zaten reddedilmiş.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Randevu reddedilemedi: " + ex.getMessage());
                }
            }
        });
    }

    private void randevuTablosunuGuncelle() {
        DefaultTableModel model = (DefaultTableModel) randevuTablosu.getModel();
        model.setRowCount(0); // Önce tabloyu temizle

        try (Connection conn = SQLiteConnection.connect()) {
            String sql = "SELECT r.öğrenci_id, r.randevu_tarihi, r.randevu_saati, r.durum, u.kullanıcı_adı " +
                         "FROM Randevular r JOIN Kullanıcılar u ON r.öğrenci_id = u.kullanıcı_id " +
                         "WHERE r.öğretim_üyesi_id = (SELECT kullanıcı_id FROM Kullanıcılar WHERE kullanıcı_adı = ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ogretimAdi);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String ogrenciAdi = rs.getString("kullanıcı_adı");
                String tarih = rs.getString("randevu_tarihi");
                String saat = rs.getString("randevu_saati");
                String durum = rs.getString("durum");
                model.addRow(new Object[]{ogrenciAdi, tarih, saat, durum});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getOgrenciEmail(String ogrenciAdi) {
        try (Connection conn = SQLiteConnection.connect()) {
            String sql = "SELECT kullanıcı_email FROM Kullanıcılar WHERE LOWER (kullanıcı_adı) = LOWER(?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ogrenciAdi);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("kullanıcı_email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // E-posta adresi bulunamazsa
    }

    public JPanel getOgretimUyesiPanel() {
        return ogretimUyesiPanel;
    }
}