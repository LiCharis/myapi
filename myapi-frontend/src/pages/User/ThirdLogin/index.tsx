import {listInterfaceInfoVoByPageUsingPost} from '@/services/myapi-backend/interfaceController';
import {PageContainer} from '@ant-design/pro-components';
import {useModel} from '@umijs/max';
import {Button, List, message, Result} from 'antd';
import React, {useEffect, useState} from 'react';
import {history} from "@@/core/history";
import {useIntl} from "@@/exports";
import {getLoginUserUsingGet} from "@/services/myapi-backend/userController";
import {flushSync} from 'react-dom';
import {ApiOutlined} from "@ant-design/icons";

/**
 * 主页
 * @constructor
 */

const ThirdLogin: React.FC = () => {
  const {initialState, setInitialState} = useModel('@@initialState');
  const [loading, setLoading] = useState(false);
  const intl = useIntl();
  const urlParams = new URLSearchParams(window.location.search);


  // alert(accessToken)
  /**
   * 第三方登录
   */
  const thirdLogin = async () => {
    const state = localStorage.getItem('login_state') || '';

    const accessToken = urlParams.get('access_token');
    if (accessToken) {
      setLoading(true);
      if (state === 'GITEE') {

        const accessToken = urlParams.get('access_token');
        console.log("access_token", accessToken);

        const userObj = await fetch(
          'https://gitee.com/api/v5/user?access_token=' + accessToken,
          {
            method: 'GET',
          },
        );
        userObj.json().then(async (userInfo) => {
          const name: any = userInfo.name;
          const avatar_url: any = userInfo.avatar_url;

          console.log("name", name);
          console.log("avatar", avatar_url);
          try {
            const response = await fetch(
              'https://api.liproject.top/api/login3rd/login',
              {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                },
                mode: "cors",
                credentials: "include",
                body: JSON.stringify({
                  name: name,
                  avatar_url: avatar_url
                }),
              },
            );

            response.json().then(async (res) => {
              console.log("code", res.code);
              console.log("data", res.data);
              if (res.code === 0) {
                const defaultLoginSuccessMessage = intl.formatMessage({
                  id: 'pages.login.success',
                  defaultMessage: '登录成功！',
                });
                /**
                 * 设置当前登陆状态
                 */
                await fetchUserInfo();
                console.log("initialState", initialState);
                message.success(defaultLoginSuccessMessage);
                const urlParams = new URL(window.location.href).searchParams;

                history.push(urlParams.get('redirect') || '/index');
                return;
              } else {
                message.error(res.message);
                history.push('/user/login');
              }
            });

            // console.log('params', params);

          } catch (error: any) {
            message.error(error.message);
          }
          setLoading(false);
        });

      } else if (state === 'GITHUB') {
        const userObj = await fetch(
          'https://api.github.com/user',

          {
            method: 'GET',
            headers: {
              "Authorization":"Bearer " + accessToken,
            }
          },
        );
        userObj.json().then(async (userInfo) => {
          const name: any = userInfo.login;
          const avatar_url: any = userInfo.avatar_url;
          console.log("userInfo",userInfo);
          console.log("name", name);
          console.log("avatar", avatar_url);
          try {
            const response = await fetch(
              'https://api.liproject.top/api/login3rd/login',
              {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                },
                mode: "cors",
                credentials: "include",
                body: JSON.stringify({
                  name: name,
                  avatar_url: avatar_url
                }),
              },
            );

            response.json().then(async (res) => {
              console.log("code", res.code);
              console.log("data", res.data);
              if (res.code === 0) {
                const defaultLoginSuccessMessage = intl.formatMessage({
                  id: 'pages.login.success',
                  defaultMessage: '登录成功！',
                });
                /**
                 * 设置当前登陆状态
                 */
                await fetchUserInfo();
                console.log("initialState", initialState);
                message.success(defaultLoginSuccessMessage);
                const urlParams = new URL(window.location.href).searchParams;

                history.push(urlParams.get('redirect') || '/index');
                return;
              } else {
                message.error(res.message);
                history.push('/user/login');
              }
            });

            // console.log('params', params);

          } catch (error: any) {
            message.error(error.message);
          }
          setLoading(false);
        });
      }
    } else {
      message.error('第三方验证失败,请稍后重试');
      history.push('/user/login');
    }
  }


/**
 * 获取用户登录信息
 */
const fetchUserInfo = async () => {
  const res = await getLoginUserUsingGet();
  if (res) {
    flushSync(() => {
      // @ts-ignore
      setInitialState((s) => ({
        ...s,
        loginUser: res.data,
      }));
    });
  }
};

useEffect(() => {
  thirdLogin();
}, [])

return <>
  <Result
    icon={<ApiOutlined/>}
    title="第三方检测中,请稍等..."
  />

</>;
}
;

export default ThirdLogin;
