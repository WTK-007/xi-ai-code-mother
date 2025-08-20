package com.wtk.xiaicodemother.controller;

import com.mybatisflex.core.paginate.Page;
import com.wtk.xiaicodemother.annotation.AuthCheck;
import com.wtk.xiaicodemother.common.BaseResponse;
import com.wtk.xiaicodemother.common.ResultUtils;
import com.wtk.xiaicodemother.constant.UserConstant;
import com.wtk.xiaicodemother.exception.BusinessException;
import com.wtk.xiaicodemother.exception.ErrorCode;
import com.wtk.xiaicodemother.exception.ThrowUtils;
import com.wtk.xiaicodemother.model.dto.user.*;
import com.wtk.xiaicodemother.model.entity.User;
import com.wtk.xiaicodemother.model.vo.LoginUserVO;
import com.wtk.xiaicodemother.model.vo.UserVO;
import com.wtk.xiaicodemother.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 控制层。
 *
 * @author xiyan
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    /**
     * 用户注册
     * @param userRegisterRequest 用户注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest 用户登录请求
     * @param request
     * @return 脱敏后的用户数据
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 创建用户
     * @param userAddRequest 创建用户请求
     * @return 新用户id
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
       final String DEFAULT_PASSWORD = "12345678";
       String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
       user.setUserPassword(encryptPassword);
       boolean result = userService.save(user);
       ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
       return ResultUtils.success(user.getId());
   }

    /**
     * 根据id获取用户
     * @param id 用户id
     * @return 用户信息
     */
   @GetMapping("/get")
   @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
   public BaseResponse<User> getUserById(Long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
   }

   /**
     * 根据id获取用户信息
     * @param id 用户id
     * @return 用户信息
     */
   @GetMapping("/get/vo")
   public BaseResponse<UserVO> getUserVOById(Long id) {
       BaseResponse<User> response = getUserById(id);
       User user = response.getData();
       return ResultUtils.success(userService.getUserVO(user));
   }


    /**
     * 删除用户
     * @param userDeleteRequest 删除用户请求
     * @return 是否成功
     */
   @PostMapping("/delete")
   @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
   public BaseResponse<Boolean> deleteUser (@RequestBody UserDeleteRequest userDeleteRequest) {
       if (userDeleteRequest == null || userDeleteRequest.getId() < 0) {
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
       boolean b = userService.removeById(userDeleteRequest.getId());
       return ResultUtils.success(b);
   }

    /**
     * 更新用户
     * @param userUpdateRequest 更新用户请求
     * @return 是否成功
     */
   @PostMapping("/update")
   @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
   public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
       // 检查参数是否为空或ID是否小于0
       if (userUpdateRequest == null || userUpdateRequest.getId() < 0) {
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
       // 创建User对象
       User user = new User();
       // 将请求中的属性复制到User对象中
       BeanUtils.copyProperties(userUpdateRequest, user);
       // 更新用户
       boolean result = userService.updateById(user);
       // 如果更新失败，抛出异常
       ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
       // 返回成功结果
       return ResultUtils.success(true);
   }

    /**
     * 分页获取用户封装列表(仅管理员)
     * @param userQueryRequest 查询请求参数
     * @return
     */
   @PostMapping("/list/page/vo")
   @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
   public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
       ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
       long pageNum = userQueryRequest.getPageNum();
       long pageSize = userQueryRequest.getPageSize();
       Page<User> userPage = userService.page(Page.of(pageNum, pageSize), userService.getQueryWrapper(userQueryRequest));
       // 数据脱敏
       Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalRow());
       List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
       userVOPage.setRecords(userVOList);
       return ResultUtils.success(userVOPage);
   }


}
