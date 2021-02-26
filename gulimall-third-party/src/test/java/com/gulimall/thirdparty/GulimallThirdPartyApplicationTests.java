package com.gulimall.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Value("${alibaba.cloud.access-key}")
    private String accessKey;
    @Value("${alibaba.cloud.secret-key}")
    private String secretKey;
    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;

    @Test
    void contextLoads() {
    }
    @Autowired
    OSSClient ossClient;
//
    @Test
    public void testUpload() throws FileNotFoundException{
        InputStream inputStream=new FileInputStream("C:\\Users\\ling\\Pictures\\jpg\\30070-106.jpg");
//        OSSClient ossClient=new OSSClient(endpoint, accessKey, secretKey);
        ossClient.putObject("gulimall-hasdfasdf","30070-106-2.jpg",inputStream);
        ossClient.shutdown();
        System.out.println("上传完成!!");
    }

}
