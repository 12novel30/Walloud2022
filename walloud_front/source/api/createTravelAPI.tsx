import axios from "axios";
import { SetterOrUpdater, useRecoilState } from "recoil";
import { TravelProps } from "../recoils/travel";
import { UserProps, userState } from "../recoils/user";

const CreateTravelAPI = async (
  userId: number,
  travelName: string,
  travelList: TravelProps[],
  setTravelList: SetterOrUpdater<TravelProps[]>
) => {
  return axios
    .post(`/api/${userId}/createNewTravelUserJoining`, travelName, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((response) => {
      const newTravel: TravelProps = {
        travelId: response.data,
        name: travelName,
        isSuper: true,
      };
      setTravelList([...travelList, newTravel]);
      console.log(response);
    })
    .catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        alert("Check The network");
      }
    });
};

export default CreateTravelAPI;
