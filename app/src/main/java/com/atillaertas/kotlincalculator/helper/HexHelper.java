package com.atillaertas.kotlincalculator.helper;

public class HexHelper {
    private static HexHelper sInstance;

    //单例模式 构造方法为私有
    private HexHelper() {
    }

    //单例模式
    public static HexHelper getInstance(){
        if (sInstance == null){
            synchronized (HexHelper.class){
                if (sInstance == null){
                    sInstance = new HexHelper();
                }
            }
        }
        return sInstance;
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public byte hexToByte(String inHex) {
        byte b = (byte) Integer.parseInt(inHex, 16);
        return b;
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            // 奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            // 偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * 字节转十六进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public String bytesToHex(byte[] bytes, int size) {
        StringBuffer sb = new StringBuffer();
//        Log.d(TAG,"bytes.length = " + bytes.length);
//        for (int i = 0; i < bytes.length; i++) {
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
