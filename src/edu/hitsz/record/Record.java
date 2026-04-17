package edu.hitsz.record;

/**
 * 得分记录实体类 (Value Object)
 * 仅用于封装单条得分记录的数据，不包含任何业务逻辑
 */
public class Record {

    private String userName;
    private int score;
    private String time;

    // 构造函数
    public Record(String userName, int score, String time) {
        this.userName = userName;
        this.score = score;
        this.time = time;
    }

    // ==========================================
    // Getter 和 Setter 方法
    // ==========================================

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}