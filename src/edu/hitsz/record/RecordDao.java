package edu.hitsz.record;

import java.util.List;

/**
 * 数据访问对象接口 (DAO Interface)
 * 定义排行榜数据操作的标准接口，隔离具体的存储实现细节
 */
public interface RecordDao {

    /**
     * 获取所有得分记录
     * @return 包含所有得分记录的列表
     */
    List<Record> getAllRecords();

    /**
     * 增加一条得分记录
     * @param record 要添加的记录对象
     */
    void doAdd(Record record);

    /**
     * 删除一条指定的得分记录
     * @param record 要删除的记录对象
     */
    void doDelete(Record record);

}