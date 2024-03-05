import { listUserInterfaceInfoVoByPageUsingPost } from '@/services/myapi-backend/userInterfaceController';
import { useModel } from '@@/exports';
import { PageContainer } from '@ant-design/pro-components';
import { List, message } from 'antd';
import moment from 'moment';
import React, { useEffect, useState } from 'react';

const PersonalInvoke: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const loginUser = initialState?.loginUser;
  console.log(loginUser);
  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<API.UserInterfaceInfo[]>([]);
  const [total, setTotal] = useState<number>(0);

  const loadData = async (page: number, pageSize: number) => {
    setLoading(true);
    const res = await listUserInterfaceInfoVoByPageUsingPost({
      userId: loginUser?.id,
      sortField:"updateTime",

    });
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

  const info = '用户 ' + loginUser?.userName + ' 的接口调用情况';

  return (
    <PageContainer title={info}>
      <List
        className="interface-list"
        loading={loading}
        itemLayout="horizontal"
        dataSource={list}
        renderItem={(item) => {
          const apiLink = item.status == 1 ? `/interface_info/${item.interfaceInfoId}` : "javascript:alert('接口不可用')";
          const color = item.status == 1 ? 'blue' : 'grey';
          return (
            <List.Item
              actions={[
                <h3 key={item.id}>{moment(item.updateTime).format('YYYY-MM-DD HH:mm:ss')}</h3>,
              ]}
            >
              <List.Item.Meta
                title={
                  <h2>
                    <a style={{color:color}} href={apiLink}> {item.name}</a>
                  </h2>
                }
                description={
                <>
                  <h3 style={{color:'#434343'}}>{'已调用次数: ' + item.totalNum}</h3>
                  <h3 style={{color:'#434343'}}>{'剩余次数: ' + item.leftNum}</h3>
                </>
                }
              />
            </List.Item>
          );
        }}
        pagination={{
          showTotal() {
            return '总数' + total;
          },
          total,
          defaultCurrent: 1,
          defaultPageSize: 8,
          onChange(page, pageSize) {
            loadData(page, pageSize);
          },
        }}
      />
    </PageContainer>
  );
};

export default PersonalInvoke;
