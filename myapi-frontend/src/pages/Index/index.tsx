import { listInterfaceInfoVoByPageUsingPost } from '@/services/myapi-backend/interfaceController';
import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import { List, message } from 'antd';
import React, { useEffect, useState } from 'react';

/**
 * 主页
 * @constructor
 */

const Index: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<API.InterfaceInfo[]>([]);
  const [total, setTotal] = useState<number>(0);

  const loadData = async (page = 1, pageSize = 8) => {
    setLoading(true);
    const res = await listInterfaceInfoVoByPageUsingPost({ current: page, pageSize: pageSize });
    try {
      if (res.code === 0) {
        setList(res?.data?.records ?? []);
        setTotal(Number(res?.data?.total) ?? 0);
        console.log(typeof total);
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

  return (
    <PageContainer title="查看所有上线的接口信息">
      <List
        className="interface-list"
        loading={loading}
        itemLayout="horizontal"
        dataSource={list}
        renderItem={(item) => {
          const apiLink = item.status == 1 ? `/interface_info/${item.id}` : "#";
          return (
            <List.Item actions={[<h3 key={item.id} >{item.status?"可用":"不可用"}</h3>]}>
              <List.Item.Meta
                title={<h2><a href={apiLink}> {item.name}</a></h2>}
                description={<h3>{item.description}</h3>}
              />
            </List.Item>
          );
        }}
        pagination={{
          showTotal() {
            return '总数' + total;
          },
          total,
          pageSize: 8,
          onChange(page, pageSize) {
            loadData(page, pageSize);
          },
        }}
      />
    </PageContainer>
  );
};

export default Index;
