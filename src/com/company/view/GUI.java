package com.company.view;

import com.company.enemy.SPClass;
import com.company.enemy.StProc;
import com.company.tools.Events.EventsGen.EventsGen;
import com.company.tools.FilesChange;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class GUI extends JFrame
{
    public byte amtEn = 10;
    public byte amtMy = 10;
    private boolean dir = true;
    public JTable my;
    public JTable en;
    public final JLabel highlight = new JLabel();
    public final JLabel enemyShot = new JLabel();
    private boolean isHighlight = true;
    public boolean isShot = false;
    private final int height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;
    private final double m = height / 400.0;
    private final int change = (int) (22 * m);
    private final Scanner a = new Scanner(new FileInputStream("resources/Save.txt"));
    public byte[] hpEn = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    public byte[] hpMy = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    public final StProc stProc = new StProc();
    public final FilesChange f = new FilesChange();
    public final EventsGen gen = new EventsGen();
    public final SPClass spClass = new SPClass();
    public byte[][] myf = f.setFields(a);
    public final byte[][] enf = f.setFields(a);
    public byte[][] enfield = f.setFields(a);
    protected final MenuClass menuClass = new MenuClass(gen, enf);

    public GUI() throws IOException
    {
        super("Sea Battle");
        setLayout(null);
        p.x = 1;
        /*выравнивание символов*/
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2 - 300 * m), (int) (screenSize.getHeight() / 2 - 200 * m));
        Object[] h = new String[]{" ", "а", "б", "в", "г", "д", "е", "ж", "з", "и", "к"};
        gen.addListener(e ->
        {
            menuClass.isSaved = true;
            my.removeKeyListener(k);
            if (!isHighlight)
            {
                getLayeredPane().add(highlight);
                isHighlight = true;
            }
        });
        gen.addExitListener(e ->
                dispose());
        menuClass.setMyf(myf);
        menuClass.setEnf(enfield);
        setSize((int) (height * 1.5), height);
        setResizable(false);
        setJMenuBar(menuClass.menuGame());
        JLayeredPane pane = getLayeredPane();
        JLabel lblYour = new JLabel("Ваше поле");

        lblYour.setForeground(Color.BLACK);
        JLabel lblEnemy = new JLabel("Вражеское поле");
        {
            int y = (int) (35 * m);
            int width = (int) (150 * m);
            lblYour.setBounds((int) (108 * m), y, width, change);
            lblEnemy.setBounds((int) (373 * m), y, width, change);
        }
        lblEnemy.setForeground(Color.BLACK);
        pane.add(lblYour);
        pane.add(lblEnemy);

        highlight.setBounds((int) (36 * m) + change + 1, (int) (60 * m + change + 2), (b[0] * change) - 1, (int) (21 * m));
        pane.add(highlight);
        pane.setLayer(highlight, 1);
        highlight.setOpaque(true);
        canPlace(0, 1);

        enemyShot.setBounds((int) (43 * m), (int) (308 * m), (int) (250 * m), (int) (30 * m));
        enemyShot.setForeground(Color.BLACK);
        enemyShot.setFont(new Font(Font.DIALOG, Font.PLAIN, (int) (15 * m)));
        pane.add(enemyShot);

        my = new JTable(toObject(myf), h)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }

            @Override
            public void repaint()
            {
                super.repaint();
                highlight.repaint();
            }

            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend)
            {
            }
        };
        /*настройка таблиц*/
        my.getTableHeader().setReorderingAllowed(false);
        my.setBackground(Color.WHITE);
        my.setForeground(Color.BLACK);
        my.setDefaultRenderer(String.class, r);
        my.setRowSelectionAllowed(false);
        my.setFont(new Font(Font.DIALOG, Font.BOLD, (int) (19 * m)));
        my.setRowHeight(change);
        my.setTableHeader(new JTableHeader(my.getColumnModel())
        {
            @Override
            public Dimension getPreferredSize()
            {
                Dimension d = super.getPreferredSize();
                d.height = change;
                return d;
            }
        });

        en = new JTable(toObject(enfield), h)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }

            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend)
            {
                if (!isShot)
                {
                    super.changeSelection(rowIndex, columnIndex, toggle, extend);
                }
            }
        };
        en.setForeground(Color.BLACK);
        en.setBackground(Color.WHITE);
        en.setSelectionBackground(null);
        en.setDefaultRenderer(String.class, r);
        en.getTableHeader().setReorderingAllowed(false);
        en.setRowSelectionAllowed(false);
        en.setFont(new Font(Font.DIALOG, Font.BOLD, (int) (19 * m)));
        en.setRowHeight(change);
        en.setTableHeader(new JTableHeader(en.getColumnModel())
        {
            @Override
            public Dimension getPreferredSize()
            {
                Dimension d = super.getPreferredSize();
                d.height = change;
                return d;
            }
        });
        MouseListener mouse = new MouseAdapter()/*обработка пользовательского клика по полю противника*/
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (!isShot)
                {
                    int row = en.getSelectedRow();
                    int col = en.getSelectedColumn();
                    if (amt == 0 && amtMy > 0 && col != 0)
                    {
                        Object value = en.getValueAt(row, col);
                        if (!value.equals("\u2022") && !value.equals("X") && !value.equals("*"))
                        {
                            if (e.getButton() == MouseEvent.BUTTON1)
                            {
                                menuClass.isSaved = false;
                                switch (stProc.isKill(enf[row][col - 1], hpEn))
                                {
                                    case 1 -> {
                                        byte num = enf[row][col - 1];
                                        enfield[row][col - 1] = 66;
                                        en.setValueAt("*", row, col);
                                        try
                                        {
                                            int psDir = stProc.dirSet(row, col - 1, enfield).dir;
                                            int[] coordinates = stProc.edgeFind(decksCheck(num), psDir == 3 || psDir == 4, new int[]{row, col - 1}, enfield, spClass);
                                            setDead(en, decksCheck(num), coordinates[0], coordinates[1] + 1, psDir == 3 || psDir == 4, enfield);
                                        } catch (Exception ioException)
                                        {
                                            ioException.printStackTrace();
                                        }
                                        amtEn--;
                                        if (amtEn == 0)
                                        {
                                            winSet("Вы выиграли!!!");
                                        }
                                    }
                                    case 2 -> {
                                        enfield[row][col - 1] = 66;
                                        en.setValueAt("*", row, col);
                                    }
                                    case 0 -> {
                                        en.setValueAt("\u2022", row, col);
                                        menuClass.isSaved = false;
                                        menuClass.setEnf(enfield);
                                        enfield[row][col - 1] = 44;
                                        try
                                        {
                                            gen.fireShot();
                                        } catch (Exception exception)
                                        {
                                            exception.printStackTrace();
                                        }
                                    }
                                }
                                menuClass.isSaved = false;
                                menuClass.setEnf(enfield);
                            }
                        }
                    }
                }
            }
        };
        en.addMouseListener(mouse);
        /*делаем ячейки квардатными*/
        for (int i = 0; i < 11; i++)
        {

            TableColumn t = en.getColumnModel().getColumn(i);
            t.setMinWidth(change);
            t.setMaxWidth(change);
            t.setCellRenderer(r);
            t.setCellRenderer(i != 0 ? r : en.getTableHeader().getDefaultRenderer());
            t = my.getColumnModel().getColumn(i);
            t.setMinWidth(change);
            t.setMaxWidth(change);
            t.setCellRenderer(i != 0 ? r : my.getTableHeader().getDefaultRenderer());
        }
        pane.setLayer(my, 2);
        pane.setLayer(en, 2);
        {
            int y = (int) (60 * m);
            int size = 11 * change + 3;
            pane.add(new JScrollPane(my)).setBounds((int) (36 * m), y, size, size);
            pane.add(new JScrollPane(en)).setBounds((int) (314 * m), y, size, size);
        }
        addWindowListener(new WindowAdapter()/*обработка закрытия окна*/
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                try
                {
                    if (!menuClass.isSaved)
                    {
                        if (JOptionPane.showConfirmDialog(GUI.this, "Сохранить игру перед выходом?",
                                "Подтверждение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        {
                            a.close();
                            f.setProp("isNew", false);
                            f.printInFileArr(myf, enf, enfield);
                        } else
                        {
                            if (Boolean.parseBoolean(f.getProp("isNew")))
                            {
                                defaults();
                            }
                            a.close();
                        }
                    } else
                    {
                        f.printInFileArr(myf, enf, enfield);
                    }
                    menuClass.exitAction(1);
                    gen.fireExit();
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
        if (!Boolean.parseBoolean(f.getProp("loadLast")) && !Boolean.parseBoolean(f.getProp("isNew")))
        {
            showLoadDialog();
        } else if (Boolean.parseBoolean(f.getProp("isNew")))
        {
            defaults();
        }
    }

    private Object[][] toObject(byte[][] obj)/*преобразование массива цифр в массив символов типа Object*/
    {
        Object[][] res = new Object[10][11];
        for (int i = 0; i < 10; i++)
        {
            res[i][0] = i + 1;
            for (int j = 1; j < 11; j++)
            {
                switch (obj[i][j - 1])
                {
                    case 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 -> res[i][j] = "\u20DE";
                    case 44 -> res[i][j] = "\u2022";
                    case 55 -> res[i][j] = "Х";
                    case 66 -> res[i][j] = "*";
                    default -> res[i][j] = " ";
                }
            }
        }
        return res;
    }

    private final byte[] b = {Byte.parseByte(f.getProp("desks")), Byte.parseByte(f.getProp("shipAmt")), Byte.parseByte(f.getProp("shipProm"))};
    public byte amt = Byte.parseByte(f.getProp("amt"));
    private byte counter = 1;
    public boolean canPlace;
    KeyListener k;
    MouseListener higL;
    Point p = new Point();

    public void place() throws IOException/*размещение кораблей*/
    {
        amt = Byte.parseByte(f.getProp("amt"));
        menuClass.amt = amt;
        higL = new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)/*обработка пользовательского клика по своему полю при размещении кораблей*/
            {
                int row = my.rowAtPoint(e.getPoint());
                int col = my.columnAtPoint(e.getPoint());
                if (dir && col > 11 - b[0])
                {
                    col = 11 - b[0];
                }
                if (!dir && row > 10 - b[0])
                {
                    row = 10 - b[0];
                }
                if (col > 0)
                {
                    canPlace(row, col);
                    p.y = row;
                    p.x = col;
                    highlight.setLocation((int) (36 * m + p.x * change) + 1, (int) (60 * m + p.y * change) + change + 1);
                    super.mouseClicked(e);
                }
            }
        };
        k = new KeyAdapter()/*обработка пользовательского ввода с клавиатуры*/
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (amt > 0 && p.x != 0)
                {
                    switch (e.getKeyCode())
                    {
                        case KeyEvent.VK_UP -> {
                            if (p.y > 0)
                            {
                                highlight.setLocation(highlight.getX(), highlight.getY() - change);
                                super.keyPressed(e);
                                p.y--;
                            }
                        }
                        case KeyEvent.VK_LEFT -> {
                            if (p.x > 1)
                            {
                                highlight.setLocation((highlight.getX() - change), highlight.getY());
                                super.keyPressed(e);
                                p.x--;
                            }
                        }
                        case KeyEvent.VK_DOWN -> {
                            if (p.y < 9)
                            {
                                highlight.setLocation(highlight.getX(), highlight.getY() + change);
                                super.keyPressed(e);
                                p.y++;
                            }
                        }
                        case KeyEvent.VK_RIGHT -> {
                            if (p.x < (dir ? 11 - b[0] : 10))
                            {
                                highlight.setLocation(highlight.getX() + change, highlight.getY());
                                super.keyPressed(e);
                                p.x++;
                            }
                        }
                        case KeyEvent.VK_SPACE -> {
                            if (dir)
                            {
                                if (p.y <= 10 - b[0])
                                {
                                    dir = false;
                                    highlight.setSize(change - 1, change * b[0] - 1);
                                }
                            } else
                            {
                                if (p.x <= 11 - b[0])
                                {
                                    dir = true;
                                    highlight.setSize(b[0] * change - 1, change - 1);
                                }
                            }
                        }
                        default -> {
                            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                            {
                                try
                                {
                                    if (canPlace)
                                    {
                                        menuClass.isSaved = false;
                                        print(b[0], dir, counter, '\u20DE');
                                        spClass.shipRound(b[0], p.y, p.x - 1, dir, myf, (byte) 99);
                                        f.setProp(new String[]{"counter", "amt", "shipProm"}, new Object[]{++counter, --amt, --b[2]});
                                        menuClass.amt--;
                                        if (b[2] == 0)
                                        {
                                            f.setProp(new String[]{"shipAmt", "shipProm", "desks"}, new Object[]{++b[1], b[1], --b[0]});
                                            b[2] = b[1];
                                            highlight.setSize(dir ? (b[0] * change - 1) : (int) (21 * m) + 1, (int) (dir ? 21 * m + 1 : (b[0] * change - 1)));
                                        }
                                        moveHighLight();
                                        super.keyPressed(e);
                                    } else
                                    {
                                        moveHighLight();
                                    }
                                } catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                    canPlace(p.y, p.x);
                } else if (amt == 0)
                {
                    getLayeredPane().remove(highlight);
                    isHighlight = false;
                    my.removeKeyListener(this);
                    my.removeMouseListener(higL);
                }
            }
        };
        my.addMouseListener(higL);
        my.addKeyListener(k);
    }

    /*dir true ->
    dir false \/ */
    public void print(byte decks, boolean dir, byte counter, char c)/*печать корабля в таблицу*/
    {
        int row = p.y;
        int col = p.x;
        for (int i = 0; i < decks; i++)
        {
            row += dir || i == 0 ? 0 : 1;
            col += dir && i > 0 ? 1 : 0;
            if (myf[row][col - 1] == 0)
            {
                myf[row][col - 1] = counter;
                my.setValueAt(c, row, col);
            }
        }
    }

    volatile boolean isTimerAlive;

    public final void setEnemyShotText(int row, int col, JTable table, String value)/*занесение выстрела противника на поле игрока*/
    {
        enemyShot.setText("Я стреляю на... " + byteToChar(col + 1) + " " + (row + 1));
        table.setValueAt(value, row, col + 1);
        isTimerAlive = false;
    }

    public char byteToChar(int i)/*цифру в букву*/
    {
        return switch (i)
                {
                    case 1 -> 'a';
                    case 2 -> 'б';
                    case 3 -> 'в';
                    case 4 -> 'г';
                    case 5 -> 'д';
                    case 6 -> 'е';
                    case 7 -> 'ж';
                    case 8 -> 'з';
                    case 9 -> 'и';
                    case 10 -> 'к';
                    default -> throw new IllegalArgumentException();
                };
    }

    public void setDead(JTable table, int decks, int row, int col, boolean direct, byte[][] field) throws IOException/*пометка смерти корабля*/
    {
        for (int n = !direct ? row : col, i = 0; i < decks; n++, i++)
        {
            if (n < 11)
            {
                if (table.getValueAt(!direct ? n : row, !direct ? col : n).equals("*"))
                {
                    table.setValueAt("X", !direct ? n : row, !direct ? col : n);
                    if (field[!direct ? n : row][(!direct ? col : n) - 1] == 66)
                    {
                        field[!direct ? n : row][(!direct ? col : n) - 1] = 55;
                    } else
                        throw new IOException("array fill error " + field[!direct ? n : row][(!direct ? col : n) - 1]);
                } else
                    throw new IOException("error replace " + " " + table.getValueAt(!direct ? n : row, !direct ? col : n));
            } else
                throw new IOException("error replace " + " " + table.getValueAt(!direct ? n : row, !direct ? col : n));
        }
    }

    private void showLoadDialog()/*приветственное окно загрузки*/
    {
        JDialog d = new JDialog(this, "Загрузка последней игры", true);
        d.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - 200),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - 55));
        d.setResizable(false);
        d.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        d.setSize(400, 110);
        d.setLayout(null);

        JLabel l = new JLabel("Продолжить сохраненную игру?");
        l.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        l.setBounds(70, 5, 250, 30);


        JCheckBox box = new JCheckBox("Всегда продолжать последнюю игру");
        box.setFont(new Font(Font.DIALOG, Font.PLAIN, 10));
        box.addChangeListener(e ->
                f.setProp("loadLast", true));
        box.setBounds(20, 40, 230, 25);

        JButton b = new JButton("Да");
        b.setFont(new Font(Font.DIALOG, Font.BOLD, 10));
        b.setBounds(250, 40, 55, 20);
        b.addActionListener(e ->
                d.dispose());

        JButton b2 = new JButton("Нет");
        b2.setFont(new Font(Font.DIALOG, Font.BOLD, 10));
        b2.setBounds(315, 40, 55, 20);
        b2.addActionListener(e ->
        {
            try
            {
                defaults();
                f.newGame();
                d.dispose();
                gen.fireEvent();
            } catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        });
        d.add(l);
        d.add(box);
        d.add(b);
        d.add(b2);
        d.setVisible(true);
    }

    public void defaults()
    {
        menuClass.isSaved = true;
        b[0] = 4;
        b[1] = 1;
        b[2] = 1;
        p.x = 1;
        p.y = 0;
        amtMy = 10;
        amt = 10;
        amtEn = 10;
        counter = 1;
        highlight.setSize(b[0] * change - 1, (int) (21 * m) + 1);
        highlight.setLocation((int) (36 * m) + change + 1, (int) (60 * m + change + 1));
        f.setProp(new String[]{"isNew", "amt", "desks", "shipAmt", "shipProm", "counter"}, new Object[]{true, 10, 4, 1, 1, 1});

        for (int j = 0; j < 10; j++)
        {
            for (int k = 1; k < 11; k++)
            {
                my.setValueAt(" ", j, k);
                en.setValueAt(" ", j, k);
                myf[j][k - 1] = 0;
                enf[j][k - 1] = 0;
                enfield[j][k - 1] = 0;
            }
        }
        canPlace(0, 1);
    }

    private void canPlace(int row, int col)
    {
        canPlace = spClass.shipCanPlace(b[0], row, col - 1, dir, myf);
        highlight.setBackground(canPlace ? Color.GREEN : Color.RED);
    }

    private void moveHighLight()
    {
        /*напоминание самому себе от том ,как создать несуществующие трудности и героически с ними бороться
        /*int x=highlight.getX() + (p.y < 10 ? 0 : (p.x < 10 - b[0] ? 22 : -(10 - b[0]) * 22)); ну как? нравится? разберитесь в этом на досуге
        int y=highlight.getY() + (p.y < 10 ? 22 : dir ? -200 : (b[0] - 1) * 22);
        highlight.setLocation(x, y);
        вместо \/*/
        if ((p.y < 9 && dir) || (p.y < 10 - b[0] && !dir))
        {
            highlight.setLocation(highlight.getX(), highlight.getY() + change);
            p.y++;
        } else if ((p.x < 10 && !dir) || (p.x < 10 - b[0] && dir))
        {
            highlight.setLocation(highlight.getX() + change, highlight.getY() - p.y * change);
            p.x++;
            p.y = 0;
        } else
        {
            highlight.setLocation((int) (36 * m) + change + 1, (int) (60 * m) + change + 1);
            p.x = 1;
            p.y = 0;
        }
    }

    private int decksCheck(int num)/*определяем длину убитого корабля*/
    {
        return switch (num)
                {
                    case 1 -> 4;
                    case 2, 3 -> 3;
                    case 4, 5, 6 -> 2;
                    case 7, 8, 9, 10 -> 1;
                    default -> throw new IllegalArgumentException("decks check error " + num);
                };
    }

    public void winSet(String text)
    {
        enemyShot.setText(text);
        enemyShot.setSize((int) (100 * m), enemyShot.getHeight());
        enemyShot.setLocation(getWidth() / 2 - enemyShot.getWidth() / 2, enemyShot.getY());
    }
}