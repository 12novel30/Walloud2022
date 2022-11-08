import { css } from '@emotion/react'
import { useEffect, useState } from 'react';
import { SetterOrUpdater } from 'recoil'
import Color from '../../layout/globalStyle/globalColor';
import { FontSize } from '../../layout/globalStyle/globalSize';
import DeleteTravelAPI from '../../api/deleteTravelAPI'
import FilpCard from '../../animation/flipCard';
import UploadImageButton from "../button/uploadImageButton";

const TravelBoxStyle = css`
    height: 240px;
    width: 200px;
    min-width: 150px;
    max-width: 200px;
    border-radius: 15px;
    display: flex;
    flex-direction: column;
    background-color: white;
    &>a {
        height: 70%;
        width: 100%;
        display: block;
        font-size: 20px;
        color: black;
        background-color: ${Color.blue05};
        border-radius: 15px 15px 0 0;
        & > button {
            width: 30px;
            border-radius: 2rem;
        }
    }
    &> input {
    display: none;
    }
    &>div {
        height: 30%;
        width: 100%;
        perspective: 200px;
        &>div {
            font-size: ${FontSize.fs14};
            &>div {
                padding: 7px 10px;
            }
            &>input {
                margin: 3% 0% 3% 3%;
                width: 90%;
                height: 30%;
                font-size: ${FontSize.fs10};
                border-radius: 0.5rem;
                border-style: solid;
                border-color: ${Color.blue02};
                text-align: center;
            }
            &>button {
                float: right;
                padding: 0 5px;
                background-color: transparent;
                border: none;
                z-index: 10000;
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

function TravelBox(
    travelName: string, 
    id: number, 
    setCurrentTravel: SetterOrUpdater<number>,
    onClickEdit: {(id: number): void; (arg0: number): void},
    isEditMode: number | null){

    const name = travelName;

    return (
        <div css = {TravelBoxStyle} key = {id}>
            <a href = {`/travel/${name}`} onClick = {() => {setCurrentTravel(id)}} />
            {/*<div id={`${id}-image`}></div>*/}
            <FilpCard>
                <div className = 'front' id = {id.toString() + " front"}>
                    <div>{name}</div>
                    <button onClick = {() => {
                        var front = document.getElementById(id.toString() + " front");
                        front.style.transform = "rotateY(180deg)"
                        var back = document.getElementById(id.toString() + " back")
                        back.style.transform = "rotateY(0deg)"
                    }}>
                        <img src = "source/assets/icon/menu-vertical.svg" />
                    </button>
                </div>
                <div className = 'back' id = {id.toString() + " back"}>
                    <UploadImageButton id = {id} /> {/*CSS control Needed*/}
                    {isEditMode === id ? (
                    <input
                        placeholder={name}
                        id={`${id}-input-name`}
                        defaultValue={name}
                        autoFocus
                        onKeyDown={(event) =>
                        event.key == "Enter" ? onClickEdit(id) : null
                    }
                    />
                    ) : (
                        <div id={`${id}-back-name`}>{name}</div>
                    )}
                    <button onClick = {() => {
                            var front = document.getElementById(id.toString() + " front");
                            front.style.transform = "rotateY(0deg)"
                            var back = document.getElementById(id.toString() + " back")
                            back.style.transform = "rotateY(-180deg)"
                        }}>
                        <img alt = "return" src = "source/assets/icon/return.svg" />
                    </button>
                    <button onClick = {() => DeleteTravelAPI(id)
                    }>
                        <img alt = "delete" src = "source/assets/icon/delete.svg" />
                    </button>
                    <button onClick={() => onClickEdit(id)}>
                        <img alt = "edit" src = "source/assets/icon/edit.svg" />
                    </button>
                </div>
            </FilpCard>
        </div>
    )
}

export default TravelBox;