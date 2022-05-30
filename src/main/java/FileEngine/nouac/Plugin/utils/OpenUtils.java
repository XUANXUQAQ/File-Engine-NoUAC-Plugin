package FileEngine.nouac.Plugin.utils;

import java.io.File;
import java.io.IOException;

public class OpenUtils {

    public static void openFolderByExplorer(String dir) throws IOException {
        Runtime.getRuntime().exec("explorer.exe /select, \"" + dir + "\"");
    }

    /**
     * 以管理员方式运行文件，失败则打开文件位置
     *
     * @param path 文件路径
     */
    public static void openWithAdmin(String path, String workingDir, String params) {
        File file = new File(path);
        if (file.isDirectory()) {
            try {
                openFolderByExplorer(path);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("NoUAC: Execute failed");
            }
            return;
        }
        if (file.exists()) {
            try {
                String command = file.getAbsolutePath();
                String start = "cmd.exe /c start " + command.substring(0, 2);
                String end = "\"" + command.substring(2) + "\"";
                String full = start + end + " " + params;
                Runtime.getRuntime().exec(full, null, new File(workingDir));
            } catch (IOException e) {
                //打开上级文件夹
                try {
                    openFolderByExplorer(file.getAbsolutePath());
                } catch (IOException e1) {
                    System.out.println("NoUAC: Execute failed");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("NoUAC: File not exist");
        }
    }
}
