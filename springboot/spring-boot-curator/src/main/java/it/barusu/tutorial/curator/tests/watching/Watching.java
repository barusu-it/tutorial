package it.barusu.tutorial.curator.tests.watching;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.IntStream;

import static org.apache.zookeeper.Watcher.Event.EventType.NodeDataChanged;

@Slf4j
public class Watching {

    private static final String WATCHING_PATH = "/tests/test4watching";
    private static final String ZOOKEEPER_URL = "zk1-qa.yeshj.com:2181";
//    public static final String ZOOKEEPER_URL = "192.168.38.190:2181";


    public static void main(String... args) throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(ZOOKEEPER_URL)
                .retryPolicy(new ExponentialBackoffRetry(3000, 3))
                .build();
        curator.getConnectionStateListenable()
                .addListener((c, s) -> System.out.println("curator client state: " + s));
        curator.start();

        buildRootPathIfNotExisted(curator);

        watching(curator);

        Scanner scanner = new Scanner(System.in);
        log.info("Enter to continue.");
        scanner.nextLine();

        CloseableUtils.closeQuietly(curator);
        log.info("Watching End.");
    }

    private static void watching(CuratorFramework curator) throws Exception {
        curator.getData()
                .usingWatcher((CuratorWatcher) event -> {
                    log.info("watching event, {}, {}", event.getType().name(), event.getPath());

                    if (NodeDataChanged.equals(event.getType())) {
                        IntStream.range(1, 20).forEach(it -> {
                            try {
                                Thread.sleep(1000L);
                                log.info("sleeping {}", it);
                            } catch (InterruptedException e) {
                                // ignore
                            }
                        });
                    }

                    watching(curator);
                }).forPath(WATCHING_PATH);
    }


    private static void buildRootPathIfNotExisted(CuratorFramework curator) throws Exception {
        boolean existed = curator.checkExists().forPath(WATCHING_PATH) != null;

        if (!existed) {
            curator.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(WATCHING_PATH,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).getBytes());
            log.info("Create watching path.");
        }
    }

}
