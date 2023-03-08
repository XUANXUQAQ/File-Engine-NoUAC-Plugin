package file.engine.nouac.Plugin;

import file.engine.nouac.Plugin.configs.ProgramConfigs;
import file.engine.nouac.Plugin.utils.ColorUtils;
import file.engine.nouac.Plugin.utils.VersionUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class PluginMain extends Plugin {

    private Color backgroundColor;
    private Color labelChosenColor;
    private Color labelDefaultFontColor;
    private Color labelChosenFontColor;

    private void hideSearchBar() {
        sendEventToFileEngine("file.engine.event.handler.impl.frame.searchBar.HideSearchBarEvent");
    }

    /**
     * Deprecated
     * You should load the theme by loadPlugin and update the theme by configsChanged
     *
     * @param defaultColor    This is the color's RGB code. When the label isn't chosen, it will be shown as this color.
     * @param choseLabelColor This is the color's RGB code. When the label is chosen, it will be shown as this color.
     * @param borderColor     This is the border color of File-Engine, it is deprecated, you should not set labels' border in plugin.
     *                        However, you can still know the border color through this parameter.
     * @see #loadPlugin(Map)
     * @see #configsChanged(Map)
     * This is used for File-Engine to tell the plugin the current Theme settings.
     * This function will be called when the plugin is being loaded.
     * You can use them on method showResultOnLabel(String, JLabel, boolean).
     * When the label is chosen by user, you could set the label background as chosenLabelColor.
     * When the label isn't chosen by user, you could set the label background as defaultColor.
     * You can save the color and use it at function showResultOnLabel(String, JLabel, boolean)
     * @see #showResultOnLabel(String, JLabel, boolean)
     */
    @Override
    @Deprecated
    public void setCurrentTheme(int defaultColor, int choseLabelColor, int borderColor) {

    }

    /**
     * When the configuration file of File-Engine is updated, this function will be called.
     *
     * @param configs configs
     */
    @Override
    public void configsChanged(Map<String, Object> configs) {
        backgroundColor = new Color((Integer) configs.get("defaultBackground"));
        labelChosenColor = new Color((Integer) configs.get("labelColor"));
        labelDefaultFontColor = new Color((Integer) configs.get("fontColor"));
        labelChosenFontColor = new Color((Integer) configs.get("fontColorWithCoverage"));
    }

    /**
     * When the search bar textChanged, this function will be called.
     *
     * @param text Example : When you input "&gt;examplePlugin TEST" to the search bar, the param will be "TEST"
     */
    @Override
    public void textChanged(String text) {
        if (">set".equals(text)) {
            hideSearchBar();
            ProgramConfigs.INSTANCE.openConfigDir();
        } else if (">relaunch".equals(text)) {
            hideSearchBar();
            try {
                ProgramConfigs.INSTANCE.openAllPrograms();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (text != null && text.length() > 1) {
                text = text.substring(1);
                if ("set".startsWith(text)) {
                    addToResultQueue(">set");
                } else if ("relaunch".startsWith(text)) {
                    addToResultQueue(">relaunch");
                } else {
                    addToResultQueue(">set");
                    addToResultQueue(">relaunch");
                }
            } else {
                addToResultQueue(">set");
                addToResultQueue(">relaunch");
            }
        }
    }

    /**
     * When File-Engine is starting, the function will be called.
     * You can initialize your plugin here
     */
    @Override
    public void loadPlugin(Map<String, Object> configs) {
        try {
            backgroundColor = new Color((Integer) configs.get("defaultBackground"));
            labelChosenColor = new Color((Integer) configs.get("labelColor"));
            labelDefaultFontColor = new Color((Integer) configs.get("fontColor"));
            labelChosenFontColor = new Color((Integer) configs.get("fontColorWithCoverage"));
            ProgramConfigs.INSTANCE.openAllPrograms();
        } catch (IOException e) {
            String message = "message: " + e.getMessage();
            String cause = "cause: " + e.getCause();
            System.out.println("-----------------------NoUAC plugin init error------------------------------\n" + message +
                    "\n" + cause + "-----------------------NoUAC plugin init error------------------------------");
            throw new RuntimeException(e);
        }
    }

    /**
     * Deprecated
     * You should use loadPlugin with configs
     *
     * @see #loadPlugin(Map)
     * When File-Engine is starting, the function will be called.
     * You can initialize your plugin here
     */
    @Override
    @Deprecated
    public void loadPlugin() {

    }

    /**
     * When File-Engine is closing, the function will be called.
     */
    @Override
    public void unloadPlugin() {

    }

    /**
     * Invoked when a key has been released.See the class description for the swing KeyEvent for a definition of a key released event.
     * Notice : Up and down keys will not be included (key code 38 and 40 will not be included).
     *
     * @param e      KeyEvent, Which key on the keyboard is released.
     * @param result Currently selected content.
     */
    @Override
    public void keyReleased(KeyEvent e, String result) {

    }

    /**
     * Invoked when a key has been pressed. See the class description for the swing KeyEvent for a definition of a key pressed event.
     * Notice : Up and down keys will not be included (key code 38 and 40 will not be included).
     *
     * @param e      KeyEvent, Which key on the keyboard is pressed.
     * @param result Currently selected content.
     */
    @Override
    public void keyPressed(KeyEvent e, String result) {
        if (e.getKeyCode() == 10) {
            if (">set".equals(result)) {
                hideSearchBar();
                ProgramConfigs.INSTANCE.openConfigDir();
            } else if (">relaunch".equals(result)) {
                hideSearchBar();
                try {
                    ProgramConfigs.INSTANCE.openAllPrograms();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Invoked when a key has been typed.See the class description for the swing KeyEvent for a definition of a key typed event.
     * Notice : Up and down keys will not be included (key code 38 and 40 will not be included).
     *
     * @param e      KeyEvent, Which key on the keyboard is pressed.
     * @param result Currently selected content.
     */
    @Override
    public void keyTyped(KeyEvent e, String result) {

    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e      Mouse event
     * @param result Currently selected content.
     */
    @Override
    public void mousePressed(MouseEvent e, String result) {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e      Mouse event
     * @param result Currently selected content
     */
    @Override
    public void mouseReleased(MouseEvent e, String result) {

    }

    @Override
    public void searchBarVisible(String showingMode) {

    }

    /**
     * Get the plugin Icon. It can be the png, jpg.
     * Make the icon small, or it will occupy too much memory.
     *
     * @return icon
     */
    @Override
    public ImageIcon getPluginIcon() {
        return new ImageIcon(Objects.requireNonNull(PluginMain.class.getResource("/icon.png")));
    }

    /**
     * Get the official site of the plugin.
     *
     * @return official site
     */
    @Override
    public String getOfficialSite() {
        return "";
    }

    /**
     * Get the plugin version.
     *
     * @return version
     */
    @Override
    public String getVersion() {
        return VersionUtil._getPluginVersion();
    }

    /**
     * Get the description of the plugin.
     * Just write the description outside, and paste it to the return value.
     *
     * @return description
     */
    @Override
    public String getDescription() {
        return """
                一个用于避免弹出UAC弹窗的插件，添加所需启动程序后，程序将在插件被加载时启动
                使用方法：
                在输入框中输入\t">nouac >set"\t进入设置文件夹
                然后编辑settings.json
                在settings.json中输入你想要以管理员方式自启动的程序
                settings.json中存放的是数组

                例子：
                [
                  {
                    "path": "Your executable file path.",
                    "workingDir": "working directory.",
                    "params": "console params.",
                    "checkExeName": "check exe process name."
                  }
                ]
                输入\t">nouac >relaunch"\t尝试重新启动所有程序
                图标来自： https://icons8.com/icon/103947/删除盾 icon by https://icons8.com""";
    }

    /**
     * Check if the current version is the latest.
     *
     * @return true or false
     * @see #getUpdateURL()
     */
    @Override
    @SuppressWarnings({"unused", "RedundantThrows"})
    public boolean isLatest() throws Exception {
        return VersionUtil._isLatest();
    }

    /**
     * Get the plugin download url.
     * Invoke when the isLatest() returns false;
     *
     * @return download url
     * @see #isLatest()
     */
    @Override
    public String getUpdateURL() {
        return VersionUtil._getUpdateURL();
    }

    /**
     * Show the content to the GUI.
     *
     * @param result   current selected content.
     * @param label    The label to be displayed.
     * @param isChosen If the label is being selected.
     *                 If so, you are supposed to set the label at a different background.
     *                 <p>
     *                 You can only set the icon, text and background of this label, please do not set other attributes, such as borders...
     */
    @Override
    public void showResultOnLabel(String result, JLabel label, boolean isChosen) {
        String defaultFontColor = "#" + ColorUtils.parseColorHex(labelDefaultFontColor);
        String fontColorHighLight = "#" + ColorUtils.parseColorHex(labelChosenFontColor);
        String html = "<html><body><span%s>%s</span></body></html>";
        if (">set".equals(result)) {
            result = result + "<br>" + "打开设置文件位置";
        } else if (">relaunch".equals(result)) {
            result = result + "<br>" + "重新打开所有程序（并不会关闭当前打开的程序）";
        }
        html = String.format(html, String.format(" style=\"color: %s\"", isChosen ? fontColorHighLight : defaultFontColor), result);
        label.setText(html);
        if (isChosen) {
            label.setBackground(labelChosenColor);
        } else {
            label.setBackground(backgroundColor);
        }
    }

    @Override
    public String getAuthor() {
        return "XUANXU";
    }


    /**
     * Broadcast the current processing event.
     * You can send an event to File-Engine by sendEventToFileEngine(String eventFullClassPath, Object... params)
     *
     * @param c             event class
     * @param eventInstance event instance
     * @see #sendEventToFileEngine(String, Object...)
     * @see #sendEventToFileEngine(Event)
     */
    @Override
    public void eventProcessed(Class<?> c, Object eventInstance) {

    }

    //--------------------------------------------------------------------------------------------------------------

    /**
     * Do Not Remove, this is used for File-Engine to get message from the plugin.
     * You can show message using "displayMessage(String caption, String message)"
     *
     * @return String[2], the first string is caption, the second string is message.
     * @see #displayMessage(String, String)
     */
    @SuppressWarnings("unused")
    public String[] getMessage() {
        return _getMessage();
    }

    /**
     * Do Not Remove, this is used for File-Engine to get results from the plugin
     * You can add result using "addToResultQueue(String result)".
     *
     * @return result
     * @see #addToResultQueue(String)
     */
    @SuppressWarnings("unused")
    public String pollFromResultQueue() {
        return _pollFromResultQueue();
    }

    /**
     * Do Not Remove, this is used for File-Engine to check the API version.
     *
     * @return Api version
     */
    @SuppressWarnings("unused")
    public int getApiVersion() {
        return _getApiVersion();
    }

    /**
     * Do Not Remove, this is used for File-Engine to clear results to prepare for the next time.
     *
     * @see #addToResultQueue(String)
     * @see #pollFromResultQueue()
     */
    @SuppressWarnings("unused")
    public void clearResultQueue() {
        _clearResultQueue();
    }

    /**
     * Do Not Remove, this is used for File-Engine to poll the event that send from the plugin.
     * The object array contains two parts.
     * object[0] contains the fully-qualified name of class.
     * object[1] contains the params that the event need to build an instance.
     * To send an event to File-Engine
     *
     * @return Event
     * @see #sendEventToFileEngine(String, Object...)
     * @see #sendEventToFileEngine(Event)
     */
    @SuppressWarnings("unused")
    public Object[] pollFromEventQueue() {
        return _pollFromEventQueue();
    }

    /**
     * Do Not Remove, this is used for File-Engine to replace the handler which the plugin is registered.
     * The object array contains two parts.
     * object[0] contains the fully-qualified name of class.
     * object[1] contains a consumer to hande the event.
     *
     * @return Event handler
     * @see #registerFileEngineEventHandler(String, BiConsumer)
     */
    @SuppressWarnings("unused")
    public Object[] pollFromEventHandlerQueue() {
        return _pollEventHandlerQueue();
    }

    /**
     * Do Not Remove, this is used for File-Engine to restore the handler which the plugin is registered.
     *
     * @return Event class fully-qualified name
     * @see #restoreFileEngineEventHandler(String)
     */
    @SuppressWarnings("unused")
    public String restoreFileEngineEventHandler() {
        return _pollFromRestoreQueue();
    }
}
