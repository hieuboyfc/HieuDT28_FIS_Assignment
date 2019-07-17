package hieuboy.developer.models;

import lombok.Data;

import java.util.List;

@Data
public class AjaxResult {
    
    private List<?> resultData;

    private Boolean result;

    private Integer code;

    private String message;
}
