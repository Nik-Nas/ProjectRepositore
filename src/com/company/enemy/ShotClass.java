package com.company.enemy;

import com.company.view.GUI;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ShotClass /*TargetShotClass*/
{
    private final Point p = new Point(-1, -1);
    int d = 0;


    public Const randomShot(GUI gui, Const c) throws Exception
    {
        StProc stProc = new StProc();
        SPClass spClass = new SPClass();
        boolean flag = false;
        Random rnd = new Random();
        while (!flag && gui.amtMy > 0)
        {
            int r1 = rnd.nextInt(10);
            int r2 = rnd.nextInt(10);
            if (gui.myf[r1][r2] != 66 && gui.myf[r1][r2] != 44 && gui.myf[r1][r2] != 21 && gui.myf[r1][r2] != 55)
            {
                byte b = stProc.isKill(gui.myf[r1][r2], gui.hpMy);
                //TimeUnit.MILLISECONDS.sleep(500);
                if (b == 2)
                {
                    gui.setEnemyShotText(r1, r2, gui.my, "*");
                    d++;
                    gui.myf[r1][r2] = 66;
                    p.x = r1;
                    p.y = r2;
                    c.dir = stProc.ranDir(new int[]{r1, r2}, c.dir, gui.myf);
                    c = targetShot(gui, c);
                    flag = c.dir != 0;
                    continue;
                } else if (b == 0)
                {
                    gui.setEnemyShotText(r1, r2, gui.my, "\u2022");
                    gui.myf[r1][r2] = 44;
                } else if (b == -1)
                {
                    continue;
                } else
                {
                    gui.setEnemyShotText(r1, r2, gui.my, "*");
                    gui.myf[r1][r2] = 66;
                    gui.amtMy--;
                    spClass.shipRound((byte) 1, r1, r2, false, gui.myf, (byte) 21);
                    d = 0;
                    c.dir = 0;
                    gui.setDead(gui.my, 1, r1, r2 + 1, true, gui.myf);
                    if (gui.amtMy == 0)
                    {
                        gui.winSet("Я выиграл!!!");
                    }
                    else
                    {
                        continue;
                    }
                }
                flag = true;
            }
        }
        return c;
    }

    public Const shot(GUI gui, Const c) throws Exception
    {
        if (p.x == -1)
        {
            c = randomShot(gui,  c);
        } else
        {
            c = targetShot(gui, c);
            if (c.dir == 0 && !c.trueDir)
            {
                c = randomShot(gui, c);
            }
        }
        return c;
    }

    public Const targetShot(GUI gui, Const c) throws Exception/*прострел в заданном направлении*/
    {
        StProc stProc = new StProc();
        if (p.x > -1 && p.y > -1)
        {
            byte xOrY;
            byte addX;
            byte addY;
            byte[] a = stProc.argsSet(c.dir);
            byte b;
            xOrY = a[0];
            addX = a[1];
            addY = a[2];
            int promInd = xOrY == 0 ? p.x + addX : p.y + addY;
            while (true)
            {
                if (promInd > -1 && promInd < 10)
                {
                    if (p.x > -1 && p.x < 10 && p.y > -1 && p.y < 10)
                    {
                        int row = p.x + addX;
                        int col = p.y + addY;
                        if (gui.myf[row][col] != 66 && gui.myf[row][col] != 44 && gui.myf[row][col] != 21 && gui.myf[row][col] != 55)
                        {
                            b = stProc.isKill(gui.myf[row][col], gui.hpMy);
                            gui.setEnemyShotText(row, col, gui.my, b == 2 || b == 1 ? "*" : "\u2022");
                        } else
                        {
                            if (gui.myf[row][col] == 66)
                            {
                                if (xOrY == 0)
                                {
                                    p.x += addX;
                                } else
                                {
                                    p.y += addY;
                                }
                                promInd = xOrY == 0 ? p.x + addX : p.y + addY;
                            } else
                            {
                                if (!c.trueDir)
                                {
                                    c.dir = stProc.ranDir(new int[]{p.x, p.y}, c.dir, gui.myf);
                                } else
                                {
                                    c = stProc.dirSet(p.x, p.y, gui.myf);
                                }
                                a = stProc.argsSet(c.dir);
                                xOrY = a[0];
                                addX = a[1];
                                addY = a[2];
                            }
                            continue;
                        }
                        if (b == 2)
                        {
                            d++;
                            gui.myf[row][col] = 66;
                            if (xOrY == 0)
                            {
                                p.x += addX;
                            } else
                            {
                                p.y += addY;
                            }
                            promInd = xOrY == 0 ? p.x + addX : p.y + addY;
                            c.trueDir = true;
                        } else if (b == 0)
                        {
                            gui.myf[row][col] = 44;
                            if (!c.trueDir)
                            {
                                c.dir = stProc.ranDir(new int[]{p.x, p.y}, c.dir, gui.myf);
                            }
                            return c;
                        } else if (b == 1)
                        {
                            d++;
                            gui.myf[row][col] = 66;
                            int[] coordinates = stProc.edgeFind(d, (c.dir != 1 && c.dir != 2), new int[]{p.x, p.y}, gui.myf, gui.spClass);
                            gui.setDead(gui.my, d, coordinates[0], coordinates[1] + 1, c.dir == 3 || c.dir == 4, gui.myf);
                            gui.amtMy--;
                            p.x = -1;
                            p.y = -1;
                            c.dir = 0;
                            c.trueDir = false;
                            d = 0;
                            if (gui.amtMy == 0)
                            {
                                gui.winSet("Я выиграл!!!");
                            }
                            return c;
                        } else
                        {
                            throw new Exception("TSClass Exception");
                        }
                    }
                } else if (promInd == -1 || promInd == 10)
                {
                    if (!c.trueDir)
                    {
                        c.dir = stProc.ranDir(new int[]{p.x, p.y}, c.dir, gui.myf);
                    } else
                    {
                        switch (c.dir)
                        {
                            case 1 -> c.dir = 2;
                            case 2 -> c.dir = 1;
                            case 3 -> c.dir = 4;
                            case 4 -> c.dir = 3;
                        }
                        a = stProc.argsSet(c.dir);
                        xOrY = a[0];
                        addX = a[1];
                        addY = a[2];
                        promInd = xOrY == 0 ? p.x + addX : p.y + addY;
                    }
                }
            }
        }
        return c;
    }
}