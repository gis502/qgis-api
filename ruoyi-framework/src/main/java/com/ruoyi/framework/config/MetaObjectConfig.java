package com.ruoyi.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器（处理createTime/updateTime等字段）
 */
@Component // 必须交给Spring容器管理，否则不生效
public class MetaObjectConfig implements MetaObjectHandler {

    // 插入操作时的自动填充逻辑
    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入时填充当前日期
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        // 插入时填充用户信息
        this.strictInsertFill(metaObject, "createBy", String.class, "admin");
        this.strictInsertFill(metaObject, "updateBy", String.class, "admin");
        this.strictInsertFill(metaObject, "createDept", Integer.class, 0);

        // 填充主键
        this.strictInsertFill(metaObject, "id", Long.class, com.baomidou.mybatisplus.core.toolkit.IdWorker.getId());
        // 自动填充逻辑删除
        this.strictInsertFill(metaObject, "delFlag", Integer.class, 0);
        this.strictInsertFill(metaObject, "version", Integer.class, 1);

    }

    // 更新操作时的自动填充逻辑
    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充updateTime：更新时填充当前日期
        this.strictUpdateFill(
                metaObject,
                "updateTime",
                LocalDate.class,
                LocalDate.now()
        );
    }
}