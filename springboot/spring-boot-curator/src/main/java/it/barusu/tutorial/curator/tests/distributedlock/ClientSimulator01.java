package it.barusu.tutorial.curator.tests.distributedlock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.nodes.PersistentNode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static it.barusu.tutorial.curator.tests.distributedlock.SimulatorHelper.LOCK_PATH;
import static it.barusu.tutorial.curator.tests.distributedlock.SimulatorHelper.ZOOKEEPER_URL;

@Slf4j
public class ClientSimulator01 {

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

        Scanner scanner = new Scanner(System.in);
        log.info("Enter to continue.");
        scanner.nextLine();

        // comment code for simulate stop unsafely
//        CloseableUtils.closeQuietly(ephemeralNode);
//        CloseableUtils.closeQuietly(curator);
        log.info("Client 01 End.");

    }
}
