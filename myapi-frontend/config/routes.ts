export default [
  { path: '/', name: '接口信息', icon: 'smile', component: './Index' },
  {
    path: '/interface_info/:id',
    name: '接口信息信息',
    icon: 'smile',
    component: './InterfaceInfo',
    hideInMenu: true,
  },
  {
    path: '/user',
    layout: false,
    routes: [{ path: '/user/login', component: './User/Login' }],
  },
  {
    path: '/admin',
    name: '管理中心',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { icon: 'table', path: '/admin/list', name: '接口管理', component: './Admin' },
      {
        icon: 'table',
        path: '/admin/analyse',
        name: '接口分析',
        component: './InterfaceInfoAnalyse',
      },
    ],
  },
  {
    path: '/userManager',
    name: '个人中心',
    icon: 'crown',
    routes: [
      {
        path: '/userManager/personalInvoke',
        name:'接口调用情况',
        component: './User/Personal/Invoke',
      },
      {
        path: '/userManager/personalKeys',
        name:'密钥信息',
        component: './User/Personal/Keys',
      },
    ],
  },
  { path: '*', layout: false, component: './404' },
];
