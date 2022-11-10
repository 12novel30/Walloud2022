import axios from "axios";
import { SetterOrUpdater, useRecoilValue } from "recoil";
import { TravelProps } from "../recoils/travel";

const DeleteTravelAPI = async (
  travel_id: number,
  travelList: TravelProps[],
  setTravelList: SetterOrUpdater<TravelProps[]>
) => {
  await axios
    .delete(`/api/${travel_id}/delete`)
    .then(() => {
      setTravelList(
        [...travelList].filter((travel) => travel.travelId !== travel_id)
      );
    })
    .catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      }
      console.log(error);
    });
};

export default DeleteTravelAPI;
