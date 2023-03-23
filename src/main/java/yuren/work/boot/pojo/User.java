package yuren.work.boot.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "用户详细信息实体类")
@Data
public class User {
    @ApiModelProperty(value = "用户详细信息id")
    private Integer id;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户年龄")
    private Integer age;

    @ApiModelProperty(value = "用户性别")
    private String sex;

    @ApiModelProperty(value = "用户学校")
    private String school;

    @ApiModelProperty(value = "班级")
    private String classes;

    @ApiModelProperty(value = "用户头像地址")
    private String headImg;

    @ApiModelProperty(value = "用户文章数目")
    private Integer posts;

    @ApiModelProperty(value = "用户粉丝人数")
    private Integer fans;

    @ApiModelProperty(value = "用户关注人数")
    private Integer follows;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "状态（0：正常 1：锁定）")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除（0：正常 1：删除）")
    @TableLogic
    private Integer isDel;
}
