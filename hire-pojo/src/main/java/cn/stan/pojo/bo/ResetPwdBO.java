package cn.stan.pojo.bo;

import cn.stan.common.exception.GraceException;
import cn.stan.common.result.ResponseStatusEnum;
import cn.stan.common.utils.MD5Utils;
import cn.stan.pojo.ar.AdminAR;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResetPwdBO {

    private String adminId;
    private String password;
    private String rePassword;

    public void modifyPwd() {
        // 校验
        validate();

        AdminAR adminAR = new AdminAR();
        adminAR.setId(adminId);

        // 重置密码
        // 生成随机数字或者英文字母结合的盐
        String slat = (int) ((Math.random() * 9 + 1) * 100000) + "";
        String pwd = MD5Utils.encrypt(password, slat);
        adminAR.setPassword(pwd);
        adminAR.setSlat(slat);

        adminAR.setUpdatedTime(LocalDateTime.now());

        adminAR.updateById();
    }

    private void validate() {
        checkPwd();
        checkAdminId();
    }

    private void checkAdminId() {
        if (StringUtils.isBlank(adminId)) GraceException.display(ResponseStatusEnum.ADMIN_NOT_EXIST);

        AdminAR adminAR = new AdminAR();
        adminAR.setId(adminId);

        adminAR = adminAR.selectById();
        if (adminAR == null) GraceException.display(ResponseStatusEnum.ADMIN_NOT_EXIST);
    }

    private void checkPwd() {
        if (StringUtils.isBlank(password)) GraceException.display(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
        if (StringUtils.isBlank(rePassword)) GraceException.display(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
        if (!password.equalsIgnoreCase(rePassword)) GraceException.display(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
    }

}
