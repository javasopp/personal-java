package com.sopp.all.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * mq加密工具类
 *
 * @author sopp
 */
@Component
public class MyEncryptHelper {

    public static byte[] aesKey;

    public MyEncryptHelper() {
        String rawkey = getKey();
        if (StringUtils.isNotBlank(rawkey)) {
            String keyS = getMd5MultiTime(rawkey, 5);
            try {
                aesKey = keyS.getBytes("utf8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public String getMd5MultiTime(String raw, int time) {
        String hash = "";
        if (time < 1) {
            return hash;
        }
        Digester md5 = new Digester(DigestAlgorithm.MD5);

        for (int i = 0; i < time; i++) {
            if (i == 0) {
                hash = raw;
            }
            hash = md5.digestHex(hash);
        }
        return hash;
    }

    /**
     * Description: 加密方法
     *
     * @param rawString
     * @return java.lang.String
     * @throws
     * @auther Sopp
     * @date: 2020-03-13 11:16
     */
    public String encryptStringAesBase64(String rawString) {
        if (aesKey == null) {
            return "";
        }
        AES aes = SecureUtil.aes(aesKey);
        byte[] enByte = aes.encrypt(rawString);

        return aes.encryptBase64(rawString);
    }

    /**
     * Description: 解密算法
     *
     * @param encryptedString
     * @return java.lang.String
     * @throws
     * @auther Sopp
     * @date: 2020-03-13 11:16
     */
    public String decryptStringAesBase64(String encryptedString) {
        if (aesKey == null) {
            return "";
        }
        AES aes = SecureUtil.aes(aesKey);

        return aes.decryptStr(encryptedString);
    }

    /**
     * @功能: 通过文件获取原始key信息
     * @参数: key
     * @结果: 配置参数
     */
    public String getKey() {
        String str = "";
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("key");
            int length = inputStream.available();
            byte[] bytes = new byte[length];
            inputStream.read(bytes);
            inputStream.close();
            str = new String(bytes, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
