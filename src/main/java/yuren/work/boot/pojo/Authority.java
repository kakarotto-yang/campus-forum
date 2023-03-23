package yuren.work.boot.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户权限信息实体类")
@Data
public class Authority {
    @ApiModelProperty(value = "权限id")
    private Integer id;

    @ApiModelProperty(value = "权限uid")
    private Integer uid;

    @ApiModelProperty(value = "是否为管理员,1表示是,0表示不是")
    private Integer isAdmin;
}
