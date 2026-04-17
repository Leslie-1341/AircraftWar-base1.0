package edu.hitsz.record;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据访问对象实现类
 * 负责排行榜数据的持久化，将数据按难度分类存入对应的文本文件中
 */
public class RecordDaoImpl implements RecordDao {

    // 内存中的记录缓存
    private List<Record> records;
    // 当前操作的数据文件路径
    private String fileName;

    /**
     * 构造函数：根据游戏难度动态绑定对应的存储文件
     * @param difficulty 游戏难度 (如 "EASY", "MEDIUM", "HARD")
     */
    public RecordDaoImpl(String difficulty) {
        this.records = new ArrayList<>();
        // 动态生成文件名，例如：record_EASY.txt
        this.fileName = "record_" + difficulty + ".txt";

        // 对象创建时，立刻从文件中加载历史数据到内存
        loadRecordsFromFile();
    }

    @Override
    public List<Record> getAllRecords() {
        return records;
    }

    @Override
    public void doAdd(Record record) {
        records.add(record);
        // 内存修改后，同步保存到文件
        saveRecordsToFile();
    }

    @Override
    public void doDelete(Record record) {
        records.remove(record);
        saveRecordsToFile();
    }

    // ==========================================
    // 文件 I/O 辅助方法
    // ==========================================

    /**
     * 从文件中读取数据
     */
    private void loadRecordsFromFile() {
        File file = new File(fileName);
        // 如果文件不存在，直接返回，内存里是一个空的 List
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // 逐行读取文件
            while ((line = reader.readLine()) != null) {
                // 写入时用逗号隔开：userName,score,time
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Record record = new Record(parts[0], Integer.parseInt(parts[1]), parts[2]);
                    records.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("读取排行榜文件失败：" + e.getMessage());
        }
    }

    /**
     * 将当前内存中的数据写入文件
     */
    private void saveRecordsToFile() {
        // 使用 FileWriter 写文件，默认覆盖旧文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Record record : records) {
                // 拼接成逗号分隔的字符串写入
                String line = record.getUserName() + "," + record.getScore() + "," + record.getTime();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存排行榜文件失败：" + e.getMessage());
        }
    }
}