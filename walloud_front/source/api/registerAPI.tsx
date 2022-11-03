import axios from "axios"
import { LoginAPIProps } from "./loginAPI";

interface RegisterAPIProps {
  userAuth: LoginAPIProps;
  name: string;
  account: string;
  bank: string;
}


const RegisterAPI = async ({userAuth, name, account, bank}: RegisterAPIProps) => {
    await axios
      .post("/api/register", null, { params: {
        Name: name,
        Email: userAuth.email,
        Password: userAuth.password,
        Account: account,
        Bank: bank
    }})
      .then((response) => {
        console.log(response)
        userAuth.setLogined(true)
        userAuth.setUser({
          id: response.data.userId, 
          name: name,
          account: account, 
          email: userAuth.email, 
          bank: bank
        })
        userAuth.path("/")
      })
      .catch((error) => {
        if (error.response.data.status === 500) {
          alert(error.response.data.message);
        }
      });
  };

  export default RegisterAPI;