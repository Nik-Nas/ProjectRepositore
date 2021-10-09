package com.company.tools.Events.ExitEvent;

import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.FileNotFoundException;

public interface ExitEventListener
{
    void ExitEventPerformed(ExitEvent e) throws FileNotFoundException, BasicPlayerException;
}
