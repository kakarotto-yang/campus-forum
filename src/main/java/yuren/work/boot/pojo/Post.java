package yuren.work.boot.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "帖子详细信息实体类")
@Data
public class Post {
    @ApiModelProperty(value = "帖子id")
    private Integer id;

    @ApiModelProperty(value = "帖子标题")
    private String  title;

    @ApiModelProperty(value = "帖子内容")
    private String  content;

    @ApiModelProperty(value = "帖子修改但未发布内容")
    private String  edContent;

    @ApiModelProperty(value = "帖子作者")
    @TableField(exist = false)
    private User author;

    @ApiModelProperty(value = "帖子作者id")
    private Integer uid;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "帖子类型")
    private String  postType;

    @ApiModelProperty(value = "评论数目")
    private Integer  comments;

    @ApiModelProperty(value = "点赞量")
    private Integer  praises;

    @ApiModelProperty(value = "浏览量")
    private Integer  views;

    @ApiModelProperty(value = "收藏量")
    private Integer  collections;

    @ApiModelProperty(value = "置顶优先级,数字越小优先级越高")
    private Integer  priority;

    @ApiModelProperty(value = "状态（0：正常 1：审核中 2：审核未通过）")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除（0：正常 1：删除）")
    @TableLogic
    private Integer isDel;
}
