package com.wtk.xiaicodemother.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.wtk.xiaicodemother.model.dto.app.AppQueryRequest;
import com.wtk.xiaicodemother.model.entity.App;
import com.wtk.xiaicodemother.model.entity.User;
import com.wtk.xiaicodemother.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author xiyan
 */
public interface AppService extends IService<App> {

    /**
     * 通过对话生成应用代码
     * @param appId 应用 ID
     * @param message 用户消息
     * @param loginUser 登录用户
     * @return
     */
    public Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 应用部署
     * @param appId 应用 ID
     * @param loginUser 登录用户
     * @return 可访问的部署地址
     */
    String deployApp(Long appId, User loginUser);

    /**
     * 获取 APP 封装类
     * @param app
     * @return
     */
    public AppVO getAppVO(App app);

    /**
     * 构造应用查询条件
     * @param appQueryRequest app查询请求
     * @return 查询条件
     */
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 批量获取App封装类列表
     * @param appList app列表
     * @return app封装类列表
     */
    public List<AppVO> getAppVOList(List<App> appList);

}
