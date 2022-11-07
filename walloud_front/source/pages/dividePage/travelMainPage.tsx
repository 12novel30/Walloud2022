import { css } from '@emotion/react'
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { atom, useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil';
import GetTravelListAPI from '../../api/getTravelListAPI';
import UpdateTravelAPI from "../../api/updateTravelAPI";
import TravelBox from '../../component/box/travelBox';
import TravelCreateBox from '../../component/box/travelCreateBox';
import Color from '../../layout/globalStyle/globalColor';
import { ScreenSize } from '../../layout/globalStyle/globalSize';
import { currentTravelState, travelListState } from '../../recoils/travel';
import { userState } from '../../recoils/user';

const DivideMainPageStyle = css`
  background-color: ${Color.blue02};
  width: 100vw;
  height: 100vh;
  align-items: center;
  justify-content: center;
  display: flex;
  gap: 30px 20px;
  &:first-of-type {
    width: 20%;
  }       
`

function TravelMainPage(){
  const id = useRecoilValue(userState).id;
  const [travelList, setTravelList] = useRecoilState(travelListState);
  const [currentTravel, setCurrentTravel] = useRecoilState(currentTravelState);
  const [isEditMode, setIsEditMode] = useState<number|null>(null);

  const onClickEdit = (travelId: number) => {
    if (isEditMode !== travelId) {
      setIsEditMode(travelId);
    } else {
      const newName: string = (
        document.getElementById(`${travelId}-input-name`) as HTMLInputElement).value;
      UpdateTravelAPI(id, travelId, newName);
      setIsEditMode(null);

      const newTravelList = [...travelList].map((e) =>
        e.travelId === travelId ? { travelId: travelId, name: newName } : e
      );
      console.log(newTravelList);
      setTravelList(newTravelList);
    }
  };

  console.log(travelList)

  useEffect(() => {
    GetTravelListAPI(id, setTravelList);
  }, [])

  return (
      <div css = {DivideMainPageStyle}>
        {TravelCreateBox(travelList, setTravelList)}
        {travelList.map((travel, idx) => (
          TravelBox(travel.name, 
            travel.travelId, 
            setCurrentTravel, 
            onClickEdit, 
            isEditMode)
        ))}
      </div>
  )  
}
export default TravelMainPage;

/*
        <div>
          <h3>Travel</h3>
          <div>여행에서 소비를 어떻게 관리할까요?</div>
        </div>
*/
