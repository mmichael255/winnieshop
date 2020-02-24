package com.winnie.upload.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.winnie.common.constants.WNConstant;
import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.upload.config.OSSConfig;
import com.winnie.upload.config.OSSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UploadService {
    public static final List ALLOW_IMG_TYPE = Arrays.asList("image/png");
    @Autowired
    OSS client;

    @Autowired
    OSSProperties prop;

    public String uploadImg(MultipartFile file) {

        String contentType = file.getContentType();
        if (!ALLOW_IMG_TYPE.contains(contentType)){
            throw new WNException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        BufferedImage buff = null;
        try {
            buff = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            throw new WNException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        if (buff == null){
            throw new WNException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        File imgDocument = new File(WNConstant.BRAND_IMG_FILE);
        String fileName = UUID.randomUUID().toString()+file.getOriginalFilename();
        try {
            file.transferTo(new File(imgDocument,fileName));
        } catch (Exception e) {
            throw new WNException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
        return WNConstant.BRAND_IMG_URL+fileName;
    }

    public Map<String, Object> getSignature() {

        try {
            long expireTime = prop.getExpireTime();
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, prop.getDir());

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);
            Map<String, Object> respMap = new LinkedHashMap<String, Object>();
            respMap.put("accessId", prop.getAccessKeyId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", prop.getDir());
            respMap.put("host", prop.getHost());
            respMap.put("expire", expireEndTime);
            // respMap.put("expire", formatISO8601Date(expiration));
            return respMap;
        }catch (Exception e) {
            throw new WNException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

    }
}
