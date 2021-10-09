package com.company.tools.Events.EventsGen;

import com.company.tools.Events.ExitEvent.ExitEvent;
import com.company.tools.Events.ExitEvent.ExitEventListener;
import com.company.tools.Events.MyEvent.MyEvent;
import com.company.tools.Events.MyEvent.MyEventListener;
import com.company.tools.Events.ShotEvent.ShotEvent;
import com.company.tools.Events.ShotEvent.ShotEventListener;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class EventsGen
{
    public final ArrayList<MyEventListener> listeners = new ArrayList<>();
    public final ArrayList<ExitEventListener> exList = new ArrayList<>();
    public final ArrayList<ShotEventListener> shotEvents = new ArrayList<>();

    public void addListener(MyEventListener listener)
    {
        System.out.println("addNewg " + listener);
        listeners.add(listener);
    }

    public void addExitListener(ExitEventListener listener)
    {
        System.out.println("addExit " + listener);
        exList.add(listener);
    }

    public void addShotListener(ShotEventListener listener)
    {
        System.out.println("addShot " + listener);
        shotEvents.add(listener);
    }

    public void fireExit() throws FileNotFoundException, BasicPlayerException
    {
        ExitEvent event = new ExitEvent(this);
        for (ExitEventListener list : exList)
        {
            list.ExitEventPerformed(event);
        }
    }

    public void fireEvent() throws IOException
    {
        MyEvent event = new MyEvent(this);
        for (MyEventListener listener : listeners)
        {
            listener.MyEventPerformed(event);
        }
    }

    public void fireShot() throws Exception
    {
        ShotEvent event = new ShotEvent(this);
        for (ShotEventListener e : shotEvents)
        {
            e.ShotEventPerformed(event);
        }
    }
}
