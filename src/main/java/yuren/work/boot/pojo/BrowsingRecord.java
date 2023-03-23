package yuren.work.boot.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@ApiModel(value = "浏览详细信息实体类")
@Data
public class BrowsingRecord {
    @ApiModelProperty(value = "浏览id")
    private Integer id;

    @ApiModelProperty(value = "浏览者id")
    private Integer uid;

    @ApiModelProperty(value = "浏览者IP")
    private String ip;

    @ApiModelProperty(value = "浏览者操作预警等级 0-正常，1-告警，2-危险，默认0")
    private Integer operationLevel;

    @ApiModelProperty(value = "操作行为")
    private String operationAction;

    @ApiModelProperty(value = "请求地址")
    private String reqUrl;

    @ApiModelProperty(value = "操作结果")
    private String reqResult;

    @ApiModelProperty(value = "请求时间")
    private Date reqDate;
}
