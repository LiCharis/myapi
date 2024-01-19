import { ProColumns, ProTable } from '@ant-design/pro-components';
import { Modal } from 'antd';
import React from 'react';

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<API.RuleListItem>;

export type Props = {
  column: ProColumns<API.InterfaceInfo>[];
  onCancel: () => void;
  onSubmit: (values: API.InterfaceInfo) => boolean;
  visible: boolean;
};

const CreateModel: React.FC<Props> = (props) => {
  const { column, visible, onSubmit, onCancel } = props;
  //获取并执行外层传来的参数
  return (
    <Modal
      visible={visible}
      onCancel={() => {
        onCancel?.();
      }}
      footer={null}
    >
      <ProTable
        type="form"
        columns={column}
        onSubmit={async (values) => {
          onSubmit?.(values);
        }}
      />
    </Modal>
  );
};

export default CreateModel;
