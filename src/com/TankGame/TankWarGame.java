/*
 * 同步化區塊問題尚未解決
 * 子彈執行序過多，導致LAG
*/
package com.TankGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 坦克大戰
 * @author LU-YITING(I2A24 49906124)
 * @version 1.1
 */
public class TankWarGame extends JApplet implements KeyListener, Runnable {

    WarField tankWar;//戰場
    JLabel[] jl = {new JLabel("座標:"), new JLabel(), new JLabel("剩餘子彈(換彈請按R<3秒>):"), new JLabel(),
        new JLabel("機槍過熱"), new JLabel(), new JLabel("敵人數量:"), new JLabel(), new JLabel("生命力:"), new JLabel()};
    //顯示提示訊息
    Panel jp = new Panel();//容器

    /**
     * JApplet載入時所執行。
     */
    @Override
    public void init() {
        Object choose = JOptionPane.showInputDialog(null, "Please choose rank", "Example 1",
                JOptionPane.QUESTION_MESSAGE, null, new Object[]{"EASY", "MEDIUM", "HARD"}, "EASY");
        Rank rank = Rank.EASY;//難易度的選單
        if (choose == null) {//依據各難易度進行初始化
            System.exit(0);
        } else if (choose.toString().equals("EASY")) {
            rank = Rank.EASY;
        } else if (choose.toString().equals("MEDIUM")) {
            rank = Rank.MEDIUM;
        } else if (choose.toString().equals("HARD")) {
            rank = Rank.HARD;
        }
        tankWar = new WarField(rank, this);//匯入戰場
        setFocusable(true);//將焦點開啟，避免有時鍵盤無法觸動
        addKeyListener(this);//登記鍵盤監聽者
        for (int i = 0; i < jl.length; i++) {
            jp.add(jl[i]);//匯入至容器內
        }
        add(jp, BorderLayout.NORTH);//將容器匯入至JApplet上方
        Thread t = new Thread(this);
        t.start();
        tankWar.enemyTankStart();//敵方AI啟動
    }

    /**
     * 繪圖。
     * @param g Graphics
     */
    @Override
    public void paint(Graphics g) {
        update(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());//先畫背景
        //System.out.println(tankWar.playerTank1.missiles.size());
        for (int i = 0; i < tankWar.nonmoveArrayList.size(); i++) {
            Stone a = tankWar.nonmoveArrayList.get(i);
            a.nonMoveDraw(a.getImg(), g, this);
        }//先畫出所有非移動物件
        tankWar.playerTank1.draw(g, this);//畫出坦克
        //畫出玩家發射的子彈
        for (int i = 0; i < tankWar.playerTank1.missiles.size(); i++) {
            //System.out.println("size:" + tankWar.playerTank1.missiles.size());
            Missile m = tankWar.playerTank1.missiles.get(i);
            if (!m.getIsBomb()) {  //子彈爆炸
                m.draw(g, this);
            } else {
                tankWar.playerTank1.missiles.remove(i);  //移除，由GC回收
                i--;
            }
        }
        for (int i = 0; i < tankWar.enemyTank.size(); i++) {  //畫出敵方坦克
            EnemyTank t = tankWar.enemyTank.get(i);
            //System.out.println("EnemyTank _ NO."+i+"  "+t.getX()+","+t.getY());
            if (t.getLife() > 0) {//還活著
                t.draw(g, this);
                for (int j = 0; j < t.missiles.size(); j++) {
                    //System.out.println("Esize:" + tankWar.enemyTank.get(i).missiles.size());
                    Missile m = t.missiles.get(j);
                    if (!m.getIsBomb()) {
                        m.draw(g, this);
                    } else {
                        t.missiles.remove(j);
                        j--;
                    }
                }
            } else {  //敵方坦克死亡，將其已經發射的子彈移入站存子彈Array
                tankWar.tmpMissiles.addAll(t.missiles);
                //System.out.println("enemyTank _ NO."+i+"->remove:"+tankWar.allGameObjects.remove(t));
                tankWar.enemyTank.remove(i);
            }
            for (int j = 0; j < tankWar.tmpMissiles.size(); j++) {
                //System.out.println("Esize:" + tankWar.enemyTank.get(i).missiles.size());
                Missile m = tankWar.tmpMissiles.get(j);
                if (!m.getIsBomb()) {
                    m.draw(g, this);
                } else {
                    tankWar.tmpMissiles.remove(j);
                    j--;
                }
            }
        }//設定各提示訊息
        jl[1].setText(tankWar.playerTank1.getX() + "," + tankWar.playerTank1.getY());
        jl[3].setText(tankWar.playerTank1.getMissileCount() + "");
        jl[5].setText(tankWar.playerTank1.getIsOverheat() + "");
        jl[7].setText(tankWar.enemyTank.size() + "");
        jl[9].setText(tankWar.playerTank1.getLife() + "");
    }

    @Override
    public void update(Graphics g) //重載    將畫面幀數 降低  減弱閃爍
    {
        Color bg = getBackground();
        Dimension d = size();
        int h = d.height / 2;

        for (int x = 0; x < d.width; x++) {
            int frame = 0;
            int y1 = (int) ((1.0 + Math.sin((x - frame) * 0.05)) * h);
            int y2 = (int) ((1.0 + Math.sin((x + frame) * 0.05)) * h);
            if (y1 > y2) {
                int t = y1;
                y1 = y2;
                y2 = t;
            }
            g.setColor(bg);
        }
    }

    /**
     * 刷新畫面，延遲50毫秒
     */
    @Override
    public void run() {
        while (true) {
            try {
                repaint();
                tankWar.playerTank1.reRectangle();
                if (tankWar.playerTank1.getLife() <= 0) {  //玩家死亡
                    javax.swing.JOptionPane.showMessageDialog(rootPane, "你死掉了");
                    break;
                }
                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println("error");
            }
            System.gc();  //建議GC開始進行資源回收
        }
    }

    /**
     * 無實作
     * @param e KeyEvent
     * @deprecated 
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * 按下鍵盤，根據方向鍵使坦克移動。
     * @param e KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //判斷方向並做相對應的處理，改變方向、移動等
        synchronized (tankWar.playerTank1) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (tankWar.playerTank1.directio != Directio.UP) {//如果方向與觸動方向不同
                        tankWar.playerTank1.directio = Directio.UP;//則只進行轉向
                    } else {
                        tryMove();
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (tankWar.playerTank1.directio != Directio.DOWN) {
                        tankWar.playerTank1.directio = Directio.DOWN;
                    } else {
                        tryMove();
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (tankWar.playerTank1.directio != Directio.RIGHT) {
                        tankWar.playerTank1.directio = Directio.RIGHT;
                    } else {
                        tryMove();
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (tankWar.playerTank1.directio != Directio.LEFT) {
                        tankWar.playerTank1.directio = Directio.LEFT;
                    } else {
                        tryMove();
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    tankWar.playerTank1.fire(this);

                    break;
                case KeyEvent.VK_R:
                    tankWar.playerTank1.newClip();
                    break;
            }
        }
    }

    /**
     * 無實作
     * @param e KeyEvent
     * @deprecated 
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void tryMove() {
        boolean canMove = true;
        for (int i = 0; i < tankWar.enemyTank.size(); i++) {
            if (tankWar.playerTank1.contact(tankWar.enemyTank.get(i), tankWar.playerTank1.directio)) {
                canMove = false;
            }
        }
        for (int i = 0; i < tankWar.nonmoveArrayList.size(); i++) {
            if (tankWar.playerTank1.contact(tankWar.nonmoveArrayList.get(i), tankWar.playerTank1.directio)) {
                canMove = false;
            }
        }
        if (canMove) {

            tankWar.playerTank1.move();//相同則移動
        }
    }
}
