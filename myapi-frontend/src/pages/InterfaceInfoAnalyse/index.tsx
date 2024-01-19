import { listTopInvokeInterfaceInfoUsingGet } from '@/services/myapi-backend/interfaceInfoAnalyseController';
import { PageContainer } from '@ant-design/pro-components';
import {Divider, message} from 'antd';
import EChartsReact from 'echarts-for-react';
import React, { useEffect, useState } from 'react';

const InterfaceInfoAnalyse: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [data, setData] = useState<API.InterfaceInfoVO[]>([]);

  const loadData = async () => {
    const res = await listTopInvokeInterfaceInfoUsingGet();
    try {
      if (res.code === 0) {
        // @ts-ignore
        setData(res?.data);
      }
    } catch (error: any) {
      message.error('接口分析数据获取失败');
    }
    setLoading(false);
  };

  /**
   * 获取数据
   */
  useEffect(() => {
    loadData();
  }, []);

  /**
   * 数据映射，变成echarts可识别的格式
   */
  const pieData = data.map((item) => {
    return {
      name: item.name,
      value: item.totalNum,
    };
  });

  const pie = {
    title: {
      text: '调用次数前三的接口',
      left: 'center',
    },
    tooltip: {
      trigger: 'item',
    },
    legend: {
      orient: 'vertical',
      left: 'left',
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: '50%',
        data: pieData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  };

  /**
   * 数据映射，变成echarts可识别的格式
   */
  const barData = data.map((item) => {
    return [item.name, item.totalNum];
  });

  const bar = {
    dataset: [
      {
        dimensions: ['name', 'totalNum'],
        source: barData,
      },
      {
        transform: {
          type: 'sort',
          config: { dimension: 'totalNum', order: 'desc' },
        },
      },
    ],
    xAxis: {
      type: 'category',
      axisLabel: { interval: 0, rotate: 30 },
    },
    yAxis: {},
    series: {
      type: 'bar',
      encode: { x: 'name', y: 'totalNum' },
      datasetIndex: 1,
    },
  };

  return (
    <PageContainer>
      <EChartsReact option={pie} showLoading={loading} />
      <Divider/>
      <EChartsReact option={bar} showLoading={loading} />
    </PageContainer>
  );
};

export default InterfaceInfoAnalyse;
