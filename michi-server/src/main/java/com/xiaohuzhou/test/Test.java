package com.xiaohuzhou.test;

import com.xiaohuzhou.base.annotations.MichiMapping;
import com.xiaohuzhou.base.annotations.MichiRoute;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/27
 * @Description:
 */
@MichiRoute
//@MichiMapping("/test")
public class Test {

    @MichiMapping("/test")
    public String test(){
        return "success";
    }
}
