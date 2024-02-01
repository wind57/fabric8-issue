package wind57;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.List;

public class AllConfigMaps {

    public List<ConfigMap> all(KubernetesClient client) {
        return client.configMaps().inNamespace("default").list().getItems();
    }

}
