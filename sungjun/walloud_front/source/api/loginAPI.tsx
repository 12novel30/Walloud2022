import axios from "axios"
import { NavigateFunction } from "react-router-dom";
import { SetterOrUpdater} from "recoil";
import { UserProps, userState } from "../recoils/user";

export interface LoginAPIProps {
  email: string;
  password: string;
  setUser: SetterOrUpdater<UserProps>;
  setLogined: SetterOrUpdater<boolean>;
  path: NavigateFunction;
}

const LoginAPI = async ({email, password, setUser, setLogined, path}: LoginAPIProps) => {
    return axios.post("/api/login", null, { params: {
            Email: email,
            Password: password,
        }})
        .then((response) => {
            console.log(response)
            setLogined(true)
            setUser({id: response.data.userId, 
                name: response.data.name,
                account: response.data.account, 
                email: response.data.email, 
                bank: response.data.bank})
            path("/")
        })
        .catch((error) => {
            if (error.response.data.status === 500) {
              alert(error.response.data.message);
            }
            else {
              alert("Check The network");
            }
        });
}

export default LoginAPI;