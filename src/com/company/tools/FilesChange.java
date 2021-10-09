package com.company.tools;

import com.company.tools.Events.EventsGen.EventsGen;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;

public class FilesChange//work with files
{
    /**
     * Return array, filled "Save" file's info
     **/
    public byte[][] setFields(Scanner a)
    {
        byte[][] ints = new byte[10][10];
        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                if (a.hasNextByte())
                {
                    ints[i][j] = Byte.parseByte(a.next());
                } else
                {
                    throw new NoSuchElementException("Ощибка");
                }
            }
        }
        return ints;
    }

    /**
     * Write array's values to "Save" file
     **/
    public void printInFileArr(byte[][] my, byte[][] en, byte[][] enfield) throws FileNotFoundException
    {
        PrintStream p = new PrintStream("Save.txt");
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                for (int n = 0; n < 10; n++)
                {
                    if (i == 0)
                    {
                        p.print(my[j][n] + " ");
                    } else if (i == 1)
                    {
                        p.print(en[j][n] + " ");
                    }
                    else
                    {
                        p.print(enfield[j][n] + " ");
                    }
                }
                p.println();
            }
            p.println();
        }
        p.close();
    }

    public void newGame() throws IOException
    {
        printInFileArr(new byte[10][10], new byte[10][10], new byte[10][10]);
        new EventsGen().fireEvent();
    }

    FileInputStream fis;
    Properties property = new Properties();

    public String getProp(String key) throws IOException
    {
        try
        {
            fis = new FileInputStream("settings.properties");
            property.load(fis);
            fis.close();
            return property.getProperty(key);
        } catch (IOException ex)
        {
            ex.printStackTrace();
            fis.close();
            return null;
        }
    }

    public void setProp(String key, Object value)
    {
        try
        {
            fis = new FileInputStream("settings.properties");
            property.load(fis);
            property.setProperty(key, String.valueOf(value));
            property.store(new FileOutputStream("settings.properties"), null);
            fis.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setProp(String[] keys, Object[] values)
    {
        try
        {
            fis = new FileInputStream("settings.properties");
            property.load(fis);
            for (int i = 0; i < keys.length; i++)
            {
                property.setProperty(keys[i], String.valueOf(values[i]));
            }
            property.store(new FileOutputStream("settings.properties"), null);
            fis.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}


