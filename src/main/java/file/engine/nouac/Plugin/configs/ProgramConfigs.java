package file.engine.nouac.Plugin.configs;


import com.google.gson.Gson;
import file.engine.nouac.Plugin.utils.OpenUtils;
import file.engine.nouac.Plugin.utils.gson.GsonUtil;

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
        var sb = new StringBuilder();
        try (var reader = new BufferedReader(new InputStreamReader(new FileInputStream(savePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        List list = gson.fromJson(sb.toString(), List.class);
        if (list != null) {
            programs.clear();
            list.forEach(each -> {
                Map<String, String> tmp = (Map<String, String>) each;
                String path = tmp.getOrDefault("path", "");
                String workingDir = tmp.getOrDefault("workingDir", "");
                String params = tmp.getOrDefault("params", "");
                String checkExeName = tmp.getOrDefault("checkExeName", "");
                var launchWrapper = new LaunchWrapper(path, workingDir, params, checkExeName);
                programs.add(launchWrapper);
            });
            writeToFile();
        }
    }

    private void writeToFile() throws IOException {
        Gson gson = GsonUtil.INSTANCE.getGson();
        String json = gson.toJson(programs);
        try (var writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(savePath), StandardCharsets.UTF_8))) {
            writer.write(json);
        }
    }
}
