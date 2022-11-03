import {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { css } from '@emotion/react'
import PageContainer from '../../layout/container/pageContainer';
import MobileContainer from '../../layout/container/mobileContainer';
import SignInput from '../../component/input/signInput';
import InputContainer from '../../layout/container/inputContainer';
import LoginAPI from '../../api/loginAPI';
import BasicButton from '../../component/button/basicButton';
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil';
import { LoginedState, userState } from '../../recoils/user';
import React from 'react';
import Logo from '../../component/image/logo';
import { FontSize } from '../../layout/globalStyle/globalSize';
import Color from '../../layout/globalStyle/globalColor';

const catchPharseStyle = css`
    text-align: center;
    font-size: ${FontSize.fs16};
    color: white;
    padding-bottom: 30px;
    &>div {
        padding-top: 10px;
    }
`

const loginButtonStyle = css`
    &>button {
        width: 314px;
        border-radius: 5px;
    }
    &>div {
        padding-top: 15px;
        width: 314px;
        text-align: center;
        &>span{
            padding: 0 5px;
        }
    }
`

function SigninPage() {
    const [email, SetEmail] = useState("");
    const [password, SetPassword] = useState("");
    const setLogined = useSetRecoilState(LoginedState);
    const setUser = useSetRecoilState(userState);
    const path = useNavigate();

    return (
        <MobileContainer>
            <div css = {catchPharseStyle}>
                <div><span css = {{color: `${Color.blue05}`, fontWeight: 700}} >일상</span>을 그리는</div>
                <div>똑똑한 소비</div>
            </div>
            {Logo('80px', 'invert()')}
            <InputContainer>
                <SignInput name = {email} text = "ex) mywalloud@usage.com" setType = {SetEmail} message = "email" required = {false}/>
                <SignInput name = {password} text = "영문, 숫자, 특수문자 중 2종류 조합: 8~16자" setType = {SetPassword} message = "password" required = {false}/>
            </InputContainer>
            <div css = {loginButtonStyle}>
                <BasicButton text = "로그인" onClick = {() => 
                LoginAPI({email, password, setUser, setLogined, path})} />
                <div>
                    <span>비밀번호 찾기 </span>
                    <span>|</span>
                    <span> 회원가입하기</span>
                </div>
            </div>
        </MobileContainer>
  );
}

export default SigninPage;