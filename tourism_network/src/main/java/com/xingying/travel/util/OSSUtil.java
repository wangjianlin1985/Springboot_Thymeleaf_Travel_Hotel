package com.xingying.travel.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.xingying.travel.config.ConstantConfig;
import com.xingying.travel.pojo.Scenic;
import com.xingying.travel.service.ScenicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Title: 阿里OSS图片存储
 * @author: 陈宏松
 * @create: 2019-01-25 16:50
 * @version: 1.0.0
 **/

@Component
public class OSSUtil {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConstantConfig constantConfig;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 图片上传
     * @param file
     * @return
     */
    public String upLoad(File file){

        logger.info("------OSS文件上传开始--------"+file.getName());
        String endpoint=constantConfig.getEndpoint();
        System.out.println("获取到的Point为:"+endpoint);
        String accessKeyId=constantConfig.getKeyid();
        String accessKeySecret=constantConfig.getKeysecret();
        String bucketName=constantConfig.getBucketname();
        String fileHost=constantConfig.getFilehost();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr=format.format(new Date());
        if(file==null){
            return null;
        }
        OSSClient client=new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            if (!client.doesBucketExist(bucketName)) {
                client.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                client.createBucket(createBucketRequest);
            }
            // 设置文件路径和名称
            String fileUrl = fileHost + "/" + (dateStr + "/" + UUID.randomUUID().toString().replace("-", "") + "-" + file.getName());
           // String fileUrl = fileHost + "/" + ( UUID.randomUUID().toString().replace("-", "") + "-" + file.getName());
            System.out.println("------->打印图片的url------->"+fileUrl);

            //向缓存中存一份文件路径
            redisTemplate.opsForValue().set("fileurl" , fileUrl , 1 , TimeUnit.HOURS);


            //向相册缓存中添加文件路径
            redisTemplate.opsForValue().set("gallery" , fileUrl , 1 , TimeUnit.HOURS);


            // 上传文件
            PutObjectResult result = client.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            // 设置权限(公开读)
            client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                logger.info("------OSS文件上传成功------" + fileUrl);
            }
        }catch (OSSException oe){
            logger.error(oe.getMessage());
        }catch (ClientException ce){
            logger.error(ce.getErrorMessage());
        }finally{
            if(client!=null){
                client.shutdown();
            }
        }
        return null;
    }
}
