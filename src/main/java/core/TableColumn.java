package core;

import lombok.Builder;
import lombok.Data;

/**
 * @author wb
 * @date 2020/1/14
 */
@Data
@Builder
public class TableColumn {
    private String fieldName;
    private String columnName;
    private boolean nullable;
}
