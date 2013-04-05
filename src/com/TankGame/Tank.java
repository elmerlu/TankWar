package com.TankGame;

import java.util.ArrayList;
import javax.swing.JApplet;

/**
 * 坦克
 * @author LU-YITING(I2A24 49906124)
 * @version 1.1
 */
public class Tank extends MoveableObject {

    /**
     * 儲存發射的子彈
     */
    ArrayList<Missile> missiles = new ArrayList<Missile>();
    /**
     * 彈藥數
     */
    private int missileCount = 10;
    /**
     * 熱程度，用於機槍過熱功能
     */
    int overheatDegree;
    /**
     * 機槍過熱(無法發射子彈)
     */
    private boolean isOverheat;

    /**
     * 建構子,匯入坦克四個方向的圖片。
     * @param j JApplet
     */
    public Tank(int x,int y,JApplet j) {
        super(x,y,40,40,5);//位移量設定為5
        isOverheat = false;
    }

    /**
     * 發射子彈
     * @param j JApplet
     */
    public void fire(JApplet j) {
        if (this.missileCount > 0 && !isOverheat) {  //要有彈藥和機槍未過熱方可發射
            overheatDegree += 3;  //過熱指數+3(100為滿)
            setIsOverheat();  //判段並設定是否過熱了
            Missile missile = new Missile(getX(), getY(), 5, 75, directio,getAllOtherGameObjects(), j);
            //將剛發射的子彈存入子彈ArrayList中
            missiles.add(missile);
            Thread t = new Thread(missile); 
            t.start();//讓子彈飛
            missileCount--;  //彈藥-1
        } else if (isOverheat) {  //如果機槍過熱，則開始散熱
            new Thread(new Overheat()).start();
        }
    }

    /**
     * 判斷並設定機槍是否過熱
     */
    public void setIsOverheat() {
        if (overheatDegree >= 100) {  //過熱指數超過100設定為過熱
            isOverheat = true;
        }
    }

    /**
     * 回傳是否過熱
     * @return 過熱-true,未過熱-false
     */
    public boolean getIsOverheat() {
        return isOverheat;
    }

    /**
     * 更換新彈藥夾
     */
    public void newClip() {
        new Thread(new Clip()).start();  //開始換彈藥夾
    }

    /**
     * 彈藥夾
     * @author LU-YITING(I2A24 49906124)
     * @version 1.0
     */
    class Clip extends Thread {

        /**
         * 更換一個新的彈藥夾，需時3秒
         */
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                missileCount = 10;
            } catch (InterruptedException ex) {
            }
            System.out.println("*Change a new clip\n");
        }
    }

    /**
     * 回傳彈藥數
     * @return 彈藥數
     */
    public int getMissileCount() {
        return missileCount;
    }

    /**
     * 設定機槍是否過熱
     * @param isOverheat 是否機槍過熱
     */
    protected void setIsOverheat(boolean isOverheat) {
        this.isOverheat = isOverheat;
    }

    /**
     * 過熱
     * @author LU-YITING(I2A24 49906124)
     * @version 1.0
     */
    class Overheat extends Thread {
        /**
         * 散熱，需時10秒
         */
        @Override
        public void run() {
            System.out.println("*Overheat\n");
            try {
                Thread.sleep(10000);
                setIsOverheat(false);
                overheatDegree = 0;
            } catch (InterruptedException ex) {
            }
            System.out.println("*No overheat\n");
        }
    }
}
