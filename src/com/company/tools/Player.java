package com.company.tools;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class Player
{
    private Timer timer;
    private final String pathToMp3;
    public boolean canPlay = true;

    public Player()
    {
        String songName = "pr.mp3";
        pathToMp3 = System.getProperty("user.dir") + "/" + songName;
    }

    public void play(BasicPlayer player)
    {
        try
        {
            timer = new Timer();
            player.open(new URL("file:///" + pathToMp3));
            TimerTask t = new TimerTask()
            {
                @Override
                public void run()
                {
                    if (canPlay)
                    {
                        try
                        {
                            player.play();
                        } catch (BasicPlayerException basicPlayerException)
                        {
                            basicPlayerException.printStackTrace();
                        }
                    }
                }
            };
            timer.scheduleAtFixedRate(t, 0, 884000);
        } catch (BasicPlayerException | MalformedURLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void pause(BasicPlayer player) throws BasicPlayerException
    {
        player.stop();
        canPlay = false;
        timer.cancel();
    }
}