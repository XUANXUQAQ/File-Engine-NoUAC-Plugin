package file.engine.nouac.Plugin.utils;

import file.engine.nouac.Plugin.configs.ProgramConfigs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class OpenUtils {

    public static void openFolderByExplorer(String dir) throws IOException {
        Runtime.getRuntime().exec("explorer.exe /select, \"" + dir + "\"");
    }

    public static boolean isProcessExist(String procName) throws IOException, InterruptedException {
        StringBuilder strBuilder = new StringBuilder();
        if (!procName.isEmpty()) {
            Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "tasklist", "/FI", "\"IMAGENAME eq " + procName + "\""});
            p.waitFor();
            String eachLine;
            try (var buffr = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.defaultCharset()))) {
                while ((eachLine = buffr.readLine()) != null) {
                    strBuilder.append(eachLine);
                }
            }
            return strBuilder.toString().contains(procName);
        }
        return false;
    }

    /**
     * 以管理员方式运行文件，失败则打开文件位置
     *
     * @param launchWrapper 需要打开的文件
     */
    public static void openWithAdmin(ProgramConfigs.LaunchWrapper launchWrapper) {
        if (!((launchWrapper.checkExeName == null) || launchWrapper.checkExeName.isEmpty())) {
            try {
                if (isProcessExist(launchWrapper.checkExeName)) {
                    return;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        File file = new File(launchWrapper.path);
        if (!file.exists()) {
            System.out.println("NoUAC: File not exist");
            return;
        }
        if (file.isDirectory()) {
            try {
                openFolderByExplorer(launchWrapper.path);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("NoUAC: Execute failed");
            }
            return;
        }
        try {
            String command = file.getAbsolutePath();
            String start = "cmd.exe /c start " + command.substring(0, 2);
            String end = "\"" + command.substring(2) + "\"";
            String full = start + end + " " + launchWrapper.params;
            Runtime.getRuntime().exec(full, null, new File(launchWrapper.workingDir));
        } catch (IOException e) {
            //打开上级文件夹
            try {
                openFolderByExplorer(file.getAbsolutePath());
            } catch (IOException e1) {
                System.out.println("NoUAC: Execute failed");
                e.printStackTrace();
            }
        }
    }
}
