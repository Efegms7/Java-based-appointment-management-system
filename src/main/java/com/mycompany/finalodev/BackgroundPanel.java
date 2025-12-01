/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalodev;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author efe
 */
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            System.out.println("Resim yüklenemedi: " + e.getMessage());
        }
        setLayout(new GridLayout(4, 2, 10, 10)); // Layout'u buraya taşı
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}