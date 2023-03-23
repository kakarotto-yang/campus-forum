package yuren.work.boot.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "关注信息实体类")
@Data
public class Follow {
    @ApiModelProperty(value = "关注信息id")
    private Integer id;

    @ApiModelProperty(value = "关注者id")
    private Integer uid;

    @ApiModelProperty(value = "被关注id")
    private Integer fid;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "状态（0：取消关注 1：正常）")
    private Integer status;
}
