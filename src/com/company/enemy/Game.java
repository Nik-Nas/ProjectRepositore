package com.company.enemy;

import com.company.view.GUI;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Game
{
    private final ShotClass t = new ShotClass();
    private void newG(GUI gui, SPClass spClass) throws IOException
    {
        c.dir = 0;
        c.trueDir = false;
        gui.place();
        spClass.fieldFill(gui);
    }
    private Const c = new Const(false, 0);
    public void playerGameStart() throws Exception
    {
        GUI gui = new GUI();
        SPClass spClass = new SPClass();
        gui.gen.addShotListener(e ->
                new Thread(() ->
                {
                    try
                    {
                        gui.isShot = true;
                        c = t.shot(gui, c);
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    gui.isShot = false;
                }).start());
        gui.gen.addListener(e ->
        {
            //System.out.println("lg");
            gui.defaults();
            try
            {
                newG(gui, spClass);
            } catch (FileNotFoundException fileNotFoundException)
            {
                fileNotFoundException.printStackTrace();
            }
        });
        if (Boolean.parseBoolean(gui.f.getProp("isNew")) || gui.amt > 0)
        {
            newG(gui, spClass);
        }
    }
}