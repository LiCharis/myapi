package com.my.springbootinit.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.my.myapiclientsdk.client.MyApiClient;
import com.my.myapiclientsdk.model.ModelInterfaceInfo;
import com.my.myapicommon.model.InterfaceInfo;
import com.my.myapicommon.model.User;
import com.my.springbootinit.annotation.AuthCheck;
import com.my.springbootinit.common.*;
import com.my.springbootinit.constant.UserConstant;
import com.my.springbootinit.exception.BusinessException;
import com.my.springbootinit.exception.ThrowUtils;

import com.my.springbootinit.mapper.ModelInterfaceInfoMapper;
import com.my.springbootinit.model.dto.intefaceInfo.InterfaceInfoAddRequest;
import com.my.springbootinit.model.dto.intefaceInfo.InterfaceInfoInvokeRequest;
import com.my.springbootinit.model.dto.intefaceInfo.InterfaceInfoQueryRequest;
import com.my.springbootinit.model.dto.intefaceInfo.InterfaceInfoUpdateRequest;


import com.my.springbootinit.model.enums.ClassEnum;
import com.my.springbootinit.service.InterfaceInfoService;
import com.my.springbootinit.service.ModelInterfaceInfoService;
import com.my.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private MyApiClient myApiClient;

    @Resource
    private ModelInterfaceInfoMapper modelInterfaceInfoMapper;

    @Resource
    private ModelInterfaceInfoService modelInterfaceInfoService;

    /**
     * 保存的文件路径
     */
    private static final String targetResultDir = System.getProperty("user.dir") + File.separator + "src/main/java/com/my/springbootinit/uploadFiles";


    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建(仅管理员)
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());

        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除(仅管理员)
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);

        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 接口上线（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        //判断接口是否可用
        com.my.myapiclientsdk.model.User user = new com.my.myapiclientsdk.model.User();
        String jsonStr = JSONUtil.toJsonStr(user);
        user.setUsername("test");
        String content = myApiClient.getUserNameByPost(jsonStr);
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口状态异常");
        }
        //进管理员可更改接口状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(1);

        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 接口下线（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        //进管理员可更改接口状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(0);

        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }


    /**
     * 保存上传的文件
     *
     * @param multipartFile
     * @param targetPath
     * @throws IOException
     * @throws IOException
     */
    public String saveFile(MultipartFile multipartFile, String targetPath) throws IOException {
        // 确保目标路径存在，如果不存在则创建
        File targetDirectory = new File(targetPath);
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }

        // 构建目标文件的完整路径
        String fileName = multipartFile.getOriginalFilename();
        String filePath = targetPath + File.separator + fileName;
        File targetFile = new File(filePath);

        // 将MultipartFile保存到目标文件
        multipartFile.transferTo(targetFile);
        return filePath;
    }


    /**
     * 接口调用
     *
     * @param interfaceInfoInvokeRequest
     * @return
     */
    @PostMapping("/invoke")
    public Object invokeInterfaceInfo(@RequestPart(name = "file", required = false) MultipartFile multipartFile,InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) throws NoSuchFieldException, IOException {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        // 判断是否存在
        Long id = interfaceInfoInvokeRequest.getId();
        String requestBody = interfaceInfoInvokeRequest.getUserRequestBody();

        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);


        String name = oldInterfaceInfo.getName();
        String parameterType = oldInterfaceInfo.getParameterType();
        String method = oldInterfaceInfo.getMethod();
        Integer isUpload = oldInterfaceInfo.getIsUpload();

        //判断接口是否关闭
        if (oldInterfaceInfo.getStatus() == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口已关闭");
        }
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        MyApiClient myApiClientTemp = new MyApiClient(accessKey, secretKey, id);

        /**
         * todo 重构前面做的接口平台
         */
        QueryWrapper<ModelInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", id);
        queryWrapper.eq("img_order", requestBody);
        ModelInterfaceInfo modelInterfaceInfo = modelInterfaceInfoService.getOne(queryWrapper);
        if (modelInterfaceInfo != null) {
            modelInterfaceInfo.setImg_order(requestBody);
            String jsonStr = JSONUtil.toJsonStr(modelInterfaceInfo);
            requestBody = jsonStr;
        }


        /**
         *  这里需要根据传递的参数更加灵活转变
         */
        Object result = null;
        Class targetClass = String.class;
        Method interfaceInfoMethod = null;
        try {
            /**
             * 找到接口对应的方法
             */

            if (isUpload == 1) {
                if (multipartFile.isEmpty()) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "未上传文件");
                }
                interfaceInfoMethod = myApiClientTemp.getClass().getMethod(name, MultipartFile.class, targetClass);
                result = interfaceInfoMethod.invoke(myApiClientTemp, multipartFile, requestBody);
            } else {
                interfaceInfoMethod = myApiClientTemp.getClass().getMethod(name, targetClass);

                result = interfaceInfoMethod.invoke(myApiClientTemp, requestBody);
            }


//
//            Gson gson = new Gson();
//
//            Object data = gson.fromJson(requestBody, targetClass);
//
//            /**
//             * 将Object类型的对象向下转型 targetClass.cast(obj)
//             */
//            result = interfaceInfoMethod.invoke(myApiClientTemp, targetClass.cast(data));


        } catch (Exception e) {
            e.printStackTrace();
        }
//        String userNameByPost = myApiClientTemp.getUserNameByPost(user);
        /**
         * 经网关提示错误返回的格式与成功调用接口返回的格式不一样，
         * 发现是在后端在完成发送对接口调用的请求后，返回的时候多套了一层BaseRonponse。
         * 为了更好的区分网关权限错误类别及统一接口返回结果的格式，
         * 因此将后端返回的格式设置为字符串String，转为BaseResponse格式的工作交由网关和接口，
         * 这样不仅统一格式，前端也好同意去取数据呈现。（1.5h）
         */

        return result;
    }


    @GetMapping("/download")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@RequestParam String filePath) throws IOException {
        // 构建文件路径
        Path path = Paths.get(filePath);
        Path fileName = path.getFileName();

        // 创建Resource对象，用于封装文件
        org.springframework.core.io.Resource resource = new UrlResource(path.toUri());

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        // 返回ResponseEntity，设置响应头和状态码
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                       HttpServletRequest request) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }


}