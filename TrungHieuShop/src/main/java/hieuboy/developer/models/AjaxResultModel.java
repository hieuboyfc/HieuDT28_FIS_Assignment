package hieuboy.developer.models;

import lombok.Data;

@Data
public class AjaxResultModel<H> {

    private H resultData;

    private Boolean result;

    private Long code;

    private String message;
}
