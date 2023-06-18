package com.jason.gen;

import com.jason.gen.service.AbstractGenContext;
import com.jason.gen.service.DefaultGenContext;

/**
 * 代码生成器入口
 *
 * @author guozhongcheng
 * @since 2023/6/13
 **/
public class GenRun {

    public static void main(String[] args) throws Exception {
        AbstractGenContext abstractGenContext = new DefaultGenContext();
        abstractGenContext.run();
    }
}
