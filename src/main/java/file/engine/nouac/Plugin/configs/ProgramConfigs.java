package file.engine.nouac.Plugin.configs;


import file.engine.nouac.Plugin.utils.OpenUtils;
import file.engine.nouac.Plugin.utils.gson.GsonUtil;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum ProgramConfigs {
    INSTANCE;

    public static class LaunchWrapper {
        public final String path;
        public final String workingDir;
        public final String params;
        public final String checkExeName;

        private LaunchWrapper(String path, String workingDir, String params, String checkExeName) {
            this.path = path;
            this.workingDir = workingDir;
            this.params = params;
            this.checkExeName = checkExeName;
        }
    }

    private static final String savePath = "plugins/Plugin configuration files/NoUAC/settings.json";
    private final ArrayList<LaunchWrapper> programs = new ArrayList<>();

    public void openAllPrograms() throws IOException {
        init();
        programs.forEach(OpenUtils::openWithAdmin);
    }

    public void openConfigDir() {
        try {
            OpenUtils.openFolderByExplorer(Path.of(savePath).toAbsolutePath().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void init() throws IOException {
        Path _savePath = Path.of(savePath);
        if (!Files.exists(_savePath)) {
            Files.createDirectories(Path.of(_savePath.getParent().toAbsolutePath().toString()));
            Files.createFile(_savePath);
        }
        Gson gson = GsonUtil.INSTANCE.getGson();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(savePath), StandardCharsets.UTF_8))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            List list = gson.fromJson(sb.toString(), List.class);
            if (list != null) {
                programs.clear();
                list.forEach(each -> {
                    Map<String, String> tmp = (Map<String, String>) each;
                    String path = tmp.get("path");
                    String workingDir = tmp.get("workingDir");
                    String params = tmp.get("params");
                    String checkExeName = tmp.get("checkExeName");
                    LaunchWrapper launchWrapper = new LaunchWrapper(path, workingDir, params, checkExeName);
                    programs.add(launchWrapper);
                });
            }
        }
    }
}
