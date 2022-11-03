import { css } from '@emotion/react'
import { Dispatch, SetStateAction, useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useRecoilState, useRecoilValue } from 'recoil';
import GetTravelDetailAPI from '../../api/getTravelDetailAPI';
import Color from '../../layout/globalStyle/globalColor';
import { currentTravelState, eventListState, personListState } from '../../recoils/travel';
import { UserProps, userState } from '../../recoils/user';
import EventsSection from './eventsSection';
import PersonSection from './personSection';

const DivideMainPageStyle = css`
  font-size: 40px;
  background-color: ${Color.blueClassic};
  width: 100vw;
  height: 150vh;
  align-items: center;
  justify-content: center;
  display: flex;
  color: white;
  gap: 5vw;
  &>div {
    width: 40vw;
    min-width: 400px;
    border: 2px solid white;
    border-radius: 10px;
    transition: ease 1s;
  }
`;

function TravelPage(){
    const {travelName} = useParams();
    const userId = useRecoilValue(userState).id;
    const travelId = useRecoilValue(currentTravelState);
    const [personList, setPersonList] = useRecoilState(personListState);
    const [eventList, setEventList] = useRecoilState(eventListState);
    const [period, setPeriod] = useState("");

    useEffect(() => {
      GetTravelDetailAPI({userId, travelId, setPersonList, setEventList, setPeriod});
    }, [])

    return (
        <div css = {DivideMainPageStyle}>
          {/*ViewSection(period, travelName)*/}
          {EventsSection(eventList)}
          {/*TravelDetailSection(Map, Calendar, etc.)*/}
          {PersonSection(personList, travelId)}
        </div>
  )
}
export default TravelPage;

// moment.tz(event.date, "Asia/Seoul").format().substring(5, 10)