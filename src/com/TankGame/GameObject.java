package com.TankGame;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JApplet;

/**
 * 遊戲的基本物件。
 * 
 * @author LU-YITING(I2A24 49906124)
 * @version 1.1
 */
public class GameObject {

    /**
     * X座標
     */
    private int x;
    /**
     * Y座標
     */
    private int y;
    /**
     * 物件的寬
     */
    private int width;
    /**
     * 物件的高
     */
    private int hight;
    /*
     * 物件的生存力
     */
    private int life=1000;
    /**
     * 物件是否可被破壞
     */
    private boolean destructable=true;
    /**
     * 專屬於各個物件的矩形，用於判斷是否碰撞
     * Rectangle可以比較是否與另一個Rectangle交集
     */
    private Rectangle rectangle;
    /**
     * 儲存戰場上其他的物件，用於碰撞判斷
     */
    private ArrayList<GameObject> allGameObjects;
    
    /**
     * 建構子
     * @param x X座標
     * @param y Y座標
     * @param widht 寬
     * @param high 高
     */
    public GameObject(int x, int y, int widht, int high) {
        setX(x);
        setY(y);
        setWidth(widht);
        setHight(high);
        setRectangle();
    }
    /**
     * 刷新rectangle，因為每次移動之後，座標都會改變
     */
    public void reRectangle(){
        getRectangle().x=getX();
        getRectangle().y=getY();
    }
    /**
     * 用rectangle判斷與目標是否進行碰撞
     * @param g 目標
     * @return 碰撞即回傳true，沒有碰撞則回傳false
     */
    public boolean contact(GameObject g){
        return rectangle.intersects(g.getRectangle());
    }
    /**
     * 用於非繼承MoveableObject的CLASS要畫出物件圖示時用
     * @param img 要畫出的圖
     * @param g Graphics
     * @param a JApplet
     */
    public void nonMoveDraw(Image img,Graphics g, JApplet a) {
        g.drawImage(img, getX(), getY() + 30, a);
        //+30為上方顯示座標會被蓋住，故將基準座標Y+30
    }
    /**
     * 讀入圖片用
     * @param name 路徑名稱
     * @param a JApplet
     * @return 匯入的圖片
     */
    public Image loadImage(String name, JApplet a) {
        return a.getImage(a.getDocumentBase(), name);
    }

    /**
     * 設定X座標
     * @param x X座標
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * 回傳X座標
     * @return X座標
     */
    public int getX() {
        return this.x;
    }

    /**
     * 設定Y座標
     * @param y Y座標
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * 回傳Y座標
     * @return Y座標
     */
    public int getY() {
        return this.y;
    }

    /**
     * 回傳高
     * @return 高
     */
    public int getHight() {
        return hight;
    }

    /**
     * 設定高
     * @param hight 高
     */
    private void setHight(int hight) {
        this.hight = hight;
    }

    /**
     * 回傳寬
     * @return 寬
     */
    public int getWidth() {
        return width;
    }

    /**
     * 設定寬
     * @param width 寬
     */
    private void setWidth(int width) {
        this.width = width;
    }

    /**
     * 設定rectangle
     */
    private void setRectangle() {
        this.rectangle = new Rectangle(x, y, width, hight);
    }

    /**
     * 回傳rectangle
     * @return 物件rectangle
     */
    public Rectangle getRectangle() {
        return this.rectangle;
    }

    /**
     * 取出其他所有物件的ArrayList
     * @return 其他所有物件的ArrayList
     */
    public ArrayList<GameObject> getAllOtherGameObjects() {
        return allGameObjects;
    }

    /**
     * 設定其他所有物件的ArrayList
     * @param allOtherGameObjects 
     */
    public void setAllOtherGameObjects(ArrayList<GameObject> allOtherGameObjects) {
        this.allGameObjects = allOtherGameObjects;
    }

    /**
     * 回傳生命力
     * @return 生命力
     */
    public int getLife() {
        return life;
    }

    /**
     * 設定生命力
     * @param life 生命力
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * 回傳是否為可破壞
     * @return 可破壞-true,不可破壞-false
     */
    public boolean isDestructable() {
        return destructable;
    }

    /**
     * 設定是否可被破壞,預設圍可破壞
     * @param destructable 
     */
    public void setDestructable(boolean destructable) {
        this.destructable = destructable;
    }
}
