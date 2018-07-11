package me.leiho.elasticsearch.handler;

/**
 * @author lengleng
 * @date 2018/5/24
 */

import me.leiho.elasticsearch.util.R;
import lombok.extern.slf4j.Slf4j;
import me.leiho.elasticsearch.util.R;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局的的异常拦截器
 *
 * @author lengleng
 * @date 2018/05/22
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常.
     *
     * @param e the e
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public R exception(Exception e) {
        //throw new exception 不会通过 ResponseBodyAdvice
        log.info("全局异常：{}", e.getMessage(), e);
        return new R<>(e);
    }
}
