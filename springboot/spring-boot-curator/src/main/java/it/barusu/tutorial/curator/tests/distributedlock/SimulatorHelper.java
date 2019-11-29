package it.barusu.tutorial.curator.tests.distributedlock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SimulatorHelper {
    public static final String ZOOKEEPER_URL = "zk1-qa.yeshj.com:2181";
    public static final String LOCK_ROOT_PATH = "/tests/simulator";
    public static final String LOCK_SUB_PATH_PREFIX = "lock";
    public static final String LOCK_PATH = LOCK_ROOT_PATH + "/" + LOCK_SUB_PATH_PREFIX;
    public static final String LOCK_SUB_PATH_REGEX = "(" + LOCK_SUB_PATH_PREFIX + ")(\\d*)$";


    public static void buildRootPathIfNotExisted(CuratorFramework curator) throws Exception {
        boolean existed = curator.checkExists().forPath(LOCK_ROOT_PATH) != null;

        if (!existed) {
            curator.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(LOCK_ROOT_PATH);
            log.info("Create lock root path.");
        }
    }

    public static String getSequential(String subPath) {
        Matcher matcher = Pattern.compile(LOCK_SUB_PATH_REGEX).matcher(subPath);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";
    }

    public static void main(String... args) {
        String testSubPath = "_c_1c3b2c64-5ba3-4607-b36a-56bd62ace632-lock0000000001";
        log.info("test regex: " + getSequential(testSubPath));
    }

}
