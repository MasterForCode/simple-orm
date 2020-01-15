package core;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wb
 * @date 2020/1/14
 */
public class TableManager {
    public static Map<Class<?>, TableMapper<?>> map = new HashMap<>();

    public static <T> void createTableMapper(Class<T> clz) {
        TableMapper<?> tableMapper = map.get(clz);
        if (tableMapper == null) {
            tableMapper = new TableMapper<>(clz);
            if (!tableMapper.isIgnore()) {
                map.put(clz, new TableMapper<>(clz));
            }
        }
    }

    public static void scan(String[] entityPath) {
        Arrays.stream(entityPath).map(path -> new Reflections(path, new TypeAnnotationsScanner())).flatMap(item -> item.getTypesAnnotatedWith(Entity.class, true).stream()).forEach(TableManager::createTableMapper);
    }

    public static void main(String[] args) {
        TableManager.scan(new String[]{"entity"});
        System.out.println(map.size());
    }


}
