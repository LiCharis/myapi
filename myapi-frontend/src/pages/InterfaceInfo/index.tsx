import {
  downloadFileUsingGet,
  getInterfaceInfoVoByIdUsingGet,
  invokeInterfaceInfoUsingPost,
} from '@/services/myapi-backend/interfaceController';
import {InboxOutlined} from '@ant-design/icons';
import {PageContainer} from '@ant-design/pro-components';
import {useModel} from '@umijs/max';
import {
  Button,
  Card,
  Col,
  Descriptions,
  DescriptionsProps,
  Divider,
  Flex,
  Form,
  Input,
  message,
  Rate,
  Row,
  Upload,
} from 'antd';
import moment from 'moment';
import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router';

/**
 * 主页
 * @constructor
 */

const InterfaceInfo: React.FC = () => {
  const {initialState} = useModel('@@initialState');
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.InterfaceInfo>();
  const [invokeRes, setInvokeRes] = useState<any>();
  const [invokeLoading, setInvokeLoading] = useState<boolean>();
  const params = useParams();

  const loadData = async () => {
    if (!params.id) {
      message.error('接口不存在');
      return;
    }
    setLoading(true);
    const res = await getInterfaceInfoVoByIdUsingGet({id: params.id});
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
  // @ts-ignore
  const items: DescriptionsProps['items'] = data
    ? [
      {
        key: '1',
        label: '接口名',
        children: data.name,
      },
      {
        key: '2',
        label: '描述',
        children: data.description,
      },
      {
        key: '3',
        label: '接口状态',
        children: data.status === 0 ? '关闭' : '开启',
      },
      {
        key: '4',
        label: '请求地址',
        children: data?.protocol + data?.host + data?.path,
      },

      {
        key: '5',
        label: '请求参数',
        children: data.requestBody,
      },
      // {
      //   key: '6',
      //   label: '参数类型',
      //   children: data.parameterType,
      // },

      {
        key: '7',
        label: '请求方法',
        children: data.method,
      },
      {
        key: '8',
        label: '请求头',
        children: data.requestHeader,
      },
      {
        key: '9',
        label: '响应头',
        children: data.responseHeader,
      },
      {
        key: '10',
        label: '创建时间',
        children: moment(data.createTime).format('YYYY-MM-DD HH:mm:ss'),
      },
      {
        key: '11',
        label: '更新时间',
        children: moment(data.updateTime).format('YYYY-MM-DD HH:mm:ss'),
      },
    ]
    : [
      {
        key: '1',
        label: '异常',
        children: '接口不存在',
      },
    ];

  const onFinish = async (values: any) => {
    if (!params.id) {
      message.error('接口不存在');
      return;
    }
    const file = values?.file ? values.file.file.originFileObj : null;
    try {
      setInvokeLoading(true);
      const res = await invokeInterfaceInfoUsingPost(
        {
          ...values,
          id: params.id,
          file: undefined,
        },
        {},
        file,
      );

      if (res.code === 0) {
        if (typeof res.data !== 'string') {
          res.data = JSON.stringify(res.data);
        }
        setInvokeRes(res.data);
        message.success('接口调用成功');
      } else {
        message.error(res.message);
      }
    } catch (error: any) {
      message.error(error.message); //error.message
    }
    setInvokeLoading(false);
  };

  const download = async (value: any) => {
    const res = await downloadFileUsingGet({filePath: invokeRes});
    try {
      // @ts-ignore
      const blob = new Blob([res]);
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      const arr = invokeRes.split('/');
      const length = arr.length;
      const filename = arr[length - 1];
      link.download = filename;
      link.click();
    } catch (error: any) {
      message.error(error.message); //error.message
    }
  };

  const desc = ['terrible', 'bad', 'normal', 'good', 'wonderful'];
  const [value, setValue] = useState(3);

  // @ts-ignore
  return (
    <PageContainer title="查看接口文档">
      <Row gutter={24}>
        <Col span={12}>
          <Card title={'在线测试'}>
            <Descriptions title="接口详细信息" items={items} column={1}/>
          </Card>
          <Card title={"接口使用体验评分"}>
            <Flex gap="middle" vertical title={"请您打个评分哦"}>
              <Rate tooltips={desc} onChange={setValue} value={value}/>
              {value ? <span>{desc[value - 1]}</span> : null}
            </Flex>
          </Card>
        </Col>
        <Col span={12}>
          <Card title={'请求参数'}>
            <Form name={'invoke'} layout={'vertical'} onFinish={onFinish}>
              {data?.isUpload ? (
                <Form.Item label="上传xlsx文件">
                  <Form.Item name="file" noStyle>
                    <Upload.Dragger name="file" action="">
                      <p className="ant-upload-drag-icon">
                        <InboxOutlined/>
                      </p>
                      <p className="ant-upload-text">Click or drag file to this area to upload</p>
                      <p className="ant-upload-hint">Support for a single or bulk upload.</p>
                    </Upload.Dragger>
                  </Form.Item>
                </Form.Item>
              ) : null}

              <Form.Item label="输入请求参数" name={'userRequestBody'}>
                <Input.TextArea size={"large"} autoSize placeholder="请输入请求参数"/>
              </Form.Item>
              <Form.Item>
                <Button type="primary" htmlType="submit">
                  调用
                </Button>
              </Form.Item>
            </Form>
          </Card>

          <Card title={'调用结果'} loading={invokeLoading}>
            <Input.TextArea size={"large"} placeholder="等待结果返回" autoSize value={invokeRes}/>
            <Divider/>
            <Form name={'download'} layout={'vertical'} onFinish={download}>
              <Form.Item>
                {invokeRes ? (
                  <Button type="primary" htmlType="submit">
                    下载
                  </Button>
                ) : null}
              </Form.Item>
            </Form>
          </Card>
        </Col>
      </Row>
    </PageContainer>
  );
};

export default InterfaceInfo;
