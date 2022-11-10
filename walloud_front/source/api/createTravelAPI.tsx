import axios from "axios"
import { SetterOrUpdater } from "recoil";
import { TravelProps } from "../recoils/travel";
import { UserProps, userState } from "../recoils/user";

const CreateTravelAPI = async (userId: number, travelName: string,
  travelList: TravelProps[], setTravelList: SetterOrUpdater<TravelProps[]>) => {
    return axios.post(`/api/${userId}/createTravel`, null, { params: {
            travel_name: travelName
        }})
        .then((response) => {
          const newTravel : TravelProps = {
            travelId: response.data,
            name: travelName
          } 
          setTravelList([...travelList, newTravel])
          console.log(response);
        })
        .catch((error) => {
            if (error.response.data.status === 500) {
              alert(error.response.data.message);
            }
            else {
              alert("Check The network");
            }
        });
}

export default CreateTravelAPI;