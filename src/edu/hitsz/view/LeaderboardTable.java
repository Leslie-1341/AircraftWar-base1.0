package edu.hitsz.view;

import edu.hitsz.application.Main;
import edu.hitsz.record.Record;
import edu.hitsz.record.RecordDao;
import edu.hitsz.record.RecordDaoImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 排行榜界面
 */
public class LeaderboardTable {
    private JPanel mainPanel;
    private JTable scoreTable;
    private DefaultTableModel model;
    private RecordDao recordDao;
    private String difficulty;

    public LeaderboardTable(String difficulty) {
        this.difficulty = difficulty;
        this.recordDao = new RecordDaoImpl(difficulty);

        // 1. 初始化面板布局
        mainPanel = new JPanel(new BorderLayout());

        // 2. 顶部标题
        JLabel titleLabel = new JLabel("排行榜 - 难度：" + difficulty, SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 3. 中间表格部分
        String[] columnNames = {"名次", "玩家名", "得分", "记录时间"};

        // 重写 isCellEditable 使表格不可编辑
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scoreTable = new JTable(model);
        // 允许选中多行
        scoreTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 加载并显示数据
        loadData();

        // 4. 底部按钮区
        JPanel bottomPanel = new JPanel();
        JButton deleteButton = new JButton("删除选中记录");
        JButton returnButton = new JButton("返回主菜单");
        bottomPanel.add(deleteButton);
        bottomPanel.add(returnButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 5. 绑定监听器
        // 多行删除逻辑
        deleteButton.addActionListener(e -> {
            int[] selectedRows = scoreTable.getSelectedRows();
            if (selectedRows.length > 0) {
                int result = JOptionPane.showConfirmDialog(mainPanel,
                        "确认删除选中的 " + selectedRows.length + " 条记录吗？", "提示", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    List<Record> allRecords = recordDao.getAllRecords();
                    sortRecords(allRecords);

                    // 必须先对选中的索引进行排序，然后逆序删除，防止索引错位
                    Arrays.sort(selectedRows);
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        int rowIdx = selectedRows[i];

                        // 从数据源(DAO)删除
                        Record toDelete = allRecords.get(rowIdx);
                        recordDao.doDelete(toDelete);

                        // 从表格模型(View)删除
                        model.removeRow(rowIdx);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(mainPanel, "请先选中要删除的行！");
            }
        });

        // 返回逻辑
        returnButton.addActionListener(e -> {
            Main.cardLayout.show(Main.cardPanel, "START_MENU");
        });
    }

    private void loadData() {
        List<Record> records = recordDao.getAllRecords();
        sortRecords(records);
        for (int i = 0; i < records.size(); i++) {
            Record r = records.get(i);
            model.addRow(new Object[]{i + 1, r.getUserName(), r.getScore(), r.getTime()});
        }
    }

    private void sortRecords(List<Record> records) {
        Collections.sort(records, (r1, r2) -> Integer.compare(r2.getScore(), r1.getScore()));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}