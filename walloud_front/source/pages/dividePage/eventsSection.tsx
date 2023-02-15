import React, { useState } from "react";
import { Link, useParams } from "react-router-dom";
import moment from "moment-timezone";
import { useLocation } from "react-router-dom";
import { css } from "@emotion/react";
import { FontSize } from "../../layout/globalStyle/globalSize";
import { useResetRecoilState, useSetRecoilState } from "recoil";
import { eventListState, EventProps } from "../../recoils/travel";
import {
  isopenModal,
  ModalContainer,
} from "../../layout/container/modalContainer";
import Color from "../../layout/globalStyle/globalColor";
import EventsDetail from "./eventsDetail";
import { EventCreate } from "./eventsModify";
import SignUpPage from "../authentication/signupPage";

const EventsSectionStyle = css`
  border: 2px solid white;
  display: flex;
  flex-direction: column;
  & > div {
    position: relative;
    display: flex;
    &:first-of-type {
      font-size: ${FontSize.fs18};
      padding: 10px 10px;
    }
    &:nth-of-type(2) {
      border-bottom: 2px solid white;
      & > span > a {
        color: white;
        font-weight: 600;
        &:hover {
          color: ${Color.gray05};
          cursor: pointer;
        }
      }
    }
    & > span {
      font-size: ${FontSize.fs12};
      margin: 2% 2%;
      text-align: right;
      // white-space: nowrap;
      padding-right: 10px;
      &:nth-of-type(-n + 3) {
        width: 30%;
      }
      &:nth-of-type(n + 4) {
        width: 20%;
      }
    }
  }
`;
const CreateBotton = css`
  opacity: 0.5;
  border: none;
  outline: none;
  width: 25px;
  height: 25px;
  border-radius: 25px;
  font-size: 2px;
  font-weight: bold;
  margin-top: 4%;
  margin-bottom: 4%;
  margin-left: 48%;
  transition-duration: 0.3s;
  &:hover {
    opacity: 0.7;
  }
  &:focus {
    border: none;
    outline: none;
  }
`;
function EventsSection(eventList: any[], travelId: number) {
  const setOpenEventModal = useSetRecoilState(isopenModal);
  const setEventList = useSetRecoilState(eventListState);

  function sortEvent(a: any, b: any, attri: string) {
    if (a[attri] > b[attri]) return 1;
    if (a[attri] < b[attri]) return -1;
    else return 0;
  }

  return (
    <div css={EventsSectionStyle}>
      <div>Event</div>
      <div>
        <div></div>
        <span>
          <a
            onClick={() => {
              setEventList(
                [...eventList].sort((a, b) => sortEvent(a, b, "name"))
              );
            }}
          >
            Name
          </a>
        </span>
        <span>
          <a
            onClick={() => {
              setEventList(
                [...eventList].sort((a, b) => sortEvent(b, a, "price"))
              );
            }}
          >
            Price
          </a>
        </span>
        <span>
          <a
            onClick={() => {
              setEventList(
                [...eventList].sort((a, b) => sortEvent(a, b, "payerName"))
              );
            }}
          >
            Payer
          </a>
        </span>
        <span>
          <a
            onClick={() => {
              setEventList(
                [...eventList].sort((a, b) => sortEvent(a, b, "date"))
              );
            }}
          >
            Date
          </a>
        </span>
      </div>
      {eventList.map((event, idx) =>
        EventsDetail(event, idx, travelId, eventList, setEventList)
      )}
      <button
        css={CreateBotton}
        onClick={() => {
          setOpenEventModal(true);
        }}
      >
        +
      </button>
      <ModalContainer checkleft={true}>
        <EventCreate></EventCreate>
      </ModalContainer>
    </div>
  );
}

export default EventsSection;
