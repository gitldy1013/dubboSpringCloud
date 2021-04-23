package org.cmcc.service.crypto.utils;


import com.cmpay.lemonframework.smx.Sm4Utils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author zhaolei
 * @date 2021-04-23 13:59
 */
public class CryptoUtils {

    private static final String ENCODING = "UTF-8";

    /**
     *
     * 号码借数据解密
     * */
    public static String decrypt(String secretKey,String data){
        String decryptData = "";
        byte[] secretKeyBytes = ByteUtils.fromHexString(secretKey);
        byte[] dataBytes = ByteUtils.fromHexString(data);
        try {
            byte[] decryptDataBytes = Sm4Utils.decryptEcbPadding(secretKeyBytes, dataBytes);
            decryptData = new String(decryptDataBytes, ENCODING);

        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return decryptData;

    }

    /**
    *
     * 号码借数据加密
     * */
    public static String encrypt(String secretKey,String data){
        String encryptData = "";
        try {
        byte[] secretKeyBytes = ByteUtils.fromHexString(secretKey);

            byte[] dataBytes = data.getBytes(ENCODING);
            byte[] encryptDataBytes = Sm4Utils.encryptEcbPadding(secretKeyBytes, dataBytes);
            encryptData = ByteUtils.toHexString(encryptDataBytes);

        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return encryptData;

    }
}
