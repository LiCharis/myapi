import CreateModel from '@/pages/Admin/TableList/components/CreateModel';
import {
  addInterfaceInfoUsingPost,
  deleteInterfaceInfoUsingPost,
  listInterfaceInfoVoByPageUsingPost,
  offlineInterfaceInfoUsingPost,
  onlineInterfaceInfoUsingPost,
  updateInterfaceInfoUsingPost,
} from '@/services/myapi-backend/interfaceController';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns, ProDescriptionsItemProps} from '@ant-design/pro-components';
import {PageContainer, ProDescriptions, ProTable} from '@ant-design/pro-components';
import {FormattedMessage, useIntl} from '@umijs/max';
import {Button, Drawer, message, Popconfirm} from 'antd';
import {SortOrder} from 'antd/lib/table/interface';
import React, {useRef, useState} from 'react';
import UpdateModel from './TableList/components/UpdateModel';

const TableList: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalOpen, handleModalOpen] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);

  const [showDetail, setShowDetail] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.InterfaceInfo>();
  const [selectRowsState, setSelectedRows] = useState<API.InterfaceInfo[]>([]);

  /**
   * @en-US Add node
   * @zh-CN 添加节点
   * @param fields
   */
  const handleAdd = async (fields: API.InterfaceInfoAddRequest) => {
    const hide = message.loading('正在添加');
    try {
      const res = await addInterfaceInfoUsingPost({...fields});
      hide();
      if (res.code === 0) {
        message.success('创建成功');
        //重新加载
        actionRef.current?.reload();
      }
      handleModalOpen(false);
      return true;
    } catch (error: any) {
      hide();
      message.error('创建失败,' + error.message);
      return false;
    }
  };

  /**
   * @en-US Update node
   * @zh-CN 更新节点
   *
   * @param fields
   */
  const handleUpdate = async (fields: API.InterfaceInfoUpdateRequest) => {
    const hide = message.loading('修改中');
    if (!currentRow?.id) {
      message.error('接口不存在');
      return;
    }
    try {
      const res = await updateInterfaceInfoUsingPost({...fields, id: currentRow.id});
      hide();
      if (res.code === 0) {
        message.success('修改成功');
        //重新加载
        actionRef.current?.reload();
      }

      return true;
    } catch (error: any) {
      hide();
      message.error('修改失败,' + error.message);
      return false;
    }
  };

  /**
   *  Delete node
   * @zh-CN 上线接口
   *
   * @param selectedRows
   */
  const handleOnline = async (record: API.IdRequest) => {
    const hide = message.loading('发布中');
    if (!record) return true;
    try {
      const res = await onlineInterfaceInfoUsingPost({id: record.id});
      hide();
      if (res.code === 0) {
        message.success('上线成功');
        //重新加载
        actionRef.current?.reload();
      }else {
        message.error('上线失败,' + res.message);
      }

      return true;
    } catch (error: any) {
      hide();
      message.error('上线失败,' + error.message);
      return false;
    }
  };

  /**
   *  Delete node
   * @zh-CN 删除节点
   *
   * @param selectedRows
   */
  const handleOffline = async (record: API.IdRequest) => {
    const hide = message.loading('正在下线');
    if (!record) return true;
    try {
      const res = await offlineInterfaceInfoUsingPost({id: record.id});
      hide();
      if (res.code === 0) {
        message.success('下线成功');
        //重新加载
        actionRef.current?.reload();
      }else {
        message.error('下线失败,' + res.message);
      }

      return true;
    } catch (error: any) {
      hide();
      message.error('下线失败,' + error.message);
      return false;
    }
  };

  /**
   *  Delete node
   * @zh-CN 删除节点
   *
   * @param selectedRows
   */
  const handleRemove = async (record: API.InterfaceInfo) => {
    const hide = message.loading('正在删除');
    if (!record) return true;
    try {
      const res = await deleteInterfaceInfoUsingPost({id: record.id});
      hide();
      if (res.code === 0) {
        message.success('删除成功');
        //重新加载
        actionRef.current?.reload();
      }

      return true;
    } catch (error: any) {
      hide();
      message.error('删除失败' + error.message);
      return false;
    }
  };

  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */
  const intl = useIntl();

  const columns: ProColumns<API.InterfaceInfo>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'index',
    },
    {
      title: '接口名称',
      dataIndex: 'name',
      valueType: 'text',
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '描述',
      dataIndex: 'description',
      valueType: 'textarea',
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '请求方法',
      dataIndex: 'method',
      valueType: 'text',
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '通信协议',
      dataIndex: 'protocol',
      valueType: 'text',
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '主机地址',
      dataIndex: 'host',
      valueType: 'text',
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '路径',
      dataIndex: 'path',
      valueType: 'text',
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '请求参数',
      dataIndex: 'requestBody',
      valueType: 'jsonCode',
      hideInTable: true,
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '请求头',
      dataIndex: 'requestHeader',
      valueType: 'jsonCode',
      hideInTable: true,
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '响应头',
      dataIndex: 'responseHeader',
      valueType: 'jsonCode',
      hideInTable: true,
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '总调用次数',
      dataIndex: 'totalNum',
      valueType: 'text',
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '剩余调用次数',
      dataIndex: 'leftNum',
      valueType: 'text',
      formItemProps: {
        rules: [
          {
            required: true,
          },
        ],
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      valueEnum: {
        0: {
          text: '关闭',
          status: 'Default',
        },
        1: {
          text: '开启',
          status: 'Processing',
        },
      },
    },
    {
      title: '是否可下载',
      dataIndex: 'isDownload',
      valueEnum: {
        0: {
          text: '否',
          status: 'Default',
        },
        1: {
          text: '是',
          status: 'Processing',
        },

      }
    },
    {
      title: '是否可上传',
      dataIndex: 'isUpload',
      valueEnum: {
        0: {
          text: '否',
          status: 'Default',
        },
        1: {
          text: '是',
          status: 'Processing',
        },

      }
    },

    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInTable: true,
      hideInForm: true,
    },
    {
      title: '修改时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      hideInTable: true,
      hideInForm: true,
    },
    {
      title: <FormattedMessage id="pages.searchTable.titleOption" defaultMessage="Operating"/>,
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        record.status === 0 ? (
          <Button
            key="config"
            type="default"
            onClick={() => {
              handleOnline(record);
            }}
          >
            上线
          </Button>
        ) : (
          <Button
            key="config"
            type="dashed"
            onClick={() => {
              handleOffline(record);
            }}
          >
            下线
          </Button>
        ),

        <Button
          key="config"
          type="primary"
          onClick={() => {
            handleUpdateModalOpen(true);
            setCurrentRow(record);
          }}
        >
          修改
        </Button>,
        <Popconfirm title="确定删除吗?" onConfirm={() => handleRemove(record)}>
          <Button
            key="config"
            type="primary"
            danger
          >
            删除
          </Button>
        </Popconfirm>

      ],
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.InterfaceInfoQueryRequest, API.PageParams>
        headerTitle={intl.formatMessage({
          id: 'pages.searchTable.title',
          defaultMessage: 'Enquiry form',
        })}
        actionRef={actionRef}
        rowKey="key"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalOpen(true);
            }}
          >
            <PlusOutlined/> <FormattedMessage id="pages.searchTable.new" defaultMessage="New"/>
          </Button>,
        ]}
        /**
         * 返回数据
         **/

        request={async (
          // params: {
          //   pageSize?: number;
          //   current?: number;
          //   keyword?: string;
          // },
          params: API.InterfaceInfoQueryRequest,
          sort: Record<string, SortOrder>,
          filter: Record<string, (string | number)[] | null>,
        ) => {
          const res: any = await listInterfaceInfoVoByPageUsingPost({...params, sortField: "createTime"});
          if (res?.data) {
            return {
              data: res.data.records || [],
              success: true,
              total: res.data.total,
            };
          } else {
            return {
              data: [],
              success: false,
              total: 0,
            };
          }
        }}
        columns={columns}
        // rowSelection={{
        //   onChange: (_, selectedRows) => {
        //     setSelectedRows(selectedRows);
        //   },
        // }}
      />

      <UpdateModel
        column={columns}
        onSubmit={async (values) => {
          const success = await handleUpdate(values);
          /**
           * 这里没有传入id，因此要根据currentRow把id传入(column没有id)
           *    alert(JSON.stringify(values));
           */
          if (success) {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
          if (!showDetail) {
            setCurrentRow(undefined);
          }
        }}
        visible={updateModalOpen}
        values={currentRow || {}}
      />

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.name && (
          <ProDescriptions<API.RuleListItem>
            column={2}
            title={currentRow?.name}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.name,
            }}
            columns={columns as ProDescriptionsItemProps<API.RuleListItem>[]}
          />
        )}
      </Drawer>
      {/*创建接口的表单*/}
      <CreateModel
        column={columns}
        onCancel={() => {
          handleModalOpen(false);
        }}
        onSubmit={(values) => {
          handleAdd(values);
        }}
        visible={createModalOpen}
      />
    </PageContainer>
  );
};

export default TableList;
