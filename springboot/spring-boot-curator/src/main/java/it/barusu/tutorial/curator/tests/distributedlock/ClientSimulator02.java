package it.barusu.tutorial.curator.tests.distributedlock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.nodes.PersistentNode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static it.barusu.tutorial.curator.tests.distributedlock.SimulatorHelper.*;
import static org.apache.zookeeper.Watcher.Event.EventType.NodeDeleted;

@Slf4j
public class ClientSimulator02 {

    public static void main(String... args) throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(ZOOKEEPER_URL)
                .retryPolicy(new ExponentialBackoffRetry(3000, 3))
                .build();
        curator.getConnectionStateListenable()
                .addListener((c, s) -> System.out.println("curator client state: " + s));
        curator.start();

        SimulatorHelper.buildRootPathIfNotExisted(curator);

        PersistentNode ephemeralNode = new PersistentNode(curator,
                CreateMode.EPHEMERAL_SEQUENTIAL, true, LOCK_PATH, "0".getBytes());
        ephemeralNode.start();
        ephemeralNode.waitForInitialCreate(10, TimeUnit.SECONDS);

        String path = ephemeralNode.getActualPath();
        log.info("create ephemeral node path: " + path);

        List<String> children = curator.getChildren().forPath(LOCK_ROOT_PATH);
        children.sort(Comparator.comparing(SimulatorHelper::getSequential));

        log.info("children node: " + Arrays.toString(children.toArray()));

        String subPath = FilenameUtils.getName(path);
        log.info("Sub path: " + subPath);
        int index = children.indexOf(subPath);
        log.info("file index: " + index);
        if (index > 0) {
            curator.getData()
                    .usingWatcher((CuratorWatcher) (event) -> {
                        if (NodeDeleted.equals(event.getType())) {
                            log.info("Ephemeral node [" + event.getPath() + "] is deleted.");
                        }
                    }).forPath(LOCK_ROOT_PATH + "/" + children.get(index - 1));
        }

        Scanner scanner = new Scanner(System.in);
        log.info("Enter to continue.");
        scanner.nextLine();

        CloseableUtils.closeQuietly(ephemeralNode);
        CloseableUtils.closeQuietly(curator);
        log.info("Client 01 End.");

    }
}
