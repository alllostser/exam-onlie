package org.springblade.common.tool;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName GuavaCacheUtils
 * @Description //基于Guava Cahce缓存类
 * @Author GuXinYu
 * @Date 2020/5/22 23:47
 * @Version 1.0
 **/
public class GuavaCacheUtils {
    //    创建logback的logger
    private static Logger logger = LoggerFactory.getLogger(GuavaCacheUtils.class);
    //声明一个静态的内存块,guava里面的本地缓存
    private static LoadingCache<String,String> codeCache = CacheBuilder.newBuilder()
            //构建本地缓存，调用链的方式 ,1000是设置缓存的初始化容量，maximumSize是设置缓存最大容量，当超过了最大容量，guava将使用LRU算法（最少使用算法），来移除缓存项
            //expireAfterAccess(12,TimeUnit.HOURS)设置缓存有效期为12个小时
            .initialCapacity(1000)//初始化缓存项为1000
            .maximumSize(10000)//设置缓存项最大值不超过10000
            .expireAfterAccess(60*60*24, TimeUnit.SECONDS)//定时回收
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });
    /*
     * 添加本地缓存
     * */
    public static void setKey(String key, String value) {
        codeCache.put(key, value);
    }

    /*
     * 得到本地缓存
     * */
    public static String getKey(String key) {
        String value = null;
        try {
            value= codeCache.get(key);
            if ("null".equals(value)) {
                return  null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("getKey()方法错误",e);
        }
        return null;
    }

    public static void main(String[] args) {
        int count = 13;
        setKey("count", Integer.toString(count));
        setKey("count", "null");
        System.out.println(getKey("count"));
//        System.out.println( Long.valueOf(getKey("count")));
    }

}
