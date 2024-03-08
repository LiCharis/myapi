import Footer from '@/components/Footer';
import {getLoginUserUsingGet, userLoginUsingPost} from '@/services/myapi-backend/userController';
import Icon, {
  GithubFilled, GitlabFilled,
  LockOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {LoginForm, ProFormCheckbox, ProFormText} from '@ant-design/pro-components';
import {useEmotionCss} from '@ant-design/use-emotion-css';
import {FormattedMessage, Helmet, history, SelectLang, useIntl, useModel} from '@umijs/max';
import {Alert, Button, Checkbox, Form, message, Tabs} from 'antd';
import React, {useState} from 'react';
import Settings from '../../../../config/defaultSettings';
import {Oauth} from "@/components/3thLogin";
import {CustomIconComponentProps} from "@ant-design/icons/es/components/Icon";


const giteeSvg = () => (
  <svg t="17060245giteeSvg25783" className="icon" viewBox="0 0 1024 1024" version="1.1"
       xmlns="http://www.w3.org/2000/svg" p-id="4759" width="30" height="30">
    <path
      d="M512 1024C229.222 1024 0 794.778 0 512S229.222 0 512 0s512 229.222 512 512-229.222 512-512 512z m259.149-568.883h-290.74a25.293 25.293 0 0 0-25.292 25.293l-0.026 63.206c0 13.952 11.315 25.293 25.267 25.293h177.024c13.978 0 25.293 11.315 25.293 25.267v12.646a75.853 75.853 0 0 1-75.853 75.853h-240.23a25.293 25.293 0 0 1-25.267-25.293V417.203a75.853 75.853 0 0 1 75.827-75.853h353.946a25.293 25.293 0 0 0 25.267-25.292l0.077-63.207a25.293 25.293 0 0 0-25.268-25.293H417.152a189.62 189.62 0 0 0-189.62 189.645V771.15c0 13.977 11.316 25.293 25.294 25.293h372.94a170.65 170.65 0 0 0 170.65-170.65V480.384a25.293 25.293 0 0 0-25.293-25.267z"
      fill="#C71D23" p-id="4760"></path>
  </svg>
)


const githubSvg = () => (
  <svg t="1706071950714" className="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
       p-id="5719" width="35" height="35">
    <path
      d="M512 73.142857c242.285714 0 438.857143 196.571429 438.857143 438.857143 0 193.718857-125.696 358.290286-299.995429 416.585143-22.272 3.986286-30.281143-9.728-30.281143-21.138286 0-14.299429 0.585143-61.696 0.585143-120.576 0-41.142857-13.714286-67.437714-29.696-81.152 97.718857-10.861714 200.557714-48.018286 200.557715-216.576 0-48.018286-17.152-86.857143-45.129143-117.723428 4.571429-11.446857 19.419429-55.990857-4.571429-116.553143-36.571429-11.446857-120.576 45.129143-120.576 45.129143a412.525714 412.525714 0 0 0-219.428571 0S318.317714 243.419429 281.746286 254.866286c-23.990857 60.562286-9.142857 105.142857-4.571429 116.553143-28.013714 30.866286-45.129143 69.705143-45.129143 117.723428 0 168.009143 102.290286 205.714286 200.009143 216.576-12.580571 11.446857-23.990857 30.866286-28.013714 58.843429-25.161143 11.446857-89.161143 30.866286-127.414857-36.571429-23.990857-41.728-67.437714-45.129143-67.437715-45.129143-42.861714-0.585143-2.852571 26.843429-2.852571 26.843429 28.562286 13.129143 48.566857 64 48.566857 64 25.709714 78.299429 148.004571 52.004571 148.004572 52.004571 0 36.571429 0.585143 70.838857 0.585142 81.700572 0 11.446857-8.009143 25.161143-30.281142 21.138285C198.912 870.253714 73.216 705.682286 73.216 511.963429c0-242.285714 196.571429-438.857143 438.857143-438.857143zM239.433143 703.414857c1.133714-2.304-0.585143-5.156571-3.986286-6.838857-3.437714-1.133714-6.290286-0.585143-7.424 1.133714-1.133714 2.304 0.585143 5.156571 3.986286 6.838857 2.852571 1.718857 6.290286 1.133714 7.424-1.133714z m17.700571 19.456c2.304-1.718857 1.718857-5.705143-1.133714-9.142857-2.852571-2.852571-6.838857-3.986286-9.142857-1.718857-2.304 1.718857-1.718857 5.705143 1.133714 9.142857 2.852571 2.852571 6.838857 3.986286 9.142857 1.718857z m17.152 25.709714c2.852571-2.304 2.852571-6.838857 0-10.861714-2.304-3.986286-6.838857-5.705143-9.728-3.437714-2.852571 1.718857-2.852571 6.290286 0 10.276571s7.424 5.705143 9.728 3.986286z m23.990857 23.990858c2.304-2.304 1.133714-7.424-2.304-10.861715-3.986286-3.986286-9.142857-4.571429-11.446857-1.718857-2.852571 2.304-1.718857 7.424 2.304 10.861714 3.986286 3.986286 9.142857 4.571429 11.446857 1.718858z m32.585143 14.299428c1.133714-3.437714-2.304-7.424-7.424-9.142857-4.571429-1.133714-9.728 0.585143-10.861714 3.986286s2.304 7.424 7.424 8.557714c4.571429 1.718857 9.728 0 10.861714-3.437714z m35.986286 2.852572c0-3.986286-4.571429-6.838857-9.728-6.290286-5.156571 0-9.142857 2.852571-9.142857 6.290286 0 3.986286 3.986286 6.838857 9.728 6.290285 5.156571 0 9.142857-2.852571 9.142857-6.290285z m33.133714-5.705143c-0.585143-3.437714-5.156571-5.705143-10.276571-5.156572-5.156571 1.133714-8.557714 4.571429-8.009143 8.557715 0.585143 3.437714 5.156571 5.705143 10.276571 4.571428s8.557714-4.571429 8.009143-8.009143z"
      fill="" p-id="5720"></path>
  </svg>
)

const ActionIcons = () => {

  const langClassName = useEmotionCss(({token}) => {
    return {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    };
  });

  return (
    <>
      {/*<AlipayCircleOutlined key="AlipayCircleOutlined" className={langClassName} />*/}
      {/*<TaobaoCircleOutlined key="TaobaoCircleOutlined" className={langClassName}/>*/}
      {/*<WeiboCircleOutlined key="WeiboCircleOutlined" className={langClassName}/>*/}
      <GitlabFilled key="GitlabCircleOutLined" className={langClassName}/>
      <GithubFilled key="GiteeCircleOutLined" className={langClassName}/>
    </>
  );
};

const Lang = () => {
  const langClassName = useEmotionCss(({token}) => {
    return {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    };
  });

  return (
    <div className={langClassName} data-lang>
      {SelectLang && <SelectLang/>}
    </div>
  );
};

const LoginMessage: React.FC<{
  content: string;
}> = ({content}) => {
  return (
    <Alert
      style={{
        marginBottom: 24,
      }}
      message={content}
      type="error"
      showIcon
    />
  );
};

const Login: React.FC = () => {
  const urlParams = new URLSearchParams(window.location.search);
  const [userLoginState, setUserLoginState] = useState<API.LoginResult>({});
  const [type, setType] = useState<string>('account');
  const {initialState, setInitialState} = useModel('@@initialState');


  const containerClassName = useEmotionCss(() => {
    return {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    };
  });

  const intl = useIntl();

  // /**
  //  * 第三方登录
  //  */
  // const checkLogin3rd = async () => {
  //   const state = localStorage.getItem('login_state') || '';
  //   if (state === 'GITEE') {
  //     const accessToken = urlParams.get('access_token');
  //     console.log("access_token",accessToken);
  //
  //     const userObj = await fetch(
  //       'https://gitee.com/api/v5/user?access_token=' + accessToken,
  //       {
  //         method: 'GET',
  //       },
  //     );
  //     const userInfo: any = userObj.json();
  //     const name: string = userInfo?.name;
  //     const avatar_url: string = userInfo?.avatar_url;
  //
  //     try {
  //       const response = await fetch(
  //         '/api/login3rd/login',
  //         {
  //           method: 'POST',
  //           headers: {
  //             'Content-Type': 'application/json',
  //           },
  //           body: JSON.stringify({name: name, avatar_url: avatar_url}),
  //         },
  //       );
  //
  //       const res: any = response.json();
  //
  //       // console.log('params', params);
  //       if (res.code === 0) {
  //         const defaultLoginSuccessMessage = intl.formatMessage({
  //           id: 'pages.login.success',
  //           defaultMessage: '登录成功！',
  //         });
  //         /**
  //          * 设置当前登陆状态
  //          */
  //         setInitialState({
  //           loginUser: res.data,
  //         });
  //         message.success(defaultLoginSuccessMessage);
  //         const urlParams = new URL(window.location.href).searchParams;
  //
  //         history.push(urlParams.get('redirect') || '/');
  //         return;
  //       } else {
  //         message.error(res.message);
  //       }
  //     } catch (error: any) {
  //       message.error(error.message);
  //     }
  //   }
  //
  // }
  //
  // checkLogin3rd()


  const handleSubmit = async (values: API.UserLoginRequest) => {
    try {
      // 登录
      const res = await userLoginUsingPost({...values});
      if (res.code === 0) {
        const defaultLoginSuccessMessage = intl.formatMessage({
          id: 'pages.login.success',
          defaultMessage: '登录成功！',
        });
        /**
         * 设置当前登陆状态
         */
        setInitialState({
          loginUser: res.data,
        });
        message.success(defaultLoginSuccessMessage);
        const urlParams = new URL(window.location.href).searchParams;

        history.push(urlParams.get('redirect') || '/index');

        return;
      } else {
        message.error(res.message);
      }
      console.log(res);
    } catch (error) {
      const defaultLoginFailureMessage = intl.formatMessage({
        id: 'pages.login.failure',
        defaultMessage: '登录失败，请重试！',
      });
      console.log(error);
      message.error(defaultLoginFailureMessage);
    }
  };
  const {status, type: loginType} = userLoginState;

  // @ts-ignore
  return (
    <div className={containerClassName}>
      <Helmet>
        <title>
          {intl.formatMessage({
            id: 'menu.login',
            defaultMessage: '登录页',
          })}
          - {Settings.title}
        </title>
      </Helmet>
      <Lang/>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          logo={<img alt="logo" src="/logo.svg"/>}
          title="API开放平台"
          subTitle={intl.formatMessage({id: 'pages.layouts.userLayout.title'})}
          initialValues={{
            autoLogin: true,
          }}
          actions={[
            <FormattedMessage
              key="loginWith"
              id="pages.login.loginWith"
              defaultMessage="其他登录方式"
            />,
            <span>
              <Button type="link" size={"middle"} icon={giteeSvg()} onClick={Oauth.getGiteeCode}/>
              <Button type="link" size={"middle"} icon={githubSvg()} onClick={Oauth.getGithubCode}/>
            </span>


            // <a onClick={OauthGitee.getCode}>
            //   <GiteeIcon/>
            // </a>

          ]}
          onFinish={async (values) => {
            await handleSubmit(values as API.UserLoginRequest);
          }}
        >
          <Tabs
            activeKey={type}
            onChange={setType}
            centered
            items={[
              {
                key: 'account',
                label: intl.formatMessage({
                  id: 'pages.login.accountLogin.tab',
                  defaultMessage: '账户密码登录',
                }),
              },

            ]}
          />

          {status === 'error' && loginType === 'account' && (
            <LoginMessage
              content={intl.formatMessage({
                id: 'pages.login.accountLogin.errorMessage',
                defaultMessage: '账户或密码错误',
              })}
            />
          )}
          {type === 'account' && (
            <>
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined/>,
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.username.placeholder',
                  defaultMessage: '用户名',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.username.required"
                        defaultMessage="请输入用户名!"
                      />
                    ),
                  },
                ]}
              />
              <ProFormText.Password
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined/>,
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.password.placeholder',
                  defaultMessage: '密码',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.password.required"
                        defaultMessage="请输入密码！"
                      />
                    ),
                  },
                ]}
              />
              <div
                style={{
                  marginBottom: 24,
                }}
              >
                <ProFormCheckbox noStyle name="autoLogin">
                  <FormattedMessage id="pages.login.rememberMe" defaultMessage="自动登录"/>
                </ProFormCheckbox>
                <a
                  style={{
                    float: 'right',
                  }}
                >
                  <FormattedMessage id="pages.login.forgotPassword" defaultMessage="忘记密码"/>
                </a>
              </div>
              <a href="/user/register">没有账号，快速注册</a>


            </>

          )}

        </LoginForm>

      </div>
      <Footer/>
    </div>
  );
};

export default Login;
