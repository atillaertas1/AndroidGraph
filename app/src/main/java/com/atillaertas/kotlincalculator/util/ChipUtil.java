package com.atillaertas.kotlincalculator.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ChipUtil {
    private final String TAG = ChipUtil.class.getSimpleName();
    //AllWinner
    //private final String SYS_INFO_PATH = "/sys/class/sunxi_info/sys_info";
    //RockChip
    private final String SYS_INFO_PATH = "/proc/cpuinfo";
    private final String CHIP_SERIAL = "Serial";
    private static ChipUtil sInstance;

    //单例模式 构造方法为私有
    private ChipUtil() {}

    //单例模式
    public static ChipUtil getInstance(){
        if (sInstance == null){
            synchronized (ChipUtil.class){
                if (sInstance == null){
                    sInstance = new ChipUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取该屏的Chip ID 唯一码
     * @return 返回值即为该机的Chip ID
     */
    public String getChipId(){
        String result = null;
        Properties prop = new Properties();
        try{
            //读取属性文件cpuinfo
            InputStream in = new BufferedInputStream (new FileInputStream(SYS_INFO_PATH));
            prop.load(in);     ///加载属性列表
            result = prop.getProperty(CHIP_SERIAL);
            in.close();
            return result;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
