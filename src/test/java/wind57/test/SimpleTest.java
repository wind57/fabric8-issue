package wind57.test;

import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.ConfigMapListBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wind57.AllConfigMaps;

import java.util.HashMap;
import java.util.Map;

@EnableKubernetesMockClient
public class SimpleTest {

    private static final String API = "/api/v1/namespaces/default/configmaps";

    private static KubernetesMockServer mockServer;

    private static KubernetesClient mockClient;

    @BeforeAll
    static void beforeAll() {
        System.setProperty(Config.KUBERNETES_MASTER_SYSTEM_PROPERTY, mockClient.getConfiguration().getMasterUrl());
        System.setProperty(Config.KUBERNETES_TRUST_CERT_SYSTEM_PROPERTY, "true");
        System.setProperty(Config.KUBERNETES_AUTH_TRYKUBECONFIG_SYSTEM_PROPERTY, "false");
        System.setProperty(Config.KUBERNETES_AUTH_TRYSERVICEACCOUNT_SYSTEM_PROPERTY, "false");
        System.setProperty(Config.KUBERNETES_HTTP2_DISABLE, "true");
        System.setProperty(Config.KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY, "1000000");
    }

    @Test
    void testAll() {

        mockClient.getConfiguration().setRequestRetryBackoffLimit(0);
        mockClient.getConfiguration().setRequestRetryBackoffInterval(0);

        Map<String, String> data = new HashMap<>();
        data.put("some.prop", "theValue");
        data.put("some.number", "0");

        mockServer.expect().withPath(API).andReturn(500, "Error").times(5);
        mockServer
                .expect().withPath(API).andReturn(200, new ConfigMapListBuilder().withItems(new ConfigMapBuilder()
                        .withNewMetadata().withName("application").endMetadata().addToData(data).build()).build())
                .once();

        new AllConfigMaps().all(mockClient);

    }

}
