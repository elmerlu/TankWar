package com.TankGame;

import javax.swing.JApplet;

/**
 * 玩家坦克
 * @author LU-YITING(I2A24 49906124)
 * @version 1.0
 */
public class PlayerTank extends Tank {

    /**
     * 建構子
     * @param x X座標
     * @param y Y座標
     * @param j JApplet
     */
    public PlayerTank(int x, int y, JApplet j) {
        super(x, y, j);
        for (int i = 0; i < 4; i++) {//匯入圖片，圖片放於picture資料夾中
            loadImage("picture/playertank" + (i + 1) + ".png", j, i);
        }
    }

    @Override
    public synchronized  void move() {
        super.move();
    }
    
    
}
