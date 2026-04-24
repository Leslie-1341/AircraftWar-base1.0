package edu.hitsz.view;

import edu.hitsz.application.Game;

import javax.swing.*;
import java.awt.*;

/**
 * 游戏起始菜单界面
 * 提供难度选择和音效开关功能
 */
public class StartMenu {
    private JPanel mainPanel;
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private JCheckBox musicCheckBox; // 音效开关

    // 记录用户选择的难度和音效状态
    private String selectedDifficulty = "MEDIUM";
    private boolean musicEnabled = true;

    public StartMenu() {
        // 初始化主面板，使用网格布局，使得按钮垂直等大排列
        mainPanel = new JPanel();
        // 5行1列，水平间距0，垂直间距20。加1行是为了看起来不那么拥挤
        mainPanel.setLayout(new GridLayout(5, 1, 0, 20));
        // 设置面板边距 (上, 左, 下, 右)
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // 1. 初始化组件
        JLabel titleLabel = new JLabel("飞机大战(AircraftWar)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));

        easyButton = new JButton("简单模式 (EASY)");
        mediumButton = new JButton("普通模式 (MEDIUM)");
        hardButton = new JButton("困难模式 (HARD)");

        // 默认开启音效
        musicCheckBox = new JCheckBox("开启游戏音效", true);
        musicCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

        // 2. 将组件添加到主面板
        mainPanel.add(titleLabel);
        mainPanel.add(easyButton);
        mainPanel.add(mediumButton);
        mainPanel.add(hardButton);
        mainPanel.add(musicCheckBox);

        // 3. 绑定按钮监听器 (目前仅打印，后续加入界面切换逻辑)
        easyButton.addActionListener(e -> {
            selectedDifficulty = "EASY";
            musicEnabled = musicCheckBox.isSelected();
            startGame();
        });

        mediumButton.addActionListener(e -> {
            selectedDifficulty = "MEDIUM";
            musicEnabled = musicCheckBox.isSelected();
            startGame();
        });

        hardButton.addActionListener(e -> {
            selectedDifficulty = "HARD";
            musicEnabled = musicCheckBox.isSelected();
            startGame();
        });
    }

    /**
     * 处理点击开始游戏后的统一逻辑
     */
    private void startGame() {
        System.out.println("准备开始游戏，难度：" + selectedDifficulty);

        // 1. 实例化游戏界面，传入选择的难度和音效状态
        Game game = new Game(selectedDifficulty, musicEnabled);

        // 2. 将游戏界面添加到 Main 的容器中，并命名为 "GAME"
        edu.hitsz.application.Main.cardPanel.add(game, "GAME");
        edu.hitsz.application.Main.cardLayout.show(edu.hitsz.application.Main.cardPanel, "GAME");

        // 3. 【修改】直接调用 action() 启动游戏内部的定时器主循环，不需要 new Thread
        game.action();
    }

    /**
     * 供 CardLayout 获取当前界面的根面板
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}