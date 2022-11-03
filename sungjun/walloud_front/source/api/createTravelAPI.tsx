import axios from "axios"
import { NavigateFunction } from "react-router-dom";
import { SetterOrUpdater} from "recoil";
import { UserProps, userState } from "../recoils/user";

const CreateTravelAPI = async (userId: number, travelName: string) => {
    return axios.post(`/api/${userId}/createTravel`, null, { params: {
            travel_name: travelName
        }})
        .then((response) => {
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