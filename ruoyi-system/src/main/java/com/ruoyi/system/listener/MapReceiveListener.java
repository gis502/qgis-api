package com.ruoyi.system.listener;

import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ServeException;
import com.ruoyi.common.utils.QiniuOssUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.entity.pub.DZProduct;
import com.ruoyi.system.domain.params.QgisArgs;
import com.ruoyi.system.service.pub.IDZProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.rabbitmq.client.Channel;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-04-12 16:47
 * @description: 获取专题图监听器
 */

@Slf4j
@Component
public class MapReceiveListener {

    @Resource
    private QiniuOssUtil qiniuOss;
    @Autowired
    private IDZProductService idzProductService;

    // rabbitmq 监听专题图队列
    @RabbitListener(queues = "maps", ackMode = "MANUAL")
    public void receive(QgisArgs args, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Message message) throws IOException {
        log.info("接收通知：{} 已生成！", args.getName());
        try {
            // 获取路径
            File originFile = new File(args.getOutFile());
            if (!originFile.exists()) {
                throw new ServeException(BaseConstants.FILE_NOT_FOUND_ERROR);
            }
            // 设备离线不上传云
            if (!qiniuOss.getOffline()) {
                // 获取对应图片文件二进制流
                MultipartFile file = new MockMultipartFile(originFile.getName(), originFile.getName(),
                        "image/jpeg", new FileInputStream(originFile));
                // 将图片设置唯一Id
                String imageUrl = args.getQueueId() + "_" + args.getName();
                // 将图片上传到七牛云服务器
                String qiniuUrl = qiniuOss.upload(imageUrl, file);
                // 上传失败
                if (StringUtils.isEmpty(qiniuUrl)) {
                    throw new ServeException(BaseConstants.THEMATIC_FAILED);
                }
                log.info("{} 成功上传到七牛云服务器!", args.getName());
                handleData(args, qiniuUrl);
                log.info("{} 路径已保存到数据库!", args.getName());
            } else {
                handleData(args, null);
                log.info("设备离线,图件已存储到本地!");
            }
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            log.info("ack：消息确认成功！");

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("处理消息失败: {}", ex);

            /**
             * 参数说明：
             * 1. deliveryTag：消息唯一标识
             * 2. multiple：是否拒绝多条
             * 3. requeue：是否重新入队（false=直接丢弃，true=重新入队重试）
             * 建议：非幂等业务设置为 false，避免死循环；幂等业务可设置为 true
             */
            channel.basicNack(deliveryTag, false, false);

            // 抛出异常
            throw new ServeException(BaseConstants.THEMATIC_FAILED);
        }
    }

    private void handleData(QgisArgs args, String url) {
        DZProduct product = new DZProduct();
        product.setEqQueueId(args.getQueueId());
        product.setProTime(LocalDateTime.now());
        product.setCode(args.getMapLayout());
        product.setFileType("图片");
        product.setFileName(args.getName());
        product.setFilePath(args.getPath());
        product.setFileExtension(".jpg");
        // 地震/暴雨 专题图
        product.setProType(args.getDisaster());

        if (url == null) {
            // 修改存储路径
            product.setSourceFile(args.getOutFile());
        } else {
            product.setSourceFile(url);
        }
        // 将图件信息插入到结果表中
        idzProductService.save(product);
        log.info("{} 已保存到数据库!", args.getName());

    }

}
