package it.barusu.tutorial.embedzookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.test.InstanceSpec;
import org.apache.curator.test.TestingCluster;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class EmbedZookeeperTests {

    private TestingCluster cluster;

    @Before
    public void before() throws Exception {
        List<InstanceSpec> specs = IntStream.of(0, 1, 2)
                .mapToObj(it -> new InstanceSpec(null, it + 2181, it + 3181,
                        it + 4181, true, it,
                        10000, 100, null, "127.0.0.1"))
                .collect(Collectors.toList());

        cluster = new TestingCluster(specs);

        cluster.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                cluster.stop();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }));
    }

    @After
    public void after() throws IOException {
        cluster.stop();
    }

    @Test
    public void testLog() throws InterruptedException {
        Thread.sleep(2000L);

        log.info("test log.");
    }
}
