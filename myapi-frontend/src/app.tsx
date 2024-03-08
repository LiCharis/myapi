import Footer from '@/components/Footer';
import {Question, SelectLang} from '@/components/RightContent';
import {getLoginUserUsingGet} from '@/services/myapi-backend/userController';
import InitialState from '@@/plugin-initialState/@@initialState';
import {LinkOutlined} from '@ant-design/icons';
import {SettingDrawer} from '@ant-design/pro-components';
import type {RunTimeLayoutConfig} from '@umijs/max';
import {history, Link} from '@umijs/max';
import {AvatarDropdown, AvatarName} from './components/RightContent/AvatarDropdown';
import {errorConfig} from './requestErrorConfig';
import Oauth from "@/components/3thLogin";
import {useIntl, useModel} from "@@/exports";
import {message} from "antd";

const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/user/login';
const urlParams = new URLSearchParams(window.location.search);

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  loginUser: API.LoginUserVO,
}> {

//当页面首次加载时，保存全局信息
  const fetchUserInfo = async () => {
    try {
      const res = await getLoginUserUsingGet();
      return res.data;
    } catch (error) {
      history.push(loginPath);
    }
    return undefined;
  };
// 如果不是登录页面，执行
  const {location} = history;
  console.log(location.pathname)
  if (location.pathname !== loginPath) {
    const loginUser = await fetchUserInfo();
    console.log("fffffffffff")
    return {
      loginUser,
    };
  }
//如果是在登录界面,不返回登陆信息
  return {};
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({initialState, setInitialState}) => {
  const intl = useIntl();
  const urlParams = new URLSearchParams(window.location.search);
  return {
    actionsRender: () => [<Question key="doc"/>, <SelectLang key="SelectLang"/>],
    avatarProps: {
      src: initialState?.loginUser?.userAvatar,
      title: <AvatarName/>,
      render: (_, avatarChildren) => {
        return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
      },
    },
    waterMarkProps: {
      content: initialState?.loginUser?.userName,
    },

    footerRender: () => <Footer/>,
    onPageChange: async () => {

      const {location} = history;
      // 如果没有登录，重定向到 login
      console.log("location.pathname",location.pathname)
      console.log("initialState?.loginUser",initialState?.loginUser)
      if (!initialState?.loginUser && location.pathname !== loginPath ) {
        // const accessToken = urlParams.get('access_token');
        //
        // // alert(accessToken)
        // /**
        //  * 第三方登录
        //  */
        // if (accessToken) {
        //   const state = localStorage.getItem('login_state') || '';
        //   if (state === 'GITEE') {
        //     const accessToken = urlParams.get('access_token');
        //     console.log("access_token", accessToken);
        //
        //     const userObj = await fetch(
        //       'https://gitee.com/api/v5/user?access_token=' + accessToken,
        //       {
        //         method: 'GET',
        //       },
        //     );
        //     userObj.json().then(async (userInfo) => {
        //       const name: any = userInfo.name;
        //       const avatar_url: any = userInfo.avatar_url;
        //       console.log("name", name);
        //       console.log("avatar", avatar_url);
        //       try {
        //         const response = await fetch(
        //           'http://127.0.0.1:8101/api/login3rd/login',
        //           {
        //             method: 'POST',
        //             headers: {
        //               'Content-Type': 'application/json',
        //             },
        //             mode: "cors",
        //             credentials: "include",
        //             body: JSON.stringify({
        //               name: name,
        //               avatar_url: avatar_url
        //             }),
        //           },
        //         );
        //
        //         response.json().then((res) => {
        //           console.log("code", res.code);
        //           if (res.code === 0) {
        //             const defaultLoginSuccessMessage = intl.formatMessage({
        //               id: 'pages.login.success',
        //               defaultMessage: '登录成功！',
        //             });
        //             /**
        //              * 设置当前登陆状态
        //              */
        //             setInitialState({
        //               loginUser: res.data,
        //             });
        //             message.success(defaultLoginSuccessMessage);
        //             const urlParams = new URL(window.location.href).searchParams;
        //
        //             history.push(urlParams.get('redirect') || '/');
        //             return;
        //           } else {
        //             message.error(res.message);
        //           }
        //         });
        //
        //         // console.log('params', params);
        //
        //       } catch (error: any) {
        //         message.error(error.message);
        //       }
        //     });
        //
        //   }
        // }
        console.log("gggggggggg")
        history.push(loginPath);
      }
    },
    layoutBgImgList: [
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/D2LWSqNny4sAAAAAAAAAAAAAFl94AQBr',
        left: 85,
        bottom: 100,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/C2TWRpJpiC0AAAAAAAAAAAAAFl94AQBr',
        bottom: -68,
        right: -45,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/F6vSTbj8KpYAAAAAAAAAAAAAFl94AQBr',
        bottom: 0,
        left: 0,
        width: '331px',
      },
    ],
    links: isDev
      ? [
        <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
          <LinkOutlined/>
          <span>OpenAPI 文档</span>
        </Link>,
      ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          <SettingDrawer
            disableUrlParams
            enableDarkTheme
            settings={initialState?.settings}
            onSettingChange={(settings) => {
              setInitialState((preInitialState) => ({
                ...preInitialState,
                settings,
              }));
            }}
          />
        </>
      );
    },
    ...initialState?.settings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request = {
  ...errorConfig,
};
