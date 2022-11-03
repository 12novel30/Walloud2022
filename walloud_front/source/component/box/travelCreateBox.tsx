import { css } from '@emotion/react'
import { useState } from 'react';
import Color from '../../layout/globalStyle/globalColor';
import { FontSize } from '../../layout/globalStyle/globalSize';
import CreateTravelAPI from '../../api/createTravelAPI'
import FilpCard from '../../animation/flipCard';
import SignInput from '../input/signInput';
import BasicButton from '../button/basicButton';
import { useRecoilValue } from 'recoil';
import { userState } from '../../recoils/user';

const TravelCreateBoxStyle = css`
    &>div {
        height: 240px;
        width: 200px;
        min-width: 150px;
        max-width: 200px;
        border-radius: 15px;
        &>.front {
            border-radius: 15px;
            display: flex;
            flex-direction: column;
            background-color: white;
            &>a {
                height: 70%;
                width: 100%;
                cursor: pointer;
                &>img {
                    height: 50%;
                    margin: 25%;
                }
            }
            &>div {
                height: 30%;
                font-size: ${FontSize.fs14};
                text-align: center;
            }
        }
        &>.back {
            border-radius: 15px;
            display: flex;
            flex-direction: column;
            gap: 5px;
            background-color: ${Color.gray01};
            &>div {
                padding-top: 20%;
                &>div {
                    color: black;
                    padding-left: 10%;
                }
                &>input {
                    width: 80%;
                }
            }
            &>#basic {
                margin: 2% auto;
                width: 80%;
            } 
            &>#small {
                float: right;
                padding: 0px;
                background-color: transparent;
                border: none;
                &:hover {
                    cursor: pointer;
                }
                &:focus {
                    border: none;
                    outline: none;
                }
                &>img {
                    width: 24px;
                }
            }
        }
    }
`

function TravelCreateBox() {
    const [travelName, setTravelName] = useState<string>("");
    const userId = useRecoilValue(userState).id;

    return (
        <div css = {TravelCreateBoxStyle}>
            <FilpCard>
                <div className = "front" id = "front-create">
                    <a onClick = {() => {
                        var front = document.getElementById("front-create");
                        front.style.transform ="rotateY(180deg)";
                        var back = document.getElementById("back-create");
                        back.style.transform ="rotateY(0deg)";
                    }}>
                        <img src = "source/assets/icon/plus.svg" />
                    </a>
                    <div>
                        만들기
                    </div>
                </div>
                <div className = "back" id = "back-create">
                    <SignInput name = {travelName} text = "ex) 부산 여행"
                        setType = {setTravelName} message = "Travel Name" required = {true}/>
                    <BasicButton text = "여행 만들기" onClick= {() =>
                    {if (travelName !== ""){
                        CreateTravelAPI(userId, travelName)}
                    else {
                        alert("이름을 입력하세요")
                    }}}/>
                    <button id = "small" onClick = {() => {
                        var front = document.getElementById("front-create");
                        front.style.transform = "rotateY(0deg)";
                        var back = document.getElementById("back-create");
                        back.style.transform = "rotateY(-180deg)";
                    }}>
                        <img src = "source/assets/icon/return.svg" />
                    </button>
                </div>
        </FilpCard>
        </div>
    )
}

export default TravelCreateBox;