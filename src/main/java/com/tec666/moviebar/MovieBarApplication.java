package com.tec666.moviebar;

import com.tec666.moviebar.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class MovieBarApplication {

    public static ThreadPoolExecutor threadPoolExecutor;

    public static String HOST_IP = null;

    private final static Logger logger = LoggerFactory.getLogger(MovieBarApplication.class);

    static {
        System.setProperty(net.sf.ehcache.CacheManager.ENABLE_SHUTDOWN_HOOK_PROPERTY, "true");
    }

    public static void main(String[] args) {

        List<String> localIpList = IpUtil.getLocalIpList();
        if (localIpList.size() == 0) {
            logger.error("无法获取本机IP，无法启动");
            return;
        }
        HOST_IP = localIpList.get(0);

        threadPoolExecutor = new ThreadPoolExecutor(12, 30, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(Integer.MAX_VALUE), new ThreadPoolExecutor.AbortPolicy());

        SpringApplication.run(MovieBarApplication.class, args);
    }

}
