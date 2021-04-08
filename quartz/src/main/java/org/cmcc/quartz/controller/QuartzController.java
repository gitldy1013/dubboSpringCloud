package org.cmcc.quartz.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.cmcc.service.QuartzService;
import org.cmcc.service.bean.QuartzTaskErrors;
import org.cmcc.service.bean.QuartzTaskInformations;
import org.cmcc.service.common.uitl.ResultEnum;
import org.cmcc.service.common.uitl.ResultUtil;
import org.cmcc.service.dto.QuartzTaskRecordsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassName QuartzController
 * @Description quartz controller主要逻辑
 * @Author cmcc
 * @Date 2019/1/3
 * Version  1.0
 */
@Controller
@RequestMapping("/quartz")
@Slf4j
public class QuartzController {

    @Autowired
    private QuartzService quartzService;

    @RequestMapping(value = "/add/taskpage", method = RequestMethod.GET)
    public String addTaskpage() {
        return "addtask";
    }

    @ResponseBody
    @RequestMapping(value = "/add/task", method = RequestMethod.POST)
    public String addTask(QuartzTaskInformations taskInformations) {
        try {
            return quartzService.addTask(taskInformations);
        } catch (Exception e) {
            log.error("/add/task exception={}", e.getMessage());
            return ResultUtil.fail();
        }
    }

    @RequestMapping(value = "/edit/taskpage", method = RequestMethod.GET)
    public String editTaskpage(Model model, String id) {
        QuartzTaskInformations taskInformation = quartzService.getTaskById(id);
        model.addAttribute("taskInformation", taskInformation);
        return "updatetask";
    }

    @ResponseBody
    @RequestMapping(value = "/edit/task", method = RequestMethod.POST)
    public String editTask(QuartzTaskInformations taskInformations) {
        try {
            return quartzService.updateTask(taskInformations);
        } catch (Exception e) {
            log.error("/edit/task exception={}", e.getMessage());
            return ResultUtil.fail();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/del/task", method = RequestMethod.POST)
    public String delTask(QuartzTaskInformations taskInformations) {
        try {
            return quartzService.delTask(taskInformations);
        } catch (Exception e) {
            log.error("/del/task exception={}", e.getMessage());
            return ResultUtil.fail();
        }
    }

    /**
     * 启动 或者 暂定定时任务
     *
     * @param taskNo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list/optionjob", method = RequestMethod.GET)
    public String optionJob(String taskNo) {
        if (StringUtils.isEmpty(taskNo)) {
            return ResultUtil.success(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getMessage());
        }
        return quartzService.startOrStopJob(taskNo);

    }

    /**
     * 定时任务执行情况
     *
     * @param taskNo
     * @param model
     * @return
     */
    @RequestMapping(value = "/taskrecords", method = RequestMethod.GET)
    public String taskRecordsPage(@RequestParam(value = "taskno", required = false) String taskNo, Model model) {
        log.info("");
        try {
            if (StringUtils.isEmpty(taskNo)) {
                return "redirect:/";
            }
            List<QuartzTaskRecordsVo> quartzTaskRecords = quartzService.taskRecords(taskNo);
            model.addAttribute("quartzTaskRecords", quartzTaskRecords);
        } catch (Exception e) {
            log.error("");
            return "redirect:/";
        }
        return "/taskrecords";
    }

    /**
     * 立即运行一次定时任务
     *
     * @param taskNo
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/runtask/rightnow", method = RequestMethod.GET)
    public String runTaskRightNow(@RequestParam(value = "taskno", required = false) String taskNo, Model model) {
        log.info("");
        try {
            if (StringUtils.isEmpty(taskNo)) {
                return ResultUtil.success(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getMessage());
            }
            return quartzService.runTaskRightNow(taskNo);
        } catch (Exception e) {
            log.error("");
            return ResultUtil.success(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMessage());
        }
    }

    /**
     * 定时任务失败详情
     *
     * @param recordId
     * @param model
     * @return
     */
    @RequestMapping(value = "/task/errors", method = RequestMethod.GET)
    public String detailTaskErrors(@RequestParam(value = "recordid", required = false) String recordId, Model model) {
        log.info("");
        try {
            if (StringUtils.isEmpty(recordId)) {
                return ResultUtil.success(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getMessage());
            }
            QuartzTaskErrors taskErrors = quartzService.detailTaskErrors(recordId);
            model.addAttribute("taskErrors", taskErrors);
            return "taskerrors";
        } catch (Exception e) {
            log.error("");
            return "redirect:/";
        }
    }


}
