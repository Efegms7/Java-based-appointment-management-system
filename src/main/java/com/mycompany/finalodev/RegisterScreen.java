package com.mycompany.finalodev;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen {
    private JTextField kullanıcıAdiField;
    private JPasswordField şifreField;
    private JTextField emailField;
    private JTextField soyadField;
    private JComboBox<String> kullanıcıTipiCombo;
    private JButton kaydetButton;
    private JPanel registerPanel;

    private KullanıcıKayit kullanıcıKayit;

    public RegisterScreen() {
        kullanıcıKayit = new KullanıcıKayit();

        // Panel ve bileşenleri oluştur
        registerPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        kullanıcıAdiField = new JTextField();
        şifreField = new JPasswordField();
        emailField = new JTextField();
        soyadField = new JTextField();
        kullanıcıTipiCombo = new JComboBox<>(new String[]{"Öğrenci", "Öğretim Üyesi"});
        kaydetButton = new JButton("Kaydet");

        registerPanel.add(new JLabel("Kullanıcı Adı:"));
        registerPanel.add(kullanıcıAdiField);
        registerPanel.add(new JLabel(" Şifre:"));
        registerPanel.add(şifreField);
        registerPanel.add(new JLabel("Mail:"));
        registerPanel.add(emailField);
        registerPanel.add(new JLabel("Soyad:"));
        registerPanel.add(soyadField);
        registerPanel.add(new JLabel("Kullanıcı Türü:"));
        registerPanel.add(kullanıcıTipiCombo);
        registerPanel.add(new JLabel()); // boşluk
        registerPanel.add(kaydetButton);

        // Kaydet butonuna tıklanma işlemi
        kaydetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kullanıcıAdi = kullanıcıAdiField.getText();
                String şifre = new String(şifreField.getPassword());
                //String ad = adField.getText();
                String soyad = soyadField.getText();
                String email = emailField.getText(); 
                String kullanıcıTipi = (String) kullanıcıTipiCombo.getSelectedItem();

                if (kullanıcıKayit.kullanıcıKaydet(kullanıcıAdi, şifre,  soyad,email, kullanıcıTipi)) {
                    JOptionPane.showMessageDialog(null, "Kayıt başarılı!");

                    // Giriş ekranına yönlendirme
                    JFrame loginFrame = new JFrame("Kullanıcı Girişi");
                    loginFrame.setContentPane(new LoginScreen().getLoginPanel());
                    loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    loginFrame.pack();
                    loginFrame.setVisible(true);

                    // Şu anki pencereyi kapat
                    SwingUtilities.getWindowAncestor(registerPanel).dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Kayıt başarısız! Lütfen tekrar deneyin.");
                }
            }
        });
    }

    public JPanel getRegisterPanel() {
        return registerPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Kullanıcı Kaydı");
        frame.setContentPane(new RegisterScreen().getRegisterPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}