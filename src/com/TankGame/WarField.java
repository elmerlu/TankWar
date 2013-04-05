package com.TankGame;

import java.util.ArrayList;
import javax.swing.JApplet;

/**
 * 戰場，內函戰場所需物件
 * @author LU-YITING(I2A24 49906124)
 * @version 1.0
 */
public class WarField extends GameObject {
    /**
     * 儲存戰場上其他的物件，用於碰撞判斷
     */
    ArrayList<GameObject> allGameObjects = new ArrayList<GameObject>();
    /**
     * 儲存戰場上已經陣亡的坦克所發射出去的子彈
     */
    ArrayList<Missile> tmpMissiles = new ArrayList<Missile>();
    /**
     * 一輛玩家坦克
     */
    PlayerTank playerTank1;
    /**
     * 敵人坦克
     */
    ArrayList<EnemyTank> enemyTank = new ArrayList<EnemyTank>();
    /**
     * 非移動物件
     */
    ArrayList<Stone> nonmoveArrayList = new ArrayList<Stone>();

    /**
     * 建構子。
     * @param rank 難易度
     * @param j JApplet
     */
    public WarField(Rank rank,JApplet j) {
        super(0, 0, 800, 600);
        this.playerTank1 = new PlayerTank(40, 40, j);//坦克實體
        allGameObjects.add(this.playerTank1);  //放入儲存戰場物件的Array，用於碰撞判斷
        int[][] data = {{5, 500}, {100, 0}, {160, 240}, {320, 400}, {400, 160}
                , {400, 520}, {640, 240}, {560, 240}, {280, 160}, {720, 160}, {160, 360}};
        //[0][0]=敵人數量，[0][1]=敵方坦克AI執行動作所間隔的時間(毫秒)
        //[1][0]=敵人子彈攻擊力，其餘為敵方坦克的座標
        switch(rank){  //依據難易度決定WarField的資料
            case EASY:
                break;
            case MEDIUM:
                data[0][0]=7;
                data[0][1]=400;
                data[1][0]=150;
                break;
            case HARD:
                data[0][0]=9;
                data[0][1]=340;
                data[1][0]=200;
        }
        for (int i = 0; i < data[0][0]; i++) {  //依據資料產生敵人，並放入Array中
            EnemyTank e = new EnemyTank(data[i+2][0],data[i+2][1],data[0][1],data[1][0],j, playerTank1);
            enemyTank.add(e);
            allGameObjects.add(this.enemyTank.get(i));
        }
        for (int i = 0; i < 15; i++) {  //戰場石頭(不可破壞)
            if (i == 0 || i == 14) {
                for (int ii = 0; ii < 20; ii++) {
                    Stone s = new Stone(ii * 40, i * 40, j);
                    nonmoveArrayList.add(s);
                    allGameObjects.add(s);
                }
            } else {
                Stone s = new Stone(0, i * 40, j);
                nonmoveArrayList.add(s);
                allGameObjects.add(s);
                s = new Stone(760, i * 40, j);
                nonmoveArrayList.add(s);
                allGameObjects.add(s);
            }
        }
        Stone[] s = new Stone[]{new Stone(80, 80, j), new Stone(120, 80, j), new Stone(120, 80, j), new Stone(160, 80, j),
            new Stone(160, 120, j), new Stone(320, 120, j), new Stone(360, 120, j), new Stone(400, 120, j), new Stone(440, 120, j),
            new Stone(480, 120, j), new Stone(600, 120, j), new Stone(600, 160, j), new Stone(120, 200, j), new Stone(400, 200, j),
            new Stone(600, 200, j), new Stone(120, 240, j), new Stone(400, 240, j), new Stone(600, 240, j), new Stone(120, 280, j),
            new Stone(160, 280, j), new Stone(200, 280, j), new Stone(240, 280, j), new Stone(400, 280, j), new Stone(600, 280, j),
            new Stone(640, 280, j), new Stone(120, 320, j), new Stone(400, 320, j), new Stone(120, 440, j), new Stone(200, 440, j),
            new Stone(240, 440, j), new Stone(280, 440, j), new Stone(560, 440, j), new Stone(600, 440, j), new Stone(640, 440, j),
            new Stone(120, 280, j), new Stone(160, 280, j), new Stone(200, 280, j), new Stone(120, 240, j), new Stone(400, 240, j),
            new Stone(600, 440, j), new Stone(120, 480, j), new Stone(440, 520, j)};
        for (int i = 0; i < s.length; i++) {  //將allGameObjects的內容設定完
            nonmoveArrayList.add(s[i]);
            allGameObjects.add(s[i]);
        }
        playerTank1.setAllOtherGameObjects(allGameObjects);//將allGameObjects的內容給予各物件
        for (int i = 0; i < this.enemyTank.size(); i++) {
            enemyTank.get(i).setAllOtherGameObjects(allGameObjects);
        }
    }

    /**
     * 各敵方坦克啟動
     */
    public void enemyTankStart() {
        for (int i = 0; i < enemyTank.size(); i++) {
            new Thread(enemyTank.get(i)).start();
        }
    }
}
/**
 * 難易度
 * @author LU-YITING
 * @version 1.0
 */
enum Rank{
    EASY,MEDIUM,HARD
}