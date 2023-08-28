package com.erp.floor.headler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.erp.common.core.domain.model.LoginUser;
import com.erp.common.utils.LogUtils;
import com.erp.common.utils.SecurityUtils;
import com.erp.common.utils.ServletUtils;
import com.erp.common.utils.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author hanfei
 * @version 1.0
 * @Description
 * @date 2022/7/21 16:52
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static LoginUser loginUser;

    public static LoginUser getLoginUser() {
        return loginUser;
    }

    public static void setLoginUser(LoginUser loginUser) {
        MyMetaObjectHandler.loginUser = loginUser;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdAt", new Date(), metaObject);
        this.setFieldValByName("updatedAt", new Date(), metaObject);
        if(loginUser != null && StringUtils.isNotEmpty(loginUser.getUsername())){
            this.setFieldValByName("createdPerson", loginUser.getUser().getNickName(), metaObject);
            this.setFieldValByName("updatedPerson", loginUser.getUser().getNickName(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updatedAt", new Date(), metaObject);
        if(loginUser != null && StringUtils.isNotEmpty(loginUser.getUsername())){
            this.setFieldValByName("updatedPerson", loginUser.getUser().getNickName(), metaObject);
        }
    }
}
