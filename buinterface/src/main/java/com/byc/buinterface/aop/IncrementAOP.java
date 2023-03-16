package com.byc.buinterface.aop;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 调用次数切面
 */
@RestControllerAdvice
public class IncrementAOP {

    //伪代码
    // 定义切面触发的时机 （什么时候执行方法）controller 接口方法执行后，执行下述方法
    public void doInvokeCount() {
        // 调用方法
        // 调用成功后， 次数 + 1
    }
}
