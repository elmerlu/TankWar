package com.TankGame;

import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JApplet;

/**
 * 子彈
 * @author LU-YITING
 * @version 1.0
 */
public class Missile extends MoveableObject implements Runnable {

    /**
     * 子彈移動的頻率(一秒內)
     */
    private int frequency = 5;
    /**
     * 子彈的攻擊力
     */
    private int attack=100;
    /**
     * 子彈是否已經引爆
     */
    private boolean isBomb=false;

    /**
     * 建構子
     * @param x X座標
     * @param y Y座標
     * @param directio 方向
     * @param allGameObjects 儲存戰場上其他的物件，用於碰撞判斷
     * @param j JApplet
     */
    public Missile(int x, int y, Directio directio,ArrayList<GameObject> allGameObjects, JApplet j) {
        this(x, y, 30, 5, directio,allGameObjects, j);
    }

    /**
     * 建構子
     * @param x X座標
     * @param y Y座標
     * @param delta 位移量
     * @param frequency 頻率
     * @param directio 方向
     * @param allGameObjects 儲存戰場上其他的物件，用於碰撞判斷
     * @param j JApplet
     */
    public Missile(int x, int y, int delta, int frequency, Directio directio,ArrayList<GameObject> allGameObjects, JApplet j) {
        super(x,y,12,12,delta);
        if (directio == Directio.UP) {  //依據方向，設定子彈初始位置
            y -= 12;
            x += 14;
        } else if (directio == Directio.DOWN) {
            y += 40;
            x += 14;
        } else if (directio == Directio.RIGHT) {
            y += 14;
            x += 40;
        } else if (directio == Directio.LEFT) {
            y += 14;
            x -= 12;
        }
        setX(x);  //重新設定子彈座標
        setY(y);
        reRectangle();  //刷新Rectangle
        //System.out.println("Missile:"+getX()+","+getY());
        this.directio = directio;  //設定方向
        this.frequency = frequency;  //設定頻率
        for (int i = 0; i < 4; i++) {  //匯入是個放向的圖片
            loadImage("picture/shells.png", j, i);//匯入圖片，圖片放於picture資料夾中
        }
        setAllOtherGameObjects(allGameObjects);  //設定戰場上其他的物件
    }

    /**
     * 執行續，進行子彈的移動，爆炸，及傷害
     */
    @Override
    public void run() {
        boolean canMove;  //判斷是否可以繼續移動
        boolean b=true;  //判斷是否已經爆炸
        while (true) {
            canMove=true;
            try {
                Thread.sleep(1000 / frequency);  //達成一秒內要移動的次數
            } catch (InterruptedException ex) {
            }
            reRectangle();  //刷新Rectangle
            //移動前先與戰場上其他的物件進行碰撞判斷
            for(int i=0;i<getAllOtherGameObjects().size();i++){
                GameObject g=getAllOtherGameObjects().get(i);
                b=contact(g, directio);//有碰撞即為砲彈爆炸                
                if(b){  //爆炸
                    canMove=false;  //不能繼續移動
                    isBomb=true;  //設定已爆炸
                    if(g.isDestructable())  //如果碰撞的目標是可破壞的
                        g.setLife((g.getLife()-attack));  //則削減其生命力
                }
            }
            if(canMove)  //可移動
                move();
            else
                break;
        }
        //執行續結束
    }

    /**
     * 畫出圖片，子彈專用版
     * @param g Graphics
     * @param a JApplet
     */
    @Override
    public void draw(Graphics g, JApplet a) {
        //先判斷方向再將對應的圖片畫出
        if (directio == Directio.UP) {
            directionInteger = DirectioInteger.UP;
        } else if (directio == Directio.DOWN) {
            directionInteger = DirectioInteger.DOWN;
        } else if (directio == Directio.RIGHT) {
            directionInteger = DirectioInteger.RIGHT;
        } else if (directio == Directio.LEFT) {
            directionInteger = DirectioInteger.LEFT;
        }
        g.drawImage(img[directionInteger], getX(), getY() + 30, a);
        //+30為上方顯示座標會被蓋住，故將基準座標Y+30
    }
    
    /**
     * 傳回是否已經爆炸
     * @return 爆炸-true，未爆炸-false
     */
    public boolean getIsBomb(){
        return this.isBomb;
    }
}
