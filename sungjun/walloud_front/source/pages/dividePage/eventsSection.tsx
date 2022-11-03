import React, { useState } from "react";
import { Link, useParams } from "react-router-dom";
import moment from "moment-timezone";
import { useLocation } from "react-router-dom";
import { css } from "@emotion/react";
import { FontSize } from "../../layout/globalStyle/globalSize";
import { useResetRecoilState, useSetRecoilState } from "recoil";
import { eventListState } from "../../recoils/travel";
import Color from "../../layout/globalStyle/globalColor";
import EventsDetail from "./eventsDetail"

const EventsSectionStyle = css`
    border: 2px solid white;
    min-height: 70vh;
    display: flex;
    flex-direction: column;
    &>div{
        position: relative;
        display: flex;
        &:first-child{
        font-size: ${FontSize.fs18};
        padding: 10px 10px;
        }
        &:nth-child(2){
            border-bottom: 2px solid white;
            &>span>a {
                color: white;
                font-weight: 600;
                &:hover {
                    color: ${Color.gray05};
                    cursor: pointer;
                }
            }
        }
        &>span{
            font-size: ${FontSize.fs12};
            margin: 2% 2%;
            text-align: right;
            white-space: nowrap;
            padding-right: 10px;
            &:nth-child(-n+3){
                width: 30%;
            }
            &:nth-child(n+4){
                width: 20%;
            }
        }
    }
`

function EventsSection(eventList: any[]) {
    const setEventList = useSetRecoilState(eventListState);

    function sortEvent(a: any, b: any, attri: string){
        if (a[attri] > b[attri])
            return 1
        if (a[attri] < b[attri])
            return -1
        else 
            return 0
    }

    return (
        <div css = {EventsSectionStyle}>
            <div> 
                Event 
            </div>
            <div>
                <div></div>
                <span>
                    <a onClick ={() => {setEventList([...eventList].sort(
                        (a, b) => sortEvent(a, b, "name")))}}>
                    Name</a>
                </span>
                <span><a onClick ={() => {setEventList([...eventList].sort(
                    (a, b) => sortEvent(b, a, "price")));}}>
                    Price</a>
                </span>
                <span><a onClick ={() => {setEventList([...eventList].sort(
                    (a, b) => sortEvent(a, b, "payerName")));}}>
                    Payer</a>
                </span>
                <span><a onClick ={() => {setEventList([...eventList].sort(
                    (a, b) => sortEvent(a, b, "date")))}}>
                    Date</a>
                </span>
            </div>
            {eventList.map((event, idx) => (
                EventsDetail(event, idx)
                ))}
        </div>
    )   
}

export default EventsSection;
