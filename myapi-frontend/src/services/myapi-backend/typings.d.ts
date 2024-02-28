declare namespace API {
  type BaseResponse = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseInt_ = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseInterfaceInfo_ = {
    code?: number;
    data?: InterfaceInfo;
    message?: string;
  };

  type BaseResponseListInterfaceInfoVO_ = {
    code?: number;
    data?: InterfaceInfoVO[];
    message?: string;
  };

  type BaseResponseLoginUserVO_ = {
    code?: number;
    data?: LoginUserVO;
    message?: string;
  };

  type BaseResponseLong_ = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponsePageInterfaceInfo_ = {
    code?: number;
    data?: PageInterfaceInfo_;
    message?: string;
  };

  type BaseResponsePagePostVO_ = {
    code?: number;
    data?: PagePostVO_;
    message?: string;
  };

  type BaseResponsePageUser_ = {
    code?: number;
    data?: PageUser_;
    message?: string;
  };

  type BaseResponsePageUserInterfaceInfo_ = {
    code?: number;
    data?: PageUserInterfaceInfo_;
    message?: string;
  };

  type BaseResponsePageUserVO_ = {
    code?: number;
    data?: PageUserVO_;
    message?: string;
  };

  type BaseResponsePostVO_ = {
    code?: number;
    data?: PostVO;
    message?: string;
  };

  type BaseResponseString_ = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponseUser_ = {
    code?: number;
    data?: User;
    message?: string;
  };

  type BaseResponseUserInterfaceInfo_ = {
    code?: number;
    data?: UserInterfaceInfo;
    message?: string;
  };

  type BaseResponseUserVO_ = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type checkUsingGETParams = {
    /** echostr */
    echostr?: string;
    /** nonce */
    nonce?: string;
    /** signature */
    signature?: string;
    /** timestamp */
    timestamp?: string;
  };

  type DeleteRequest = {
    id?: string;
  };

  type downloadFileUsingGETParams = {
    /** filePath */
    filePath: string;
  };

  type getInterfaceInfoVOByIdUsingGETParams = {
    /** id */
    id?: string;
  };

  type getPostVOByIdUsingGETParams = {
    /** id */
    id?: string;
  };

  type getUserByIdUsingGETParams = {
    /** id */
    id?: string;
  };

  type getUserInterfaceInfoVOByIdUsingGETParams = {
    /** id */
    id?: string;
  };

  type getUserVOByIdUsingGETParams = {
    /** id */
    id?: string;
  };

  type IdRequest = {
    id?: string;
  };

  type InputStream = true;

  type InterfaceInfo = {
    createTime?: string;
    description?: string;
    host?: string;
    id?: string;
    isDelete?: number;
    isDownload?: number;
    isUpload?: number;
    method?: string;
    name?: string;
    parameterType?: string;
    path?: string;
    protocol?: string;
    requestBody?: string;
    requestHeader?: string;
    responseHeader?: string;
    status?: number;
    updateTime?: string;
    userId?: string;
  };

  type InterfaceInfoAddRequest = {
    description?: string;
    host?: string;
    method?: string;
    name?: string;
    parameterType?: string;
    path?: string;
    protocol?: string;
    requestBody?: string;
    requestHeader?: string;
    responseHeader?: string;
    userId?: string;
  };

  type InterfaceInfoQueryRequest = {
    current?: string;
    description?: string;
    host?: string;
    id?: string;
    method?: string;
    name?: string;
    pageSize?: string;
    parameterType?: string;
    path?: string;
    protocol?: string;
    requestBody?: string;
    requestHeader?: string;
    responseHeader?: string;
    sortField?: string;
    sortOrder?: string;
    status?: number;
    userId?: string;
  };

  type InterfaceInfoUpdateRequest = {
    description?: string;
    host?: string;
    id?: string;
    method?: string;
    name?: string;
    parameterType?: string;
    path?: string;
    protocol?: string;
    requestBody?: string;
    requestHeader?: string;
    responseHeader?: string;
    status?: number;
  };

  type InterfaceInfoVO = {
    createTime?: string;
    description?: string;
    host?: string;
    id?: string;
    isDelete?: number;
    isDownload?: number;
    isUpload?: number;
    leftNum?: number;
    method?: string;
    name?: string;
    parameterType?: string;
    path?: string;
    protocol?: string;
    requestBody?: string;
    requestHeader?: string;
    responseHeader?: string;
    status?: number;
    totalNum?: number;
    updateTime?: string;
    userId?: string;
  };

  type invokeInterfaceInfoUsingPOSTParams = {
    id?: string;
    userId?:string;
    userRequestBody?: string;
  };

  type loginByGiteeUsingGETParams = {
    /** code */
    code?: string;
    /** state */
    state?: string;
  };

  type loginByGithubUsingGETParams = {
    /** code */
    code?: string;
    /** state */
    state?: string;
  };

  type LoginUserVO = {
    accessKey?: string;
    createTime?: string;
    id?: string;
    secretKey?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type OrderItem = {
    asc?: boolean;
    column?: string;
  };

  type PageInterfaceInfo_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: InterfaceInfo[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PagePostVO_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: PostVO[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PageUser_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: User[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PageUserInterfaceInfo_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: UserInterfaceInfo[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PageUserVO_ = {
    countId?: string;
    current?: string;
    maxLimit?: string;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: string;
    records?: UserVO[];
    searchCount?: boolean;
    size?: string;
    total?: string;
  };

  type PostAddRequest = {
    content?: string;
    tags?: string[];
    title?: string;
  };

  type PostEditRequest = {
    content?: string;
    id?: string;
    tags?: string[];
    title?: string;
  };

  type PostFavourAddRequest = {
    postId?: string;
  };

  type PostFavourQueryRequest = {
    current?: string;
    pageSize?: string;
    postQueryRequest?: PostQueryRequest;
    sortField?: string;
    sortOrder?: string;
    userId?: string;
  };

  type PostQueryRequest = {
    content?: string;
    current?: string;
    favourUserId?: string;
    id?: string;
    notId?: string;
    orTags?: string[];
    pageSize?: string;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: string;
  };

  type PostThumbAddRequest = {
    postId?: string;
  };

  type PostUpdateRequest = {
    content?: string;
    id?: string;
    tags?: string[];
    title?: string;
  };

  type PostVO = {
    content?: string;
    createTime?: string;
    favourNum?: number;
    hasFavour?: boolean;
    hasThumb?: boolean;
    id?: string;
    tagList?: string[];
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    user?: UserVO;
    userId?: string;
  };

  type Resource = {
    description?: string;
    file?: string;
    filename?: string;
    inputStream?: InputStream;
    open?: boolean;
    readable?: boolean;
    uri?: string;
    url?: string;
  };

  type uploadFileUsingPOSTParams = {
    biz?: string;
  };

  type User = {
    accessKey?: string;
    createTime?: string;
    id?: string;
    isDelete?: number;
    secretKey?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserAddRequest = {
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type UserInterfaceInfo = {
    createTime?: string;
    id?: string;
    interfaceInfoId?: string;
    isDelete?: number;
    leftNum?: number;
    name?: string;
    status?: number;
    totalNum?: number;
    updateTime?: string;
    userId?: string;
  };

  type UserInterfaceInfoAddRequest = {
    interfaceInfoId?: string;
    leftNum?: number;
    totalNum?: number;
    userId?: string;
  };

  type UserInterfaceInfoQueryRequest = {
    current?: string;
    id?: string;
    interfaceInfoId?: string;
    leftNum?: number;
    pageSize?: string;
    sortField?: string;
    sortOrder?: string;
    status?: number;
    totalNum?: number;
    userId?: string;
  };

  type UserInterfaceInfoUpdateRequest = {
    id?: string;
    leftNum?: number;
    status?: number;
    totalNum?: number;
  };

  type UserLoginRequest = {
    userAccount?: string;
    userPassword?: string;
  };

  type UserQueryRequest = {
    current?: string;
    id?: string;
    pageSize?: string;
    sortField?: string;
    sortOrder?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserRegisterRequest = {
    checkPassword?: string;
    userAccount?: string;
    userPassword?: string;
  };

  type UserUpdateMyRequest = {
    userAccount?: string;
    userAvatar?: string;
    userCheckPassword?: string;
    userCurrentPassword?: string;
    userName?: string;
    userNewPassword?: string;
    userProfile?: string;
  };

  type UserUpdateRequest = {
    id?: string;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserVO = {
    createTime?: string;
    id?: string;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };
}
