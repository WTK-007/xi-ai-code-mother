package com.wtk.xiaicodemother.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.wtk.xiaicodemother.exception.BusinessException;
import com.wtk.xiaicodemother.exception.ErrorCode;
import com.wtk.xiaicodemother.model.dto.app.AppQueryRequest;
import com.wtk.xiaicodemother.model.entity.App;
import com.wtk.xiaicodemother.mapper.AppMapper;
import com.wtk.xiaicodemother.model.entity.User;
import com.wtk.xiaicodemother.model.vo.AppVO;
import com.wtk.xiaicodemother.model.vo.UserVO;
import com.wtk.xiaicodemother.service.AppService;
import com.wtk.xiaicodemother.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author xiyan
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

    @Resource
    private UserService userService;

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtils.copyProperties(app, appVO);
        Long userId = app.getUserId();
        // 关联查询用户信息
        if (userId != null) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            appVO.setUser(userVO);
        }
        return appVO;
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .like("appName", appName)
                .like("cover", cover)
                .like("initPrompt", initPrompt)
                .eq("codeGenType", codeGenType)
                .eq("deployKey", deployKey)
                .eq("priority", priority)
                .eq("userId", userId)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollectionUtils.isEmpty(appList)) {
            return new ArrayList<>();
        }
        Set<Long> userIdList = appList.stream()
                .map(App::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.listByIds(userIdList).stream()
                .collect(Collectors.toMap(User::getId, userService::getUserVO));
       return appList.stream()
                .map(app -> {
                    AppVO appVO = getAppVO(app);
                    UserVO userVO = userVOMap.get(app.getUserId());
                    appVO.setUser(userVO);
                    return appVO;
                }).collect(Collectors.toList());
    }
}
