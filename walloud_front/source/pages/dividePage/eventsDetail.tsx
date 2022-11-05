import React, { useState } from "react";
import moment, { calendarFormat } from "moment-timezone";
import GetEventPartiAPI from "../../api/getEventPartiAPI"
import { css } from "@emotion/react";
import Color from "../../layout/globalStyle/globalColor";
import { FontSize } from "../../layout/globalStyle/globalSize";
import { SetterOrUpdater, useRecoilValue } from "recoil";
import { eventListState, EventProps } from "../../recoils/travel";
import DeleteEventAPI from "../../api/deleteEventAPI";

function EventsDetail(event: any, idx: number, travelId: number,
    eventList: EventProps[], setEventList: SetterOrUpdater<EventProps[]>) {
    const EventsRowStyle = css`
        &>a {
            position: absolute;
            width: 100%;
            height: ${event.isDetail ? "calc(100% + 100px)" : "100%"};
            z-index: 100;
            transition: ease 1s;
            &:hover {
                border: 1px solid ${Color.gray01};
                cursor: pointer;
            }
        &>div {
            display: block;
        }
    }
    `

    const EventsDetailStyle = css`
        flex-direction: row-reverse;
        height: ${event.isDetail ? "100px" : "0"};
        width: 100%;
        transition: ease 0.8s;
        visibility: ${event.isDetail ? "visible" : "hidden"};
        &>div {
            text-align: center;
            transition: ease 0.3s;
            border-radius: 5px;
            border: 1px solid ${Color.gray01};
            margin: 10px 10px;
            padding: 10px 10px;
            justify-items: center;
            display: flex;
            font-size: ${FontSize.fs12};
            &>span {
                margin: 10px 10px;
                font-size: ${FontSize.fs10};
            }
        }
        &>button {
            z-index: 1000;
            float: right;
            padding: 0 5px;
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
    `

    return (
        <>
            <div key={idx} className='event-row' css={EventsRowStyle}>
                <a onClick={() => {
                    GetEventPartiAPI(event.eventId, eventList, setEventList)
                }}>
                </a>
                <span>{event.name}</span>
                <span>{event.price.toLocaleString()}₩</span>
                <span>{event.payerName}</span>
                <span>{moment.tz(event.date, "Asia/Seoul").format().substring(5, 10)}</span>
            </div>
            <div css={EventsDetailStyle}>
                <div>Detail {event.partiList.map((participant: any, idx: number) => (
                    <>
                        <span key={idx}>
                            <div>{participant.name}</div>
                            <div>{participant.chargedPrice.toLocaleString()}₩</div>
                        </span>
                    </>
                ))}</div>
                <button onClick={() => DeleteEventAPI(travelId, event.eventId, eventList, setEventList)}>
                    <img alt="delete" src="/source/assets/icon/delete.svg" />
                </button>
            </div>
        </>
    )
}

export default EventsDetail;

/*
        
*/