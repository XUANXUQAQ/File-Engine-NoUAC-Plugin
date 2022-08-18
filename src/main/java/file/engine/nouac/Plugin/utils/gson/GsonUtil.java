package file.engine.nouac.Plugin.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public enum GsonUtil {
    INSTANCE;
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final DataDataTypeAdapter dataDataTypeAdapter = new DataDataTypeAdapter();
    @SuppressWarnings("rawtypes")
    private final Type mapType = new TypeToken<Map>(){}.getType();

    public Gson getGson() {
        return gsonBuilder.setPrettyPrinting().registerTypeAdapter(mapType, dataDataTypeAdapter).create();
    }
}
