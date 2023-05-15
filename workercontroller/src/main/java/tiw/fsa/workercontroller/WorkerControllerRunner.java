package tiw.fsa.workercontroller;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;

@Component
public class WorkerControllerRunner  implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(WorkerControllerRunner.class);
    @Override
    public void run(String... args) throws Exception {
        ApiClient client;
        String kubeConfigPath = System.getenv("KUBECONFIG");
        if (kubeConfigPath == null || kubeConfigPath.strip().length() == 0) {
            kubeConfigPath = System.getenv("HOME") + "/.kube/config";
        }
        if (!new File(kubeConfigPath).exists()) {
            kubeConfigPath = null;
        }
        if (kubeConfigPath == null) {
            log.info("kubeconfig not found, assuming in-cluster config");
            client = ClientBuilder.cluster().build();
        } else {
            log.info("kubeconfig found at {}", kubeConfigPath);
            // loading the out-of-cluster config, a kubeconfig from file-system
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(
                    new FileReader(kubeConfigPath))).build();
        }
        // set the global default api-client to the one from above
        Configuration.setDefaultApiClient(client);
        // the CoreV1Api loads default api-client from global configuration.
        CoreV1Api api = new CoreV1Api();
        var list = api.listNamespacedPod(
                "default", null, null, null,
                null, null, null, null,
                null, null, null);
        for (var item: list.getItems()) {
            log.info("Found deployment {}", item.getMetadata().getName());
        }
        AppsV1Api apps = new AppsV1Api();
        var scale = apps.readNamespacedDeploymentScale("api-deployment","default", null);
        var spec = scale.getSpec();
        spec.setReplicas(3);
        scale.setSpec(spec);
        apps.replaceNamespacedDeploymentScale("api-deployment","default",
                scale,null,null,null,null);
    }
}
