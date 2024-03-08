package com.my.myapiclientsdk.client;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.my.myapiclientsdk.model.GenChartByAiRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.my.myapiclientsdk.signUtils.SignUtils.getSign;


public class MyApiClient {

    private String accessKey;
    private String secretKey;
    private Long interfaceInfoId;

    //网关地址
    private static final String GATEWAY_HOST = "http://localhost:8090/api";

    public MyApiClient(String accessKey, String secretKey, Long interfaceInfoId) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.interfaceInfoId = interfaceInfoId;
    }


    public String generateByQASystem(String input){
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/qasystem/getRes")
                .form("input",input)
                .addHeaders(getHeaders(input))
                .execute();
        return httpResponse.body();
    }


    /**
     * 传递想问的问题，Qwen回答问题
     * @param input
     * @return
     */
    public String chatByQwenAPI(String input){
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/chat/chatByQwenAPI")
                .form("input",input)
                .addHeaders(getHeaders(input))
                .execute();
        return httpResponse.body();
    }



    /**
     * 调用api接口生成图表
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @return
     */
    public String generateByAi(MultipartFile multipartFile,
                               String genChartByAiRequest) throws IOException {

        /**
         * 将multipartFile对象转为文件因为后端接口接收不到multipart对象
         */
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/temp." + suffix);
        multipartFile.transferTo(file);


        Gson gson = new Gson();
        GenChartByAiRequest chartByAiRequest = gson.fromJson(genChartByAiRequest, GenChartByAiRequest.class);
        String name = chartByAiRequest.getName();
        String goal = chartByAiRequest.getGoal();
        String chartType = chartByAiRequest.getChartType();
        String strategy = chartByAiRequest.getStrategy();

        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtil.createPost(GATEWAY_HOST + "/chart/generateByAPI")
                    .addHeaders(getHeaders(genChartByAiRequest))
                    .header("Content-Type", "multipart/form-data")
                    .form("file", file)
                    .form("name", name)
                    .form("goal", goal)
                    .form("chartType", chartType)
                    .form("strategy",strategy)
                    .execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResponse.body();
    }

    /**
     * api1模型接口调用测试
     *
     * @param modelInterfaceInfo
     * @return
     */
    public String api1(MultipartFile multipartFile,String modelInterfaceInfo) {

        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/task/model")
                .body(modelInterfaceInfo)
                .addHeaders(getHeaders(modelInterfaceInfo))
                .execute();
        System.out.println(httpResponse.getStatus());
        System.out.println(httpResponse.body());
        return httpResponse.body();

    }


    /**
     * api2模型接口调用测试
     *
     * @param modelInterfaceInfo
     * @return
     */
    public String api2(MultipartFile multipartFile,String modelInterfaceInfo) {

        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/task/model")
                .body(modelInterfaceInfo)
                .addHeaders(getHeaders(modelInterfaceInfo))
                .execute();
        System.out.println(httpResponse.getStatus());
        System.out.println(httpResponse.body());
        return httpResponse.body();

    }

    /**
     * api3模型接口调用测试
     *
     * @param modelInterfaceInfo
     * @return
     */
    public String api3(MultipartFile multipartFile,String modelInterfaceInfo) {

        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/task/model")
                .body(modelInterfaceInfo)
                .addHeaders(getHeaders(modelInterfaceInfo))
                .execute();
        System.out.println(httpResponse.getStatus());
        System.out.println(httpResponse.body());
        return httpResponse.body();

    }

    /**
     * api4模型接口调用测试
     *
     * @param modelInterfaceInfo
     * @return
     */
    public String api4(MultipartFile multipartFile,String modelInterfaceInfo) {

        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/task/model")
                .body(modelInterfaceInfo)
                .addHeaders(getHeaders(modelInterfaceInfo))
                .execute();
        System.out.println(httpResponse.getStatus());
        System.out.println(httpResponse.body());
        return httpResponse.body();

    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做url编码，拼接在url中
        HttpResponse result = HttpUtil.createGet(GATEWAY_HOST + "/name/get")
                .body("name", name)
                .form("name", name)
                .addHeaders(getHeaders(name))
                .execute();

        System.out.println(result.body());
        return result.body();

    }

    public String getNameByPost(String name) {

        HttpResponse result = HttpUtil.createPost(GATEWAY_HOST + "/name/post")
                .form("name", name)
                .addHeaders(getHeaders(name))
                .execute();


        System.out.println(result.body());
        return result.body();
    }


    public String calculator(String expression){
        HttpResponse result = HttpUtil.createPost(GATEWAY_HOST + "/chat/calculate")
                .form("expression", expression)
                .addHeaders(getHeaders(expression))
                .execute();


        System.out.println(result.body());
        return result.body();
    }


    private HashMap<String, String> getHeaders(String body) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("interfaceInfoId", String.valueOf(interfaceInfoId));
        headers.put("accessKey", accessKey);
        /**
         * 该请求不能用于发送，是用来生成签名的
         */
        //headers.put("secretKey",secretKey);
        //用户请求参数

        try {
            headers.put("body", URLEncoder.encode(body,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        //随机数
        headers.put("nonce", RandomUtil.randomNumbers(4));
        //时间戳
        headers.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        headers.put("sign", getSign(body, secretKey));
        return headers;
    }


    public String getUserNameByPost(String user) {

        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/name/user")
                .body(user)
                .addHeaders(getHeaders(user))
                .execute();
        System.out.println(httpResponse.getStatus());
        System.out.println(httpResponse.body());
        return httpResponse.body();
    }
}
