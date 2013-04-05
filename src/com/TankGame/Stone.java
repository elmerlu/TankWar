package com.TankGame;

import java.awt.Image;
import javax.swing.JApplet;

/**
 * 石頭的類別
 * 
 * @author LU-YITING(I2A24 49906124)
 * @version 1.0
 */
public class Stone extends GameObject{

    /**
     * 圖檔
     */
    private Image img;
    
    /**
     * 建構子
     * @param x X座標
     * @param y Y座標
     * @param widht 寬
     * @param high 高
     * @param j JApplet
     */
    public Stone(int x, int y, int widht, int high,JApplet j) {
        super(x, y, widht, high);
        img=loadImage("picture/stone.png",j);
        this.setDestructable(false);
    }
    
    /**
     * 建構子
     * @param x X座標
     * @param y Y座標
     * @param j JApplet
     */
    public Stone(int x, int y,JApplet j) {
        this(x, y, 40, 40,j);
    }

    /**
     * 取出圖片
     * @return 圖片
     */
    public Image getImg() {
        return img;
    }

    /**
     * 設定圖片
     * @param img 圖片
     */
    public void setImg(Image img) {
        this.img = img;
    }
    
}
