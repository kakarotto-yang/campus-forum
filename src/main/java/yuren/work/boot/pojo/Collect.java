package yuren.work.boot.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "收藏信息实体类")
@Data
public class Collect {
    @ApiModelProperty(value = "收藏信息id")
    private Integer id;

    @ApiModelProperty(value = "收藏者id")
    private Integer uid;

    @ApiModelProperty(value = "收藏的帖子id")
    private Integer postId;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "状态（0：取消点赞 1：正常）")
    private Integer status;
}
