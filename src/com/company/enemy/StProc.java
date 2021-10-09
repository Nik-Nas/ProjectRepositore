package com.company.enemy;

import java.util.Random;

public class StProc /*ShotProcessingClass*/
{
    public byte isKill(byte hp, byte[] f)
    {
        switch (hp)
        {
            case 1, 2, 3, 4, 5, 6 -> {
                f[hp - 1]--;
                if (f[hp - 1] == 0)
                {
                    return 1;
                } else
                {
                    return 2;
                }
            }
            case 7, 8, 9, 10 -> {
                if (f[hp - 1] == 1)
                {
                    f[hp - 1] = 0;
                    return 1;
                }
            }
            case 0, 99, 21 -> {
                return 0;
            }
            default -> throw new IllegalArgumentException("isKill Exception" + hp);
        }
        return -1;
    }

    public byte ranDir(int[] LastShot, byte dir, byte[][] myf)
    {
        if (LastShot[0] > -1 && LastShot[0] < 10
                && LastShot[1] > -1 && LastShot[1] < 10)
        {
            Random rnd = new Random();
            while (true)
            {
                int randDir =  rnd.nextInt(4) + 1;
                if (randDir != dir)
                {
                    switch (randDir)
                    {
                        case 1 -> {
                            if (LastShot[0] - 1 > -1 && myf[LastShot[0] - 1][LastShot[1]] != 44 && myf[LastShot[0] - 1][LastShot[1]] != 21)
                            {
                                return 1;
                            }
                        }
                        case 2 -> {
                            if (LastShot[0] + 1 < 10 && myf[LastShot[0] + 1][LastShot[1]] != 44 && myf[LastShot[0] + 1][LastShot[1]] != 21)
                            {
                                return 2;
                            }
                        }

                        case 3 -> {
                            if (LastShot[1] - 1 > -1 && myf[LastShot[0]][LastShot[1] - 1] != 44 && myf[LastShot[0]][LastShot[1] - 1] != 21)
                            {
                                return 3;
                            }
                        }

                        case 4 -> {
                            if (LastShot[1] + 1 < 10 && myf[LastShot[0]][LastShot[1] + 1] != 44 && myf[LastShot[0]][LastShot[1] + 1] != 21)
                            {
                                return 4;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    public byte[] argsSet(byte dir)
    {
        byte[] res = new byte[3];
        switch (dir)
        {
            case 1 -> res[1] = -1;

            case 2 -> {
                res[1] = 1;
                return res;
            }

            case 3 -> {
                res[0] = 1;
                res[2] = -1;
                return res;
            }

            case 4 -> {
                res[0] = 1;
                res[2] = 1;
                return res;
            }
        }
        return res;
    }

    public int[] edgeFind(int decks, boolean dir, int[] LastShot, byte[][] myf, SPClass spClass)
    {
        for (int i = dir ? LastShot[1] : LastShot[0]; true; i--)
        {
            if (i > -1)
            {
                if (myf[dir ? LastShot[0] : i][dir ? i : LastShot[1]] != 66)
                {
                    spClass.shipRound(decks, dir ? LastShot[0] : (i + 1), dir ? (i + 1) : LastShot[1], dir, myf, 21);
                    return new int[]{dir ? LastShot[0] : (i + 1), dir ? i + 1 : LastShot[1]};
                }
            } else if (i == -1 && myf[dir ? LastShot[0] : 0][dir ? 0 : LastShot[1]] == 66)
            {
                spClass.shipRound(decks, dir ? LastShot[0] : 0, dir ? 0 : LastShot[1], true, myf, 21);
                return new int[]{dir ? LastShot[0] : 0, dir ? 0 : LastShot[1]};
            }
        }
    }

    public Const dirSet(int row, int col, byte[][] myf) throws Exception
    {
        Random rnd = new Random();
        if (row - 1 > -1 && myf[row - 1][col] == 66)
        {
            return new Const(true, 1); /*вверх*/
        } else if (row + 1 < 10 && myf[row + 1][col] == 66)
        {
            return new Const(true, 2); /*вниз*/
        }

        if (col - 1 > -1 && myf[row][col - 1] == 66)
        {
            return new Const(true, 3); /*влево*/
        } else if (col + 1 < 10 && myf[row][col + 1] == 66)
        {
            return new Const(true, 4); /*вправо*/
        }
        else throw new Exception("dirSetException");
        /*далее идет определение направления выстрелов*/
    }
}