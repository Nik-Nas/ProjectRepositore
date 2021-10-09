package com.company.view;

import com.company.tools.Player;
import com.company.tools.Events.EventsGen.EventsGen;
import com.company.tools.FilesChange;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MenuClass extends JFrame
{
    private final EventsGen gen;
    private JCheckBoxMenuItem loadLast;

    private byte[][] myf;
    private byte[][] enf;
    private final byte[][] en;
    public byte amt = 10;
    private final FilesChange f = new FilesChange();
    boolean isSaved = true;
    private final BasicPlayer player = new BasicPlayer();
    private final Player s = new Player();
    boolean isMusic;

    public MenuClass(EventsGen gen, byte[][] en) throws IOException
    {
        this.en = en;
        this.gen = gen;
        isMusic = Boolean.parseBoolean(f.getProp("isMusic"));
    }

    public void setEnf(byte[][] enf)
    {
        this.enf = enf;
    }

    public void setMyf(byte[][] myf)
    {
        this.myf = myf;
    }

    public JMenuBar menuGame() throws IOException
    {
        JMenuBar bar = new JMenuBar();
        if (isMusic)
        {
            s.play(player);
        }
        JMenu item = new JMenu("Настройки");
        JCheckBoxMenuItem music = new C();
        music.setText("Музыка");
        music.setState(isMusic);
        JMenuItem newGame = new JMenuItem("Новая игра");
        newGame.addActionListener(e ->
        {
            if (JOptionPane.showConfirmDialog(this, "Вы уверены? Весь прогресс будет утерян", "Подтверждение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                try
                {
                    f.newGame();
                    gen.fireEvent();
                } catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }
            }
        });

        loadLast = new C();
        loadLast.setText("Всегда загружать последнюю игру");
        loadLast.setState(Boolean.parseBoolean(f.getProp("loadLast")));
        JMenuItem save = new JMenuItem("Сохранить");
        save.addActionListener(e ->
        {
            try
            {
                f.setProp("isNew", false);
                f.printInFileArr(myf, en, enf);
                isSaved = true;
            } catch (FileNotFoundException fileNotFoundException)
            {
                fileNotFoundException.printStackTrace();
            }
        });

        JMenuItem exit = new JMenuItem("Выход");
        exit.addActionListener(e ->
        {
            try
            {
                exitAction(0);
            } catch (BasicPlayerException | IOException exception)
            {
                exception.printStackTrace();
            }
        });
        item.add(music);
        item.add(loadLast);
        item.add(newGame);
        item.add(save);
        item.add(exit);
        bar.add(item);
        return bar;
    }

    public void exitAction(int status) throws IOException, BasicPlayerException
    {
        if (status == 0)
        {
            if (JOptionPane.showConfirmDialog(this, "Сохранить игру перед выходом?",
                    "Подтверждение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                f.printInFileArr(myf, en, enf);
            }
            if (Boolean.parseBoolean(f.getProp("isMusic")))
            {
                player.stop();
            }
            gen.fireExit();
        } else if (status == 1)
        {
            if (Boolean.parseBoolean(f.getProp("isMusic")))
            {
                s.pause(player);
                player.stop();
            }
        }
        dispose();
    }

    class C extends JCheckBoxMenuItem
    {
        @Override
        protected void processMouseEvent(MouseEvent e)
        {
            try
            {
                if (e.getID() == (MouseEvent.MOUSE_CLICKED) && e.getSource() != loadLast)
                {
                    if (e.getButton() == MouseEvent.BUTTON1)
                    {
                        if (isMusic)
                        {
                            s.pause(player);
                            f.setProp("isMusic", false);
                            isMusic = false;
                        } else
                        {
                            s.canPlay = true;
                            s.play(player);
                            isMusic = true;
                            f.setProp("isMusic", true);
                        }
                        doClick(0);
                    }
                } else if (e.getID() == (MouseEvent.MOUSE_CLICKED) && e.getSource() == loadLast)
                {
                    f.setProp("loadLast", !Boolean.parseBoolean(f.getProp("loadLast")));
                    doClick(0);
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}