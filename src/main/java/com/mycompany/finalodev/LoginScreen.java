package com.mycompany.finalodev;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;

public class LoginScreen {
    private JTextField kullanıcıAdiField;
    private JPasswordField şifreField;
    private JButton girişButton;
    private JButton kayıtOlButton;
    private JComboBox<String> kullanıcıTürüCombo;
    JPanel loginPanel;

    private KullanıcıGiris kullanıcıGiris;

    public LoginScreen() {
        kullanıcıGiris = new KullanıcıGiris();

        // Bileşenleri oluştur
        String imagePath = "/Users/efe/Desktop/Finalodev/src/main/java/com/mycompany/finalodev/Kahverengi & Bej Sade Takı Markası Logo.png";
loginPanel = new BackgroundPanel(imagePath);
        loginPanel.setLayout(new GridLayout(4, 2, 10, 10));

        kullanıcıAdiField = new JTextField();
        kullanıcıAdiField.setOpaque(false);
        kullanıcıAdiField.setForeground(Color.WHITE);
        şifreField = new JPasswordField();
        kullanıcıTürüCombo = new JComboBox<>(new String[]{"Öğrenci", "Öğretim Üyesi"});
        girişButton = new JButton("Giriş Yap");
        girişButton.setOpaque(false);
        girişButton.setContentAreaFilled(false);  // Arka plan dolgusunu kaldır
girişButton.setBorderPainted(false);      // Çerçeve çizilmesini engelle
girişButton.setFocusPainted(false);       // Tıklanınca gelen çerçeveyi engelle
girişButton.setOpaque(false);             // Opaque olmasın (arka planı göstermesin)
girişButton.setForeground(Color.blue);   // Yazı rengi beyaz olsun (arka plan resmine göre ayarla)
        kayıtOlButton = new JButton("Kayıt Ol");
        kayıtOlButton.setContentAreaFilled(false);
kayıtOlButton.setBorderPainted(false);
kayıtOlButton.setFocusPainted(false);
kayıtOlButton.setOpaque(false);
kayıtOlButton.setForeground(Color.RED);

kullanıcıAdiField.setOpaque(false);                // Arka planı saydam yap
kullanıcıAdiField.setBackground(new Color(0,0,0,0)); // Tam şeffaf arka plan
                
kullanıcıAdiField.setForeground(Color.BLACK);

kullanıcıAdiField.setBorder(new LineBorder(Color.BLACK, 1)); // İnce siyah çerçeve
        şifreField.setBorder(new LineBorder(Color.BLACK, 1));

şifreField.setOpaque(false);
şifreField.setBackground(new Color(0,0,0,0));




şifreField.setForeground(Color.WHITE);

kullanıcıTürüCombo.setOpaque(false);
kullanıcıTürüCombo.setBackground(new Color(0,0,0,0));
//kullanıcıTürüCombo.setForeground(Color.WHITE);
// Yazı rengini beyaz yap
        UIManager.put("ComboBox.background", new Color(0, 0, 0, 0));  // Menüyü şeffaf yap
UIManager.put("ComboBox.selectionBackground", new Color(0, 0, 0, 0));  // Seçim alanını şeffaf yap
UIManager.put("ComboBox.selectionForeground", Color.blue);  // Seçilen öğeyi beyaz yap


        // Bileşenleri panele ekle
        // Kalın ve büyük font
Font labelFont = new Font("Arial", Font.BOLD, 16);

// Bileşenleri panele ekle
JLabel kullanıcıAdiLabel = new JLabel("Kullanıcı Adı:");
kullanıcıAdiLabel.setFont(labelFont); // Fontu ayarla
loginPanel.add(kullanıcıAdiLabel);
loginPanel.add(kullanıcıAdiField);

JLabel şifreLabel = new JLabel("Şifre:");
şifreLabel.setFont(labelFont); 
kullanıcıAdiField.setBorder(new LineBorder(Color.BLACK, 1)); // İnce siyah çerçeve
        şifreField.setBorder(new LineBorder(Color.BLACK, 1));// Fontu ayarla
loginPanel.add(şifreLabel);
loginPanel.add(şifreField);

JLabel kullanıcıTürüLabel = new JLabel("Kullanıcı Türü:");
kullanıcıTürüLabel.setFont(labelFont); // Fontu ayarla
loginPanel.add(kullanıcıTürüLabel);
loginPanel.add(kullanıcıTürüCombo);

loginPanel.add(girişButton);
loginPanel.add(kayıtOlButton);

        // Giriş butonuna tıklanma işlemi
        girişButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kullanıcıAdi = kullanıcıAdiField.getText();
                String şifre = new String(şifreField.getPassword());
                String kullanıcıTürü = (String) kullanıcıTürüCombo.getSelectedItem();

                if (kullanıcıGiris.kullanıcıGirişYap(kullanıcıAdi, şifre, kullanıcıTürü)) {
                    String kullanıcıTipi = kullanıcıGiris.getKullanıcıTürü(kullanıcıAdi);
                    JOptionPane.showMessageDialog(null, "Giriş başarılı!\nKullanıcı Tipi: " + kullanıcıTipi);

                    // Kullanıcı tipine göre yönlendirme
                    if (kullanıcıTipi != null) {
                        if (kullanıcıTipi.equalsIgnoreCase("Öğrenci")) {
                            // Öğrenci panelini aç
                            JFrame ogrenciFrame = new JFrame("Öğrenci Paneli");
                            // Öğrenci için randevu paneli açılması sağlanıyor
                            ogrenciFrame.setContentPane(new RandevuPanel(kullanıcıAdi).getRandevuPanel());
                            ogrenciFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            ogrenciFrame.pack();
                            ogrenciFrame.setVisible(true);
                        } else if (kullanıcıTipi.equalsIgnoreCase("Öğretim Üyesi")) {
                            // Öğretim Üyesi panelini aç
                            JFrame ogretmenFrame = new JFrame("Öğretim Üyesi Paneli");
                            ogretmenFrame.setContentPane(new OgretimUyesiPanel(kullanıcıAdi).getOgretimUyesiPanel());
                            ogretmenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            ogretmenFrame.pack();
                            ogretmenFrame.setVisible(true);
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Giriş başarısız! Kullanıcı adı veya şifre hatalı.");
                }
            }
        });

        // Kayıt ol butonuna tıklanma işlemi
        kayıtOlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kayıt ekranına geçiş
                JFrame kayıtFrame = new JFrame("Kayıt Ol");
                kayıtFrame.setContentPane(new RegisterScreen().getRegisterPanel());
                kayıtFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                kayıtFrame.pack();
                kayıtFrame.setVisible(true);
            }
        });
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public static void main(String[] args) {
        // Login ekranını açmak için ana fonksiyon
        JFrame frame = new JFrame("Kullanıcı Girişi");
        frame.setContentPane(new LoginScreen().getLoginPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}