package edu.hitsz.application;

import edu.hitsz.view.StartMenu;
import javax.swing.*;
import java.awt.*;

/**
 * 程序入口 - 整合 CardLayout 界面管理
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    // 【新增】全局静态布局管理器和容器，方便在 StartMenu 或 Game 中切换界面
    public static final CardLayout cardLayout = new CardLayout();
    public static final JPanel cardPanel = new JPanel(cardLayout);

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        // 1. 初始化主窗口 Frame
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置窗口居中
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2,
                ((int) screenSize.getHeight() - WINDOW_HEIGHT) / 2,
                WINDOW_WIDTH, WINDOW_HEIGHT);

        // 2. 将卡片容器添加到 Frame 中
        frame.add(cardPanel);

        // 3. 实例化起始菜单并添加到容器，命名为 "START_MENU"
        StartMenu startMenu = new StartMenu();
        cardPanel.add(startMenu.getMainPanel(), "START_MENU");

        // 4. 展示起始菜单
        cardLayout.show(cardPanel, "START_MENU");

        frame.setVisible(true);
    }
}