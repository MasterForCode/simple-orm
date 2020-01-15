package core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import utils.FieldColumnUtil;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wb
 * @date 2020/1/14
 */
@Data
@Slf4j
public class TableMapper<T> {
    private boolean ignore;
    private String tableName;
    private Class<T> tableBean;
    private String idField;
    private List<String> idFields = new ArrayList<>();
    private Boolean autoincrement = false;
    private List<String> fieldNames = new ArrayList<>();
    private List<TableColumn> fieldColumnMapping = new ArrayList<>();

    public TableMapper(Class<T> clz) {
        List<Field> fieldList = Arrays.stream(clz.getDeclaredFields()).filter(field -> field.getAnnotation(Transient.class) == null).collect(Collectors.toList());
        boolean validation = TableMapper.validator(fieldList);
        if (!validation) {
            this.ignore = true;
            return;
        }
        this.tableBean = clz;
        Table table = clz.getAnnotation(Table.class);
        this.tableName = table.name();

        fieldList.forEach(field -> {
            TableColumn tableColumn = TableColumn.builder().fieldName(field.getName()).build();
            Column column = field.getAnnotation(Column.class);
            tableColumn.setColumnName(StringUtils.defaultString(FieldColumnUtil.field2Column(field.getName())));
            tableColumn.setNullable(column == null || column.nullable());
            fieldColumnMapping.add(tableColumn);
            this.fieldNames.add(field.getName());
        });
        List<Field> idFieldList = fieldList.stream().filter(field -> field.getAnnotation(Id.class) != null).collect(Collectors.toList());
        if (idFieldList.size() == 1) {
            this.idField = idFieldList.get(0).getName();
            if (idFieldList.get(0).getAnnotation(GeneratedValue.class) != null) {
                this.autoincrement = true;
            }
        }
        if (idFieldList.size() > 1) {
            idFieldList.forEach(field -> this.idFields.add(field.getName()));
        }
    }

    public static boolean validator(List<Field> fieldList) {
        boolean flag = false;
        flag = TableMapper.count(fieldList, field -> field.getAnnotation(Id.class) != null) <= 1;
        if (!flag) {
            log.warn("Id annotations can only have 1 and this entity will be ignore");
            return false;
        }
        flag = TableMapper.count(fieldList, field -> field.getAnnotation(GeneratedValue.class) != null) <= 1;
        if (!flag) {
            log.warn("GeneratedValue annotations can only have 1 and this entity will be ignore");
            return false;
        }
        return true;
    }

    public static <T> int count(List<T> list, Function<T, Boolean> function) {
        return Math.toIntExact(list.stream().filter(function::apply).count());
    }
}
