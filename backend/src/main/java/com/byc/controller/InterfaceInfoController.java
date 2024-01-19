package com.byc.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byc.annotation.AuthCheck;
import com.byc.common.*;
import com.byc.common.model.BaseResponse;
import com.byc.common.model.ResultUtils;
import com.byc.common.model.entity.InterfaceInfo;
import com.byc.common.model.entity.User;
import com.byc.common.model.entity.UserInterfaceInfo;
import com.byc.constant.CommonConstant;
import com.byc.common.exception.BusinessException;
import com.byc.common.model.ErrorCode;
import com.byc.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.byc.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.byc.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.byc.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;

import com.byc.model.entity.HeaderEntry;
import com.byc.model.entity.ParamEntry;
import com.byc.model.enums.InterfaceInfoStatusEnum;
import com.byc.model.enums.RequestMethodEnum;
import com.byc.service.InterfaceInfoService;
import com.byc.service.UserService;
import com.byc.utils.S3BucketUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.lang.Nullable;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.byc.common.utils.SignUtils.getSign;


/**
 * 帖子接口
 *
 * 
 */
@RestController
@RequestMapping("/interfaceinfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceinfoService;

    @Resource
    private UserService userService;

    @Value("${gateway.host}")
    private String gatewayHost;

    @Value("${gateway.port}")
    private String gatewayPort;

    @Resource
    private S3BucketUtils s3BucketUtils;


    private static final String GATEWAY_HOST = "http://localhost:8002";

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceinfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestPart("image") Optional<MultipartFile> image,
                                               InterfaceInfoAddRequest interfaceinfoAddRequest,
                                               HttpServletRequest request) throws IOException {
        if (interfaceinfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoAddRequest, interfaceinfo);
        // 校验
        interfaceinfoService.validInterfaceInfo(interfaceinfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceinfo.setUserId(loginUser.getId());
        boolean result = interfaceinfoService.save(interfaceinfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceinfo.getId();
        // upload image to s3
        if (image.isPresent()) {
            MultipartFile multipartFile = image.get();
            File file = File.createTempFile(multipartFile.getName(), null);
            multipartFile.transferTo(file);
            String eTag = s3BucketUtils.uploadFileToS3("interface" + newInterfaceInfoId, file);
            interfaceinfo.setImage(eTag);
            boolean r = interfaceinfoService.updateById(interfaceinfo);
            if (!r) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        }

        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceinfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceinfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestPart("image") Optional<MultipartFile> image,
                                                     InterfaceInfoUpdateRequest interfaceinfoUpdateRequest,
                                                        HttpServletRequest request) throws IOException {
        if (interfaceinfoUpdateRequest == null || interfaceinfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoUpdateRequest, interfaceinfo);
        // 参数校验
        interfaceinfoService.validInterfaceInfo(interfaceinfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceinfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // upload image to s3
        if (image.isPresent()) {
            MultipartFile multipartFile = image.get();
            File file = File.createTempFile(multipartFile.getName(), null);
            multipartFile.transferTo(file);
            String eTag = s3BucketUtils.uploadFileToS3("interface" + id, file);
            interfaceinfo.setImage(eTag);
        }
//        else {
//            UpdateWrapper<InterfaceInfo> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("id", interfaceinfo.getId()).set("image", (Object) null);
//            interfaceinfoService.updateInterface(updateWrapper);
//        }
        boolean result = interfaceinfoService.updateById(interfaceinfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = interfaceinfoService.getById(id);
        return ResultUtils.success(interfaceinfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceinfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceinfoQueryRequest, HttpServletRequest request) {
        InterfaceInfo interfaceinfoQuery = new InterfaceInfo();
        User loginUser = userService.getLoginUser(request);
        if (interfaceinfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceinfoQueryRequest, interfaceinfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceinfoQuery);
        List<InterfaceInfo> interfaceinfoList = interfaceinfoService.list(queryWrapper);
        return ResultUtils.success(interfaceinfoList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceinfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceinfoQueryRequest, HttpServletRequest request) {
        if (interfaceinfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoQueryRequest, interfaceinfoQuery);
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        String sortField = interfaceinfoQueryRequest.getSortField();
        String sortOrder = interfaceinfoQueryRequest.getSortOrder();
        String description = interfaceinfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceinfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceinfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceinfoPage = interfaceinfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceinfoPage);
    }

    // endregion

    /**
     * 发布
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/open")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> openInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {

        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断是否可以调用
//        com.byc.clientsdk.model.User user = new com.byc.clientsdk.model.User();
//        user.setUsername("test");
//        String username;
//        try {
//            username = buClient.getUsernameByPost(user);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//        if (StringUtils.isBlank(username)){
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
//        }
        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OPEN.getValue());
        boolean result = interfaceinfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 下线
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/close")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> closeInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.CLOSED .getValue());
        boolean result = interfaceinfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 调用
     *
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                    HttpServletRequest request) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        InterfaceInfo interfaceInfo = interfaceinfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (interfaceInfo.getStatus() == InterfaceInfoStatusEnum.CLOSED.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }

        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        // 更新接口总调用次数
        UpdateWrapper<InterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", interfaceInfo.getId());
        updateWrapper.setSql("invokeNum = invokeNum + 1");
        boolean update = interfaceinfoService.update(updateWrapper);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新总调用次数失败");
        }
        String method = interfaceInfo.getMethod();
        List<ParamEntry> paramEntries = JSONUtil.toList(userRequestParams, ParamEntry.class);
        Map<String, Object> paramMap = new HashMap<>();
        for (ParamEntry paramEntry : paramEntries) {
            paramMap.put(paramEntry.getName(), paramEntry.getValue());
        }
        // 使用hutool工具类直接调用
        String result;
        String json = JSONUtil.toJsonStr(paramMap);
        String url = String.format("http://%s:%s%s", gatewayHost, gatewayPort, interfaceInfo.getUrl());
        System.out.println(url);
        if (RequestMethodEnum.POST.getValue().equals(method)) {
            HttpResponse response = HttpRequest.post(url)
//                    .charset(StandardCharsets.UTF_8)
                    .addHeaders(getHeaders(accessKey, secretKey, interfaceInfo.getUrl(), interfaceInfo.getRequestHeader()) )
                    .body(json)
                    .execute();
            result = response.body();
            System.out.println(response);
        } else if (RequestMethodEnum.GET.getValue().equals(method)) {
            HttpResponse response = HttpRequest.get(url)
                    .addHeaders(getHeaders(accessKey, secretKey, interfaceInfo.getUrl(), interfaceInfo.getRequestHeader()) )
//                    .charset(StandardCharsets.UTF_8)
                    .form(paramMap)
                    .execute();
            result = response.body();
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "request method："+ method + " 不存在");
        }



        // 根据不同的url 调用不同的方法  - 使用sdk调用
//        BuClient tempClient = new BuClient(accessKey, secretKey);
//        String methodName = oldInterfaceInfo.getName();
//        Object result = null;
//        try {
////            Method method = tempClient.getClass().getMethod(methodName);
//            for (Method method : tempClient.getClass().getMethods()) {
//                if (method.getName().equals(methodName)){
//                    Class<?>[] parameterTypes = method.getParameterTypes();
//                    Object[] args = new Object[parameterTypes.length];
//                    for (int i = 0; i < parameterTypes.length; i++) {
//                        args[i] = gson.fromJson(userRequestParams, parameterTypes[0]);
//                    }
////                    Object args = gson.fromJson(userRequestParams, parameterTypes[0]);
//                    result = method.invoke(tempClient, args);
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return ResultUtils.success(result);

//        com.byc.clientsdk.model.User user = gson.fromJson(userRequestParams, com.byc.clientsdk.model.User.class);
//        BuClient tempClient = new BuClient(accessKey, secretKey);
//        String usernameByPost = tempClient.getUsernameByPost(user);
//        String usernameByPost = buClient.getUsernameByPost(user);
//        return ResultUtils.success(usernameByPost);
    }

    private Map<String, String> getHeaders(String accessKey, String secretKey, String url, String requestHeader) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(requestHeader)) {
            List<HeaderEntry> headerEntries = JSONUtil.toList(requestHeader, HeaderEntry.class);
            for (HeaderEntry headerEntry: headerEntries) {
                map.put(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        map.put("accessKey", accessKey);
        // 一定不能发送给后端
//        map.put("secretKey", secretKey);
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("url", url);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", getSign(url, secretKey));
        return map;
    }
}
