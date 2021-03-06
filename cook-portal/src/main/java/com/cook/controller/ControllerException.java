package com.cook.controller;

import com.cook.exception.ServiceException;
import com.cook.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @description: 全局处理 Controller 层异常
 * @author: ziHeng
 * @create: 2018-05-09 10:57
 **/
//@ControllerAdvice
public class ControllerException {

    private static Logger logger= LoggerFactory.getLogger(ControllerException.class);

    
    /** 
      * @Description: 处理所有不可预知的异常
      * @Author: ziHeng
      * @Date: 2018/5/9 上午11:36
      * @return: com.cook.response.ApiResponse
      */ 
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse handleException(Exception e) throws Exception {

        System.out.println("捕捉到异常");

        logger.error(e.getMessage(),e);

        throw e;

        //return ApiResponse.ofError(ApiResponse.Status.INTERNAL_SERVER_ERROR);
    }

    /** 
      * @Description: 处理所有业务异常
      * @Author: ziHeng
      * @Date: 2018/5/9 上午11:51
      * @return: com.cook.response.ApiResponse
      */ 
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ApiResponse handleBusinessException(ServiceException e){
        logger.error(e.getMessage(), e);

        //return ApiResponse.ofMessage();
        return null;
    }


}
