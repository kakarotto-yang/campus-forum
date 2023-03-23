package yuren.work.boot.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel(value = "评论信息实体类")
@Data
public class Comment {
    @ApiModelProperty(value = "评论id")
    private Integer id;

    @ApiModelProperty(value = "评论内容")
    private String  content;

    @ApiModelProperty(value = "评论帖子")
    private Integer postId;

    @ApiModelProperty(value = "评论者")
    private Integer fromUid;

    @ApiModelProperty(value = "评论者")
    @TableField(exist = false)
    private User from;

    @ApiModelProperty(value = "评论者")
    @TableField(exist = false)
    private List<Reply> replyList;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "逻辑删除（0：正常 1：删除）")
    @TableLogic
    private Integer isDel;
}
