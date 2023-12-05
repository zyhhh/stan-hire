package cn.stan.controller;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.result.GraceResult;
import cn.stan.common.utils.GsonUtils;
import cn.stan.common.utils.JWTUtils;
import cn.stan.pojo.Users;
import cn.stan.pojo.bo.ModifyUserBO;
import cn.stan.pojo.vo.UsersVO;
import cn.stan.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("userinfo")
public class UserInfoController extends BaseInfoProperties {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JWTUtils jwtUtils;

    /**
     * 修改用户信息
     *
     * @param modifyUserBO
     * @return
     */
    @PostMapping("modify")
    public GraceResult modify(@Valid @RequestBody ModifyUserBO modifyUserBO) {
        // 修改用户信息
        usersService.modifyUserInfo(modifyUserBO);
        // 返回最新用户信息，包含token
        UsersVO userInfo = getUserInfo(modifyUserBO.getUserId());

        return GraceResult.ok(userInfo);
    }

    private UsersVO getUserInfo(String userId) {
        Users latestUser = usersService.getById(userId);

        String token = jwtUtils.createToken(GsonUtils.objectToString(latestUser), TOKEN_USER_PREFIX);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(latestUser, usersVO);
        usersVO.setUserToken(token);

        return usersVO;
    }

}
