import { css } from '@emotion/react'
import PageContainer from '../../layout/container/pageContainer';
import MobileContainer from '../../layout/container/mobileContainer';
import { useState } from 'react';
import RegisterAPI from '../../api/registerAPI';
import InputContainer from '../../layout/container/inputContainer';
import SignInput from '../../component/input/signInput';
import SellectInput from '../../component/input/sellectInput';
import { BankList } from '../../storage/bankList';
import BasicButton from '../../component/button/basicButton';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { LoginedState, userState } from '../../recoils/user';
import Logo from '../../component/image/logo';
import Color from '../../layout/globalStyle/globalColor';
import { FontSize } from '../../layout/globalStyle/globalSize';

const catchPharseStyle = css`
    text-align: center;
    font-size: ${FontSize.fs16};
    color: white;
    padding-bottom: 30px;
    &>div {
        padding-top: 10px;
    }
`
const registerButtonStyle = css`
    &>button {
        width: 314px;
        border-radius: 5px;
    }
`

function SignUpPage() {
  const [name, SetName] = useState("");
  const [email, SetEmail] = useState("");
  const [password, SetPassword] = useState("");
  const [account, SetAccount] = useState("");
  const [bank, SetBank] = useState("");
  const [valid, SetValid] = useState(false);
  const setLogined = useSetRecoilState(LoginedState);
  const setUser = useSetRecoilState(userState);
  const path = useNavigate();

  return (
    <MobileContainer>
      <div css = {catchPharseStyle}>
        <div>똑똑한<span css = {{color: `${Color.blue05}`, fontWeight: 700}} > 소비</span>에</div>
        <div>지금 함께하세요</div>
      </div>
      {Logo('80px', 'invert()')}
      <InputContainer>
        <SignInput name = {name} text = "ex) 홍길동" setType = {SetName} message = "name" required = {true}/>
        <SignInput name = {email} text = "ex) mywalloud@usage.com" setType = {SetEmail} message = "email" required = {true}/>
        <SignInput name = {password} text = "영문, 숫자, 특수문자 중 2종류 조합: 8~16자" setType = {SetPassword} message = "password" required = {true}/>
        <SignInput name = {account} text = "ex) 1004365828210" setType = {SetAccount} message = "account" required = {false}/>
        <SellectInput typeList = {BankList.map((bank, idx) => {
          return bank.name;
        })} setType = {SetBank} />
      </InputContainer>
      <div css = {registerButtonStyle}>
        <BasicButton text = "가입하기" onClick = {() =>
        RegisterAPI({userAuth: {email, password, setLogined, setUser, path}, name, account, bank})} />
      </div>
    </MobileContainer>
  );
}

export default SignUpPage;