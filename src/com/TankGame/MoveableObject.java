package com.TankGame;

import java.awt.*;
import javax.swing.JApplet;

/**
 * 可以移動的遊戲物件
 * 
 * @author LU-YITING(I2A24 49906124)
 * @version 1.1
 */
public class MoveableObject extends GameObject {

    /**
     * 可移動之遊戲物件，基本會有四張圖片代表四個方向
     */
    Image img[] = new Image[4];
    /**
     * 用於處存各方向專用的數值，便於之後讀取img[]用
     */
    int directionInteger;
    /**
     * 物件的方向
     */
    Directio directio = Directio.UP;
    /**
     * 位移量
     */
    private int delta;
    
    /**
     * 建構子
     * @param x X座標
     * @param y Y座標
     * @param width 寬
     * @param hight 高 
     * @param delta 位移量
     */
    public MoveableObject(int x, int y, int width, int hight, int delta) {
        super(x, y, width, hight);  //呼叫GameObject的建構子
        setDelta(delta);  //設定位移量
    }

    /**
     * 讀入圖片。
     * @param name 圖片名
     * @param a JApplet
     * @param i 所要匯入的陣列位置
     */
    public void loadImage(String name, JApplet a, int i) {
        img[i] = a.getImage(a.getDocumentBase(), name);  //匯入圖片
    }

    /**
     * 移動
     */
    public void move() {  //根據方向移動座標
        if (directio == Directio.LEFT) {
                setX(getX() - delta);
        } else if (directio == Directio.RIGHT) {
                setX(getX() + delta);
        } else if (directio == Directio.DOWN) {
                setY(getY() + delta);
        } else if (directio == Directio.UP) {
                setY(getY() - delta);
        }
    }
    /**
     * 畫出圖片
     * @param g Graphics
     * @param a JApplet
     */
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
        drawLife(g);//畫出生命力提示塊
    }
    /**
     * 畫出生命力提示塊
     * @param g Graphics
     */
    protected void drawLife(Graphics g){
        int life=(int)((getLife()/1000.0)*40);  //計算生命力%數
        if(life<=10)  //少於1/4則以紅血表示
            g.setColor(Color.red);
        else if(life<=20)  //少於1/2則以黃寫表示
            g.setColor(Color.YELLOW);
        else  //大於1/2以率寫表示
            g.setColor(Color.GREEN);
        g.fillRect(getX(),getY()+70,life,10);
        g.setColor(Color.BLACK);  
        g.drawLine(getX(),getY()+70, getX()+40,getY()+70);
        g.drawLine(getX()+40,getY()+70,getX()+40,getY()+80);
        g.drawLine(getX()+40,getY()+80, getX(),getY()+80);
        g.drawLine(getX(),getY()+80, getX(),getY()+70);
    }
    
    /**
     * 即將碰撞比較
     * @param g 要比較的GameObject物件
     * @param d 要移動的方向
     * @return 移動後是否會發生碰撞
     */
    public boolean contact(GameObject g,Directio d){
        Rectangle r = new Rectangle(getRectangle());  //複製一個rectangle的複本
        //System.out.println("rx:"+r.x+" ry:"+r.y+" rw:"+r.width+" rh:"+r.height+
        //        "\ngx:"+g.getRectangle().x+" gy:"+g.getRectangle().y+" gw:"+g.getRectangle().width+" gh:"+g.getRectangle().height);
        switch(d){  //依據方向將複本rectangle設定成移動之後的rectangle
            case UP:
                r.y-=this.getDelta();
                break;
            case DOWN:
                r.y+=this.getDelta();
                break;
            case RIGHT:
                r.x+=this.getDelta();
                break;
            case LEFT:
                r.x-=this.getDelta();
                break;
        }
        return r.intersects(g.getRectangle());  
        //回傳比較是否移動後的rectangle會和目標rectangle交集
    }

    /**
     * 回傳位移量
     * @return 位移量
     */
    public int getDelta() {
        return delta;
    }

    /**
     * 設定位移量
     * @param delta 位移量
     */
    public void setDelta(int delta) {
        if (delta > 0) {  //位移量需大於0
            this.delta = delta;
        }
    }
}

/**
 * 將四個方向數值化，用於方便讀取讀片。
 * @author LU-YITING(I2A24 49906124)
 * @version 1.0
 */
class DirectioInteger {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;
}

/**
 * 四個方向。
 * @author LU-YITING(I2A24 49906124)
 * @version 1.0
 */
enum Directio {

    UP, DOWN, RIGHT, LEFT, RIGHT_UP, LEFT_UP, RIGHT_DOWN, LEFT_DOWN
}
