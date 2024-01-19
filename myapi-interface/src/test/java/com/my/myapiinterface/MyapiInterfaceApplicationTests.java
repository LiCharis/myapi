package com.my.myapiinterface;

import com.my.myapiclientsdk.client.MyApiClient;
import com.my.myapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MyapiInterfaceApplicationTests {

  @Resource
  private MyApiClient myApiClient;

  @Test
  public void contextLoads(){
    myApiClient.getNameByGet("lili1");
    myApiClient.getNameByPost("lili2");
    User user = new User();
    user.setUsername("lili3");
    myApiClient.getUserNameByPost(user);
  }

}
