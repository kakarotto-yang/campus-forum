package yuren.work.boot.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户登录信息实体类")
@Data
public class UserLogin {
    @ApiModelProperty(value = "用户登陆表id")
    private Integer id;

    @ApiModelProperty(value = "用户学号")
    private String sno;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "用户密码")
    private String password;
}
