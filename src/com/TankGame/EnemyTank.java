package com.TankGame;

import javax.swing.JApplet;

/**
 * 敵方坦克
 * @author LU-YITING(I2A24 49906124)
 * @version 1.0
 */
public class EnemyTank extends Tank implements Runnable {

    /**
     * 目標敵人
     */
    private Tank targetTank;
    /**
     * 儲存所要畫圖的視窗
     */
    JApplet Windows;
    /**
     * X軸差距
     */
    int xGap;
    /**
     * Y軸差距
     */
    int yGap;
    /**
     * 動作間隔時間(毫秒)
     */
    int sleepTime;
    /**
     * 攻擊力
     */
    int attack;

    /**
     * 建構子
     * @param x X座標
     * @param y Y座標
     * @param sleepTime 動作間隔時間
     * @param attack 攻擊力
     * @param j JApplet
     * @param target 目標敵人
     */
    public EnemyTank(int x, int y, int sleepTime, int attack, JApplet j, Tank target) {
        super(x, y, j);
        setTarget(target);
        this.sleepTime = sleepTime;
        this.attack = attack;
        this.Windows = j;
        for (int i = 0; i < 4; i++) {
            loadImage("picture/enemytank" + (i + 1) + ".png", j, i);//匯入圖片，圖片放於picture資料夾中
        }
        //System.out.println("EnemyTank _ NO.:" + i + "  inti:" + e.getX() + "," + e.getY());
    }

    /**
     * 設定攻擊力
     * @param target 攻擊力
     */
    private void setTarget(Tank target) {
        this.targetTank = target;
    }

    /**
     * AI
     */
    @Override
    public void run() {
        Directio directionTrack;
        while (true) {
            if (getLife() <= 0) {  //死掉了
                getAllOtherGameObjects().remove(this);  //remove後由GC回收
                break;
            }
            switch (directionTrack = directionTrack()) {  //判斷敵人方位
                case UP:    //如果是在四面則對目標發射子彈
                case DOWN:
                case RIGHT:
                case LEFT:
                    this.directio = directionTrack;
                    fire(Windows);
                    break;
                case RIGHT_UP:  //如果是斜角，則由XGap.YGap判斷較短的距離
                    if (xGap + yGap <= 0) {
                        directio = Directio.UP;
                    } else {
                        directio = Directio.RIGHT;
                    }
                    if (!this.contact(this.targetTank, this.directio)) {
                        //如果沒有交集則嘗試移動
                        tryMove();  
                    }
                    break;
                case RIGHT_DOWN:
                    if (xGap - yGap >= 0) {
                        directio = Directio.RIGHT;
                    } else {
                        directio = Directio.DOWN;
                    }
                    if (!this.contact(this.targetTank, this.directio)) {
                        tryMove();
                    }
                    break;
                case LEFT_UP:
                    if (xGap - yGap <= 0) {
                        directio = Directio.LEFT;
                    } else {
                        directio = Directio.UP;
                    }
                    if (!this.contact(this.targetTank, this.directio)) {
                        tryMove();
                    }
                    break;
                case LEFT_DOWN:
                    if (xGap + yGap <= 0) {
                        directio = Directio.LEFT;
                    } else {
                        directio = Directio.DOWN;
                    }
                    if (!this.contact(this.targetTank, this.directio)) {
                        //
                        tryMove();
                    }
                    break;
            }
            reRectangle();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * 方向判斷
     * @return 方向
     */
    private Directio directionTrack() {
        Directio direction = null;
        xGap = getX() - this.targetTank.getX();
        yGap = getY() - this.targetTank.getY();
        if (xGap > 0) {
            if (yGap == 0) {
                direction = Directio.LEFT;
            } else if (yGap < 0) {
                direction = Directio.LEFT_DOWN;
            } else if (yGap > 0) {
                direction = Directio.LEFT_UP;
            }
        } else if (xGap == 0) {
            if (yGap < 0) {
                direction = Directio.DOWN;
            } else if (yGap > 0) {
                direction = Directio.UP;
            }
        } else if (xGap < 0) {
            if (yGap == 0) {
                direction = Directio.RIGHT;
            } else if (yGap < 0) {
                direction = Directio.RIGHT_DOWN;
            } else if (yGap > 0) {
                direction = Directio.RIGHT_UP;
            }
        }
        return direction;
    }

    /**
     * 敵人坦克專用發射子彈，無彈藥數、過熱等設計
     * @param j JApplet
     */
    @Override
    public void fire(JApplet j) {
        Missile missile = new Missile(getX(), getY(), 5, 75, directio, getAllOtherGameObjects(), j);
        missiles.add(missile);
        Thread t = new Thread(missile);
        t.start();
    }

    /**
     * 嘗試移動
     */
    private void tryMove() {
        boolean canMove = true;
        for (int i = 0; i < getAllOtherGameObjects().size(); i++) {
            //System.out.println("allGameObjects_SIZE:"+getAllOtherGameObjects().size());
            if (getAllOtherGameObjects().get(i) != this) {//如果碰撞到物件
                if (contact(getAllOtherGameObjects().get(i), directio)) {
                    canMove = false;//則不可移動
                }
            }
        }
        if (canMove) {
            move();
        }
    }
}