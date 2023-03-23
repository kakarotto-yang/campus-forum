package yuren.work.boot.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "post搜索条件")
@Data
public class PostQueryVo {

    @ApiModelProperty(value = "帖子标题")
    private String  title;

    @ApiModelProperty(value = "帖子内容")
    private String  content;

    @ApiModelProperty(value = "创建时间")
    private String createTimeBegin;

    @ApiModelProperty(value = "创建时间")
    private String createTimeEnd;

    @ApiModelProperty(value = "帖子类型")
    private String  postType;

}
