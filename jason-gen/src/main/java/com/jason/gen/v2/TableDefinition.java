package com.jason.gen.v2;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/13
 **/
@Data
public class TableDefinition implements Serializable {
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表注释
     */
    private String tableComment;
    /**
     * 当前表内的列定义信息数组
     */
    private List<ColumnDefinition> columnDefinitionsList;


}
