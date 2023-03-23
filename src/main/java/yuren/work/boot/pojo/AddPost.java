package yuren.work.boot.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "未发表帖子信息实体类")
@Data
public class AddPost {
    @ApiModelProperty(value = "未发表帖子id")
    private Integer id;

    @ApiModelProperty(value = "未发表帖子内容")
    private String add_content;

    @ApiModelProperty(value = "未发表帖子作者")
    private User author;
}
