package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecutor;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.SecKill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill") // url: /模块/资源/{id}/细分
public class SeckillController {
    @Autowired
    SeckillService seckillService;
    // 日志！
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<SecKill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list"; // /WEB-INF/jsp/list.jsp
    }

    @RequestMapping(value = "{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        SecKill secKill = seckillService.getById(seckillId);
        if (secKill == null) {
            return "forward:/seckill/list";
        } else {
            model.addAttribute("seckill", secKill);
        }
        return "detail";
    }

    //ajax json
    //只接受post，也就是说直接在浏览器敲入这个地址是无效的？？？
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody //spring 看到这个注解后，会试图把我们返回的数据类型封装成json
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecutor> execute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  //将手机号为空放在代码逻辑中处理，而不是直接返回false
                                                  @CookieValue(value = "killPhone", required = false) Long phone) {
        SeckillResult<SeckillExecutor> result;
        if (phone == null) {
            return new SeckillResult<>(false, "未注册");
        }
        try {
            SeckillExecutor executor = seckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<>(true, executor);
        } catch (RepeatKillException e) {
            SeckillExecutor seckillExecutor = new SeckillExecutor(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<>(true, seckillExecutor);
        } catch (SeckillCloseException e) {
            SeckillExecutor seckillExecutor = new SeckillExecutor(seckillId, SeckillStateEnum.END);
            return new SeckillResult<>(true, seckillExecutor);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            SeckillExecutor seckillExecutor = new SeckillExecutor(seckillId, SeckillStateEnum.iNNER_ERROR);
            return new SeckillResult<>(true, seckillExecutor);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }
}
