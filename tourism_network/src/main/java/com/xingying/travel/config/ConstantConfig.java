package com.xingying.travel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Title: OSS常量配置
 * @author: 陈宏松
 * @create: 2019-01-25 17:03
 * @version: 1.0.0
 **/
@Component
@Configuration
public class ConstantConfig {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.bucketname}")
    private String bucketname;

    @Value("${aliyun.oss.keyid}")
    private String keyid;

    @Value("${aliyun.oss.keysecret}")
    private String keysecret;

    @Value("${aliyun.oss.filehost}")
    private String filehost;
    
    
    @Value("${aliyun.oss.urlPrefix}")
    private String urlPrefix;
    
    

    public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucketname() {
        return bucketname;
    }

    public void setBucketname(String bucketname) {
        this.bucketname = bucketname;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getKeysecret() {
        return keysecret;
    }

    public void setKeysecret(String keysecret) {
        this.keysecret = keysecret;
    }

    public String getFilehost() {
        return filehost;
    }

    public void setFilehost(String filehost) {
        this.filehost = filehost;
    }
}
