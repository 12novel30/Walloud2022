import { css } from '@emotion/react'
import { useRecoilState, useSetRecoilState } from 'recoil'
import FilpCard from '../../animation/flipCard';
import GetPersonDetailAPI from '../../api/getPersonDetailAPI';
import Color from '../../layout/globalStyle/globalColor';
import { FontSize } from '../../layout/globalStyle/globalSize';
import { currentTravelState } from '../../recoils/travel'

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
                    padding-left: 80%;
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
        }
    } 
`
function PersonBox(Person: any, id: number, type: string, travelId: number){

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
                            GetPersonDetailAPI(travelId, id);
                        }}>
                            <img src ="/source/assets/icon/details.svg" />
                        </a>
                    </div>
                </div>
                <div className= 'back' id = {id.toString() + " back"}>
                    뭥미
                </div>
            </FilpCard>
        </div>
    )
}

export default PersonBox;