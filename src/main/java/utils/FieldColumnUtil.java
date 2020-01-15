package utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wb
 * @date 2020/1/14
 */
public class FieldColumnUtil {
    public static String field2Column(String field) {
        List<String>  sliceList = FieldColumnUtil.ignoreBlank(StringUtils.defaultIfBlank(field, "").split("(?=[A-Z])"));
        return sliceList.stream().map(StringUtils::lowerCase).collect(Collectors.joining("_"));
    }

    public static void main(String[] args) {
        System.out.println(FieldColumnUtil.field2Column( "dadsIfaPP"));
    }

    public static List<String> ignoreBlank(String[] strings) {
        return Arrays.stream(strings).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }
}
