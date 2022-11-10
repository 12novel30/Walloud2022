import { css } from '@emotion/react'
import { SetterOrUpdater, useRecoilState, useSetRecoilState } from 'recoil'
import FilpCard from '../../animation/flipCard';
import GetPersonDetailAPI from '../../api/getPersonDetailAPI';
import Color from '../../layout/globalStyle/globalColor';
import { FontSize } from '../../layout/globalStyle/globalSize';
import { currentTravelState, PersonProps } from '../../recoils/travel'

const PersonBoxStyle = css`
    position: relative;
    border-collapse: collapse;
    font-size: ${FontSize.fs12};
    text-align: center;
    &:before {
        content: "";
        display: block;
        padding-top: 100%;
    }
    &>div {
        position: absolute;
        top: 0%;
        left: 0%;
        width: 100%;
        height: 100%;
        &>.front {
            display: flex;
            flex-direction: column;
            border: 1px solid gray;
            border-radius: 10px;
            background-color: ${Color.blueClassic};
            &>a {
                height: 65%;
            }
            &>.info {
                border-top: 1px solid white;
                margin: 0 10%;
                padding-top: 5px;
                text-align: left;
                height: 15%;
                display: flex;
                flex-direction: column;
                gap: 10%;
                &>:first-of-type {
                    font-size: ${FontSize.fs12};
                    &>sub {font-size: ${FontSize.fs08}}
                }
                font-size: ${FontSize.fs10};
            }
            &>.util {
                display: flex;
                height: 15%;
                &>a{
                    margin-left: 80%;
                    cursor: pointer;
                    &>img {
                        width: 24px;
                        filter: invert();
                    }
                }
            }
        }
        &>.back {
            border: 1px solid gray;
            border-radius: 10px;
            display: flex;
            flex-direction: column;
            background-color: ${Color.blue04};
            &>.info {
                margin: 0 10%;
                padding-top: 5px;
                height: 85%;
                text-align: left;
                display: flex;
                flex-direction: column;
                gap: 10%;
                &>:first-of-type {
                    border-bottom: 1px solid white;
                    font-size: ${FontSize.fs12};
                    &>sub {font-size: ${FontSize.fs08}}
                }
                font-size: ${FontSize.fs10};
            }
            &>.util {
                display: flex;
                height: 15%;
                margin-left: 70%;
                &>button {
                    float: right;
                    padding: 0 5%;
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
                        filter: invert();
                    }
                }
            }
        }
    } 
`
function PersonBox(Person: PersonProps, id: number, type: string, travelId: number,
    personList: PersonProps[], setPersonList: SetterOrUpdater<PersonProps[]>){

    return (
        <div css = {PersonBoxStyle} key = {id} className = {type}>
            <FilpCard>
                <div className='front' id = {id.toString() + " front"}>
                    <a>
                        <img src = "Person.image"/>
                    </a>
                    <div className='info'>
                        <div>{Person.name}<sub><i>{type}</i></sub>
                        </div>
                        <div>{Person.difference.toLocaleString()}₩</div>
                    </div>
                    <div className='util'>
                        <a onClick = {() => {
                            var front = document.getElementById(id.toString() + " front");
                            front.style.transform = "rotateY(180deg)"
                            var back = document.getElementById(id.toString() + " back")
                            back.style.transform = "rotateY(0deg)"
                            GetPersonDetailAPI(travelId, id, personList, setPersonList);
                        }}>
                            <img src ="/source/assets/icon/details.svg" />
                        </a>
                    </div>
                </div>
                <div className= 'back' id = {id.toString() + " back"}>
                    <div className='info'>
                        <div>{Person.name}<sub><i>{type}</i></sub></div>
                        <div>소비 총액: {Person.detail.sumSend.toLocaleString()}₩</div> {/*여기 오른쪽에 ? icon을 놓고 hover 하면 참여한 이벤트 목록들 보여주면 어떨까? spent 와 함께 영수증 느낌?*/}
                        <div>지출 총액: {Person.detail.sumGet.toLocaleString()}₩</div>
                        <div>계좌: {Person.detail.userBank} {Person.detail.userAccount}</div>
                    </div>
                    <div className='util'>
                        <button onClick = {() => {}
                        }>
                            <img alt = "delete" src = "/source/assets/icon/delete.svg" />
                        </button>
                        <button onClick = {() => {
                            var back = document.getElementById(id.toString() + " back")
                            back.style.transform = "rotateY(-180deg)"
                            var front = document.getElementById(id.toString() + " front");
                            front.style.transform = "rotateY(0deg)"
                        }}>
                            <img alt = "return" src = "/source/assets/icon/return.svg" />
                        </button>
                        {/*정산 여부 (check icon) -> only display for Manager Account, user의 마이페이지로 들어가는 버튼(연결된 계정이 있을시)*/}
                    </div>
                </div>
            </FilpCard>
        </div>
    )
}

export default PersonBox;