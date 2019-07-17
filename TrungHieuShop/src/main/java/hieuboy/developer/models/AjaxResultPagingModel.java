package hieuboy.developer.models;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AjaxResultPagingModel implements Serializable {
    private List<?> resultList;
    private Integer pageIndex;
    private Integer pageSize;
    private String key;
    private String column;
    private Integer desending;
    private Integer totalPage;
    private Long totalRecord;
}
