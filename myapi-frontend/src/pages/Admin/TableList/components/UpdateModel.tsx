import { ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import { Modal } from 'antd';
import React, {useEffect, useRef} from 'react';

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<API.RuleListItem>;

export type Props = {
  values: API.InterfaceInfo;
  column: ProColumns<API.InterfaceInfo>[];
  onCancel: () => void;
  onSubmit: (values: API.InterfaceInfo) => Promise<void>;
  visible: boolean;
};

const UpdateModel: React.FC<Props> = (props) => {
  const {values, column, visible, onSubmit, onCancel } = props;

  const formRef = useRef<ProFormInstance>();

  // 监听点击传入的所要修改接口信息values变化，以做到修改表单初始值跟所需要修改的数据一致
  useEffect(() => {
    formRef.current?.setFieldsValue(values);
  }, [values]);

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
        formRef={formRef}
        columns={column}
        onSubmit={async (values) => {
          onSubmit?.(values);
        }}
      />
    </Modal>
  );
};

export default UpdateModel;
