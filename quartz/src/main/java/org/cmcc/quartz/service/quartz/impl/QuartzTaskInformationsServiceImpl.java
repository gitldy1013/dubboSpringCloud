package org.cmcc.quartz.service.quartz.impl;


import org.apache.dubbo.config.annotation.Service;
import org.cmcc.service.QuartzTaskInformationsService;
import org.cmcc.service.common.uitl.ResultEnum;
import org.cmcc.service.common.uitl.ResultUtil;
import org.cmcc.service.bean.QuartzTaskInformations;
import org.cmcc.quartz.dao.QuartzTaskInformationsMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName QuartzTaskInformationsServiceImpl
 * @Description TODO
 * @Author cmcc
 * @Date 2019/1/3
 * Version  1.0
 */
@Service(protocol = {"dubbo"})
public class QuartzTaskInformationsServiceImpl implements QuartzTaskInformationsService {

    @Autowired
    private QuartzTaskInformationsMapper quartzTaskInformationsMapper;

    @Override
    public String insert(QuartzTaskInformations quartzTaskInformations) {
        String taskNo = quartzTaskInformations.getTaskno();
        quartzTaskInformations.setVersion(0);
        quartzTaskInformations.setCreatetime(System.currentTimeMillis());
        quartzTaskInformations.setLastmodifytime(System.currentTimeMillis());
        Integer count = quartzTaskInformationsMapper.selectByTaskNo(taskNo);
        //判断是否重复任务编号
        if (count > 0) {
            return ResultUtil.success(ResultEnum.TASKNO_EXIST.getCode(), ResultEnum.TASKNO_EXIST.getMessage());
        }
        int insert = quartzTaskInformationsMapper.insert(quartzTaskInformations);
        if (insert < 1) {
            return ResultUtil.success(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMessage());
        }
        return ResultUtil.success(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
    }

    @Override
    public String remove(QuartzTaskInformations quartzTaskInformations) {
        Integer count = quartzTaskInformationsMapper.selectByTaskNo(quartzTaskInformations.getTaskno());
        //判断是否重复任务编号
        if (count == 0) {
            return ResultUtil.success(ResultEnum.TASKNO_NOT_EXIST.getCode(), ResultEnum.TASKNO_NOT_EXIST.getMessage());
        }
        QuartzTaskInformations taskByTaskNo = quartzTaskInformationsMapper.getTaskByTaskNo(quartzTaskInformations.getTaskno());
        int insert = quartzTaskInformationsMapper.deleteByPrimaryKey(taskByTaskNo.getId());
        if (insert < 1) {
            return ResultUtil.success(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMessage());
        }
        return ResultUtil.success(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
    }

    @Override
    public List<QuartzTaskInformations> selectList(String taskNo, String currentPage) {
        Map<String, Object> map = new HashMap<>();
        Integer start = Integer.parseInt(currentPage);
        map.put("taskNo", taskNo);
        map.put("startIndex", 10 * (start - 1));
        return quartzTaskInformationsMapper.selectList(map);
    }

    @Override
    public QuartzTaskInformations getTaskById(String id) {
        return quartzTaskInformationsMapper.selectByPrimaryKey(Long.parseLong(id));
    }

    @Override
    public String updateTask(QuartzTaskInformations quartzTaskInformations) {
        Integer count = quartzTaskInformationsMapper.selectByTaskNo(quartzTaskInformations.getTaskno());
        //判断是否重复任务编号
        if (count >= 2) {
            return ResultUtil.success(ResultEnum.TASKNO_EXIST.getCode(), ResultEnum.TASKNO_EXIST.getMessage());
        }
        //设置解冻时间或冻结时间及最后修改时间
        if (ResultEnum.FROZEN.name().equals(quartzTaskInformations.getFrozenstatus())) {
            quartzTaskInformations.setFrozentime(System.currentTimeMillis());
        } else if (ResultEnum.UNFROZEN.name().equals(quartzTaskInformations.getFrozenstatus())) {
            quartzTaskInformations.setUnfrozentime(System.currentTimeMillis());
        }
        quartzTaskInformations.setLastmodifytime(System.currentTimeMillis());
        int updateCount = quartzTaskInformationsMapper.updateByPrimaryKeySelective(quartzTaskInformations);
        //乐观锁控制并发修改
        if (updateCount < 1) {
            return ResultUtil.success(ResultEnum.UPDATE_FAIL.getCode(), ResultEnum.UPDATE_FAIL.getMessage());
        }
        return ResultUtil.success(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
    }

    @Override
    public QuartzTaskInformations getTaskByTaskNo(String taskNo) {
        return quartzTaskInformationsMapper.getTaskByTaskNo(taskNo);
    }

    @Override
    public Integer updateStatusById(QuartzTaskInformations quartzTaskInformations) {
        return quartzTaskInformationsMapper.updateByPrimaryKeySelective(quartzTaskInformations);
    }

    @Override
    public List<QuartzTaskInformations> getUnnfrozenTasks(String status) {
        return quartzTaskInformationsMapper.getUnfrozenTasks(status);
    }

    @Override
    public Integer updateModifyTimeById(QuartzTaskInformations quartzTaskInformations) {
        return quartzTaskInformationsMapper.updateByPrimaryKeySelective(quartzTaskInformations);
    }

}
