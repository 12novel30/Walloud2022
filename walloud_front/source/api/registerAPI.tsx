import axios from "axios";
import { LoginAPIProps } from "./loginAPI";

interface RegisterAPIProps {
  userAuth: LoginAPIProps;
  name: string;
  account: string;
  bank: string;
}

const RegisterAPI = async ({
  userAuth,
  name,
  account,
  bank,
}: RegisterAPIProps) => {
  if (!name || !account || !userAuth.email || !userAuth.password) {
    alert("비어있는 요소가 있습니다!");
    return;
  }
  const data = {
    user_name: name,
    user_email: userAuth.email,
    user_password: userAuth.password,
    user_account: account,
    user_bank: bank,
  };

  await axios
    .post("/api/register", JSON.stringify(data), {
      headers: {
        "Content-Type": "application/json", // application/json 타입 선언
      },
    })
    .then((response) => {
      userAuth.setLogined(true);
      userAuth.setUser({
        id: response.data.userId,
        name: name,
        account: account,
        email: userAuth.email,
        bank: bank,
      });
      userAuth.path("/");
    })
    .catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      }
    });
};

export default RegisterAPI;
