package com.mycompany.finalodev;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalTime;
import javax.swing.table.DefaultTableModel;


public class RandevuPanel {
    private JPanel randevuAnaPanel;

    public RandevuPanel(String ogrenciEmail) {
        // Sol Panel: Randevu Oluştur
        JPanel solPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField tarihField = new JTextField();  // Format: YYYY-MM-DD
        JComboBox<String> musaitSaatBox = new JComboBox<>();
        JComboBox<String> ogretimUyesiBox = new JComboBox<>();
        JButton randevuOlusturButton = new JButton("Randevu Talebi Gönder");

        // Öğretim üyelerini combobox'a çek
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT kullanıcı_adı FROM Kullanıcılar WHERE kullanıcı_türü = 'Öğretim Üyesi'");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ogretimUyesiBox.addItem(rs.getString("kullanıcı_adı"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Hoca veya tarih değiştiğinde müsait saatleri getir
        ActionListener updateSaatler = e -> {
            musaitSaatBox.removeAllItems();
            String ogretimEmail = (String) ogretimUyesiBox.getSelectedItem();
            String tarih = tarihField.getText();

            if (ogretimEmail == null || tarih.isEmpty()) return;

            try (Connection conn = SQLiteConnection.connect()) {
                int ogretimId = getUserIdByEmail(conn, ogretimEmail);

                String sql = "SELECT baslangic_saat, bitis_saat FROM MusaitlikSaatleri WHERE kullanici_id = ? AND tarih = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, ogretimId);
                ps.setString(2, tarih);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    LocalTime bas = LocalTime.parse(rs.getString("baslangic_saat"));
                    LocalTime son = LocalTime.parse(rs.getString("bitis_saat"));

                    while (!bas.plusMinutes(20).isAfter(son)) {
                        String slot = bas.toString();

                        if (!isRandevuAlinmis(conn, ogretimId, tarih, slot)) {
                            musaitSaatBox.addItem(slot);
                        }

                        bas = bas.plusMinutes(20);
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        };

        ogretimUyesiBox.addActionListener(updateSaatler);
        tarihField.addActionListener(updateSaatler);

        solPanel.add(new JLabel("Tarih (YYYY-MM-DD):"));
        solPanel.add(tarihField);
        solPanel.add(new JLabel("Öğretim Üyesi:"));
        solPanel.add(ogretimUyesiBox);
        solPanel.add(new JLabel("Müsait Saat Seç:"));
        solPanel.add(musaitSaatBox);
        solPanel.add(new JLabel(""));
        solPanel.add(randevuOlusturButton);

        // Sağ Panel: Randevularım
        String[] kolonlar = {"Tarih", "Saat", "Hoca MaılAdresi", "Durum"};
        DefaultTableModel tableModel = new DefaultTableModel(kolonlar, 0);
        JTable table = new JTable(tableModel);
        JScrollPane sagPanel = new JScrollPane(table);

        loadRandevularim(tableModel, ogrenciEmail);

        // Butona tıklanınca randevu oluştur
        randevuOlusturButton.addActionListener(e -> {
            String tarih = tarihField.getText();
            String saat = (String) musaitSaatBox.getSelectedItem();
            String ogretimEmail = (String) ogretimUyesiBox.getSelectedItem();

            if (saat == null || saat.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir saat seçiniz.");
                return;
            }

            try (Connection conn = SQLiteConnection.connect()) {
                int ogrenciId = getUserIdByEmail(conn, ogrenciEmail);
                int ogretimId = getUserIdByEmail(conn, ogretimEmail);

                String sql = "INSERT INTO Randevular (öğrenci_id, öğretim_üyesi_id, randevu_tarihi, randevu_saati, durum) " +
                             "VALUES (?, ?, ?, ?, 'Bekliyor')";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, ogrenciId);
                ps.setInt(2, ogretimId);
                ps.setString(3, tarih);
                ps.setString(4, saat);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Randevu talebiniz iletildi!");
                musaitSaatBox.removeItem(saat);

                loadRandevularim(tableModel, ogrenciEmail); // tabloyu güncelle

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Randevu oluşturulamadı.");
            }
        });

        // Ana Panel: sol ve sağ paneli böl
        randevuAnaPanel = new JPanel(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, solPanel, sagPanel);
        splitPane.setDividerLocation(400);
        randevuAnaPanel.add(splitPane, BorderLayout.CENTER);
    }

    private void loadRandevularim(DefaultTableModel model, String ogrenciEmail) {
        model.setRowCount(0);
        try (Connection conn = SQLiteConnection.connect()) {
            int ogrenciId = getUserIdByEmail(conn, ogrenciEmail);

            String sql = "SELECT r.randevu_tarihi, r.randevu_saati, k.kullanıcı_email AS hoca_email, r.durum " +
                         "FROM Randevular r JOIN Kullanıcılar k ON r.öğretim_üyesi_id = k.kullanıcı_id " +
                         "WHERE r.öğrenci_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ogrenciId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("randevu_tarihi"),
                    rs.getString("randevu_saati"),
                    rs.getString("hoca_email"),
                    rs.getString("durum")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isRandevuAlinmis(Connection conn, int ogretimId, String tarih, String saat) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Randevular WHERE öğretim_üyesi_id = ? AND randevu_tarihi = ? AND randevu_saati = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, ogretimId);
        ps.setString(2, tarih);
        ps.setString(3, saat);
        ResultSet rs = ps.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    }

    private int getUserIdByEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT kullanıcı_id FROM Kullanıcılar WHERE kullanıcı_adı = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("kullanıcı_id") : -1;
    }

    public JPanel getRandevuPanel() {
        return randevuAnaPanel;
    }
}