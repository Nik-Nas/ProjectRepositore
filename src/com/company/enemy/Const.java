package com.company.enemy;

public class Const
{
    public boolean trueDir;
    public byte dir;

    public Const(boolean isTrueDir, Object dir)
    {
        this.trueDir = isTrueDir;
        try
        {
            this.dir = Byte.parseByte(String.valueOf(dir));
        } catch (ClassCastException e)
        {
            e.printStackTrace();
            System.exit(12);
        }
    }
}
