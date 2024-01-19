import { getLoginUserUsingGet, updateKeysUsingPost } from '@/services/myapi-backend/userController';
import { DownloadOutlined, RedoOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import { Button, Card, Descriptions, DescriptionsProps, Divider, Form, message, Modal } from 'antd';
import moment from 'moment';
import React, { useEffect, useState } from 'react';

/**
 * 主页
 * @constructor
 */

const Keys: React.FC = () => {
  const { initialState } = useModel('@@initialState');
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

  // @ts-ignore
  // @ts-ignore
  const items: DescriptionsProps['items'] = data
    ? [
        {
          key: '2',
          label: 'accessKey',
          children: data.accessKey,
        },
        {
          key: '3',
          label: 'secretKey',
          children: data.secretKey,
        },
        {
          key: '4',
          label: '更新时间',
          children: moment(data.updateTime).format('YYYY-MM-DD HH:mm:ss'),
        },
      ]
    : [
        {
          key: '1',
          label: '异常',
          children: '用户不存在',
        },
      ];

  const onFinish = async (values: any) => {
    setLoading(true);
    try {
      const res = await updateKeysUsingPost();
      if (res.code === 0) {
        loadData();
        message.success('密钥信息更新成功');
      } else {
        message.error(res.message);
      }
    } catch (error: any) {
      message.error(error.message); //error.message
    }
    setLoading(false);
  };

  const [isModalOpen, setIsModalOpen] = useState(false);

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleOk = () => {
    setIsModalOpen(false);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const dependency = {
    groupId: 'com.my',
    artifactId: 'myapi-client-sdk',
    version: '0.0.1',
  };

  // 转为字符串
  const dependencyString = `<dependency>
  <groupId>${dependency.groupId}</groupId>
  <artifactId>${dependency.artifactId}</artifactId>
  <version>${dependency.version}</version>
</dependency>`;

  // @ts-ignore
  return (
    <PageContainer title="查看个人密钥信息">
      <Card title={data?.userName + '的开发者密钥（调用接口的凭证）'} loading={loading}>
        <Descriptions items={items} column={1} />
        <Divider />
        <Form name={'updateKeys'} layout={'vertical'} onFinish={onFinish}>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              <RedoOutlined />更新信息
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title={'开发者 SDK(让你的程序也可以获得这些接口的功能)'}>
        <>
          <Button type="default" onClick={showModal}>
            <DownloadOutlined />Java SDK
          </Button>
          <Modal title="将该依赖引入到自己项目的pom.xml文件中" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
           <pre>{dependencyString}</pre>
          </Modal>
        </>
      </Card>
    </PageContainer>
  );
};

export default Keys;
