import axios from "axios";
import { SetterOrUpdater } from "recoil";

const CreatePerson2TravelAPI = async (travelId: number, email: string) => {
  return axios
    .post(`/api/${travelId}/createPerson2Travel`, null, {
      params: {
        email: email,
      },
    })
    .then((response) => {
      console.log(response);
    })
    .catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        console.log(error);
      }
    });
};

export default CreatePerson2TravelAPI;
