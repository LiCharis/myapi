import { message } from 'antd';
import {loginByGiteeUsingGet} from "@/services/myapi-backend/loginController";

export const Oauth = {
  getGiteeCode() {
    const authorize_uri = 'https://gitee.com/oauth/authorize';
    const client_id = '27853d6fc405e7f3f1be5710672a70845e0981ba516a61dde76bb6340773b677';
    const redirect_uri = 'http://localhost:8101/api/login3rd/gitee/callback';
    location.href = `${authorize_uri}?client_id=${client_id}&redirect_uri=${redirect_uri}&response_type=code&state=GITEE`;
  },
  getGithubCode() {
    const authorize_uri = 'https://github.com/login/oauth/authorize';
    const client_id = '81281fded19e878ed5f1';
    const redirect_uri = 'http://localhost:8101/api/login3rd/github/callback';
    location.href = `${authorize_uri}?client_id=${client_id}&redirect_uri=${redirect_uri}&scope=user&state=GITHUB`;
  },

};

export default Oauth;
