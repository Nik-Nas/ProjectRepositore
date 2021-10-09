package com.company.enemy;

import com.company.view.GUI;

import java.util.Random;

public class SPClass
{
    public void shipRound(int decks, int row, int col, boolean direct, byte[][] f, int fill)/*проверено*/
    {
        int a = decks + 2;
        byte place = (byte) fill;
        for (int i = row; i < row + (direct ? 3 : a); i++)
        {
            for (int j = col; j < col + (direct ? a : 3); j++)
            {
                if ((i - 1 >= 0 &&
                        j - 1 >= 0 &&
                        i - 1 < 10 &&
                        j - 1 < 10) &&
                        ((f[i - 1][j - 1] == 0 ||
                                f[i - 1][j - 1] == 99)))
                    f[i - 1][j - 1] = place;
            }
        }
    }

    public boolean shipCanPlace(int decks, int row, int col, boolean direct, byte[][] f)
    {
        for (int j = direct ? col : row, i = 0; i < decks; j++, i++)
        {
            if (j < 10)
            {
                if (f[direct ? row : j][direct ? j : col] != 0)
                {
                    return false;
                }
            } else return false;
        }
        return true;
    }

    public void shipPlace(int decks, int row, int col, boolean direct, byte[][] f, int m) /*проверено*/
    {
        byte m1 = (byte) m;
        for (int j = direct ? col : row, i = 0; i < decks; j++, i++)
        {
            f[direct ? row : j][direct ? j : col] = m1;
        }
        shipRound(decks, row, col, direct, f, 99);
    }

    public void fieldFill(GUI gui/*byte[][] myf*/)
    {
        //gui.myf = new byte[10][10];
        Random rnd = new Random();
        int m = 1;
        boolean dir;
        int row;
        int col;
        byte decks = 4;
        byte amount = 1;
        byte edge = 7;
        for (int i = 0; i < 10; )
        {
            int amt = amount;
            while (amt > 0)
            {
                dir = rnd.nextBoolean();
                if (dir)
                {
                    row = rnd.nextInt(10);
                    col = rnd.nextInt(edge);
                } else
                {
                    row = rnd.nextInt(edge);
                    col = rnd.nextInt(10);
                }
                if (shipCanPlace(decks, row, col, dir, gui.enf))
                {
                    shipPlace(decks, row, col, dir, gui.enf, m);
                    m++;
                    amt--;
                    i++;
                }
                /*if (shipCanPlace(decks, row, col, dir, gui.myf))
                {
                    shipPlace(decks, row, col, dir, gui.myf, m);
                    m++;
                    amt--;
                    i++;
                }*/
            }
            edge++;
            amount++;
            decks--;
        }
    }
}