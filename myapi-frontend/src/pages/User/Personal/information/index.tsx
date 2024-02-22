import React, {useEffect, useState} from 'react';
import {
  Layout,
  Flex,
  Avatar,
  message,
  Menu,
  MenuProps,
  DatePicker,
  Input,
  Form,
  Button, Card, Divider, Space, Modal, Alert, Typography, Tag, Popconfirm
} from 'antd';
import {
  deleteMyUserUsingPost,
  getLoginUserUsingGet, updateMyUserAccountUsingPost, updateMyUserPasswordUsingPost,
  updateMyUserUsingPost
} from "@/services/myapi-backend/userController";
import {
  AntDesignOutlined,
  AppstoreOutlined,
  MailOutlined,
  PlusOutlined,
  SettingOutlined,
  TwitterOutlined
} from "@ant-design/icons";
import {history, useModel} from "@@/exports";

const {Sider, Content} = Layout;


const contentStyle: React.CSSProperties = {
  textAlign: 'center',
  minHeight: 120,
  lineHeight: '120px',
  color: '#e5e9ef',
  backgroundColor: "white"
};

const siderStyle: React.CSSProperties = {
  textAlign: 'center',
  lineHeight: '120px',
  color: '#e5e9ef',
  backgroundColor: "white",
  marginRight: 10,
  borderRadius: "1%",
  border: "solid",
  borderColor: "ghostwhite",
  borderWidth: "1px"
};


const layoutStyle = {
  borderRadius: 8,
  overflow: 'hidden',
  width: 'calc(50% - 8px)',
  maxWidth: 'calc(80% - 8px)',
  margin: "auto"
};

/**
 * 加载个人信息
 */
const information: React.FC = () => {
  const {initialState, setInitialState} = useModel('@@initialState');
  const loginUser = initialState?.loginUser;
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.LoginUserVO>();

  const loadData = async () => {
    setLoading(true);
    const res = await getLoginUserUsingGet();
    try {
      if (res.code === 0) {
        setData(res?.data);
      }
    } catch (error: any) {
      message.error('请求失败' + res.message);
    }
    setLoading(false);
  };
//加载数据
  useEffect(() => {
    loadData();
  }, []);

  /**
   * 菜单栏
   */
  type MenuItem = Required<MenuProps>['items'][number];

  function getItem(
    label: React.ReactNode,
    key?: React.Key | null,
    icon?: React.ReactNode,
    children?: MenuItem[],
    type?: 'group',
  ): MenuItem {
    return {
      key,
      icon,
      children,
      label,
      type,
    } as MenuItem;
  }

  const items: MenuItem[] = [
    getItem('基本设置', 'sub1', <MailOutlined/>, [
      getItem('账号管理', '1',),
      getItem('个人资料', '2',),
    ]),

    getItem('其他应用', 'sub2', <AppstoreOutlined/>, [
      getItem('OJ平台', '5'),
      getItem('智能BI', '6'),
      getItem('模板中心', 'sub3'),
    ]),

  ];
  const [current, setCurrent] = useState('1');
  const onClick: MenuProps['onClick'] = (e) => {
    console.log('click ', e);
    setCurrent(e.key);
  };


  const {RangePicker} = DatePicker;
  const {TextArea} = Input;

  const normFile = (e: any) => {
    if (Array.isArray(e)) {
      return e;
    }
    return e?.fileList;
  };
  const [componentDisabled, setComponentDisabled] = useState<boolean>(true);

  /**
   * 修改个人基本信息
   */
  const updateBasicInfo = async (values: any) => {
    const res = await updateMyUserUsingPost(values);
    try {
      if (res.code === 0) {
        loadData();
        message.success("更新成功");
      } else {
        message.error(res.message);
      }
    } catch (error: any) {
      message.error(error.message);
    }
  }


  /**
   * 账号注销
   */
  const deleteAccount = async () => {
    try {
      const res = await deleteMyUserUsingPost({id: loginUser?.id})
      if (res.code === 0) {
        message.success("注销成功");
        /**
         * 设置当前登陆状态为空
         */
        setInitialState({
          loginUser: undefined,
        });
        history.push('/user/login');
        return;
      } else {
        message.error(res.message);
      }
    } catch (error: any) {
      message.error(error.message);
    }

  }


  /**
   * 修改账号
   */
  const [isAccountModalOpen, setAccountIsModalOpen] = useState(false);

  const showAccountModal = () => {
    setAccountIsModalOpen(true);
  };

  const handleAccountOk = () => {
    setAccountIsModalOpen(false);
  };

  const handleAccountCancel = () => {
    setAccountIsModalOpen(false);
  };


  const onAccountFinish = async (values: any) => {
    try {
      const res = await updateMyUserAccountUsingPost({...values})
      if (res.code === 0) {
        message.success("账号修改成功");
        /**
         * 设置当前登陆状态为空
         */
        setInitialState({
          loginUser: undefined,
        });
        history.push('/user/login');
        return;
      } else {
        message.error(res.message);
      }
    } catch (error: any) {
      message.error(error.message);
    }

  };

  const onAccountFinishFailed = (errorInfo: any) => {
    message.error(errorInfo);
  };

  type FieldType1 = {
    currentPassword?: string;
    userAccount?: string;
  };


  /**
   * 修改密码
   */

  const [isPasswordModalOpen, setPasswordIsModalOpen] = useState(false);

  const showPasswordModal = () => {
    setPasswordIsModalOpen(true);
  };

  const handlePasswordOk = () => {
    setPasswordIsModalOpen(false);
  };

  const handlePasswordCancel = () => {
    setPasswordIsModalOpen(false);
  };


  const onPasswordFinish = async (values: any) => {
    try {
      const res = await updateMyUserPasswordUsingPost({...values})
      if (res.code === 0) {
        message.success("密码修改成功");
      } else {
        message.error(res.message);
      }
    } catch (error: any) {
      message.error(error.message);
    }
    setPasswordIsModalOpen(false);
  };

  const onPasswordFinishFailed = (errorInfo: any) => {
    message.error(errorInfo);
  };

  type FieldType2 = {
    userCurrentPassword?: string;
    userNewPassword?: string;
    userCheckPassword?: string;
  };

  return (
    <Flex gap="middle" wrap="wrap">

      <Layout style={layoutStyle}>
        <Layout>
          <Sider width="20%" style={siderStyle}>

            <Space>
              <Avatar
                size={{xs: 24, sm: 32, md: 40, lg: 64, xl: 80, xxl: 100}}
                icon={<AntDesignOutlined/>}
                src={data?.userAvatar}
                draggable={true}
                shape={"circle"}
              />

              <Tag icon={<TwitterOutlined/>} color="#55acee">
                <Space size={"large"}>{data?.userName}</Space>
              </Tag>

            </Space>


            <Divider/>
            <Menu
              theme={"light"}
              onClick={onClick}
              style={{width: '100%'}}
              defaultOpenKeys={['sub1']}
              selectedKeys={[current]}
              mode="inline"
              items={items}
            />
          </Sider>
          <Content style={contentStyle}>
            {
              current === '2' ?
                (<Card title="基本信息" style={{width: "100%", height: "100%", backgroundColor: "#f0f2f5"}}
                       loading={loading}>
                  <Form
                    labelCol={{span: 4}}
                    wrapperCol={{span: 14}}
                    layout="horizontal"
                    style={{maxWidth: 600}}
                    onFinish={updateBasicInfo}
                  >

                    <Form.Item label="用户名" name={"userName"}>
                      <Input defaultValue={data?.userName} size={"large"}/>
                    </Form.Item>
                    <Form.Item label="自我介绍" name={"userProfile"}>
                      <TextArea rows={8} defaultValue={data?.userProfile}/>
                    </Form.Item>
                    <Form.Item>
                      <Button type={"primary"} htmlType="submit">修改</Button>
                    </Form.Item>

                  </Form>
                </Card>)
                : (<div><Card title={"账号信息"} style={{width: "100%", height: "100%"}} loading={loading}>
                  <Form
                    labelCol={{span: 4}}
                    wrapperCol={{span: 14}}
                    layout="horizontal"
                    style={{maxWidth: 600}}
                  >
                    <Form.Item label="账号">
                      <Space>
                        <Input disabled size={"large"} value={data?.userAccount}/>
                        <Button type={"dashed"} size={"large"} onClick={showAccountModal}>更换账号</Button>
                        <Modal centered width={"30%"} title="更换账号" open={isAccountModalOpen} onOk={handleAccountOk}
                               onCancel={handleAccountCancel} footer={null}>
                          <Form
                            name="basic"
                            labelCol={{span: 8}}
                            wrapperCol={{span: 16}}
                            style={{maxWidth: 600}}
                            initialValues={{remember: true}}
                            onFinish={onAccountFinish}
                            onFinishFailed={onAccountFinishFailed}
                            autoComplete="off"
                            labelAlign={"left"}
                          >
                            <Alert message="更换账号后，你将无法通过 「原账号+密码」 登录" banner/>
                            <br/>
                            <Typography.Title level={5}>当前账号密码</Typography.Title>
                            <Form.Item<FieldType1>
                              name="currentPassword"
                              rules={[{required: true, message: '请输入当前帐号密码'}]}
                            >

                              <Input.Password placeholder={"请输入当前账号密码"}/>

                            </Form.Item>
                            <Typography.Title level={5}>新账号</Typography.Title>
                            <Form.Item<FieldType1>
                              name="userAccount"
                              rules={[{required: true, message: '请输入新账号'}]}
                            >

                              <Input placeholder={"请输入新账号"}/>
                            </Form.Item>

                            <Form.Item>
                              <Button type="primary" htmlType="submit">
                                修改
                              </Button>
                            </Form.Item>

                          </Form>
                        </Modal>
                      </Space>
                    </Form.Item>
                    <Form.Item label="密码">
                      <Space>
                        <Input.Password disabled size={"large"} value={"123456789"}/>
                        <Button type={"dashed"} size={"large"} onClick={showPasswordModal}>修改密码</Button>
                        <Modal centered width={"30%"} title="修改密码" open={isPasswordModalOpen} onOk={handlePasswordOk}
                               onCancel={handlePasswordCancel} footer={null}>
                          <Alert message="第三方登录的初始密码为123456789" type="success" closable/>
                          <br/>
                          <Form
                            name="basic"
                            labelCol={{span: 8}}
                            wrapperCol={{span: 16}}
                            style={{maxWidth: 600}}
                            initialValues={{remember: true}}
                            onFinish={onPasswordFinish}
                            onFinishFailed={onPasswordFinishFailed}
                            autoComplete="off"
                            labelAlign={"left"}
                          >
                            <Typography.Title level={5}>当前账号密码</Typography.Title>
                            <Form.Item<FieldType2>
                              name="userCurrentPassword"
                              rules={[{required: true, message: '请输入当前帐号密码'}]}
                            >

                              <Input.Password placeholder={'请输入当前帐号密码'}/>
                            </Form.Item>
                            <Typography.Title level={5}>新密码</Typography.Title>
                            <Form.Item<FieldType2>
                              name="userNewPassword"
                              rules={[{required: true, message: '请输入新密码'}]}
                            >

                              <Input.Password placeholder={'请输入新密码'}/>
                            </Form.Item>
                            <Typography.Title level={5}>确定密码</Typography.Title>
                            <Form.Item<FieldType2>
                              name="userCheckPassword"
                              rules={[{required: true, message: '请再次输入新密码'}]}
                            >

                              <Input.Password placeholder={'请再次输入新密码'}/>
                            </Form.Item>
                            <Form.Item>
                              <Button type="primary" htmlType="submit">
                                修改
                              </Button>
                            </Form.Item>

                          </Form>
                        </Modal>
                      </Space>

                    </Form.Item>

                  </Form>
                </Card>
                  <Card title={"账号注销"}>
                    <Form
                      labelCol={{span: 4}}
                      wrapperCol={{span: 14}}
                      layout="horizontal"
                      style={{maxWidth: 600}}
                      onFinish={deleteAccount}
                    >
                      <Form.Item>
                        <Card style={{width: 700}}>
                          <Space>
                            <p>注销 API开放平台 的帐号是不可恢复的操作，你应自行备份该帐号相关的信息和数据。注销帐号后你将丢失该帐号自注册以来产生的数据和记录，注销后相关数据将不可恢复。</p>
                            <Popconfirm
                              title="你确定要注销该用户吗?"
                              description="该操作不可逆"
                              okText="确定"
                              cancelText="取消"
                            >
                              <Button danger size={"large"} htmlType={"submit"}>注销</Button>
                            </Popconfirm>

                          </Space>
                        </Card>

                      </Form.Item>

                    </Form>
                  </Card>
                </div>)
            }

          </Content>
        </Layout>
      </Layout>


    </Flex>
  )


};

export default information;
