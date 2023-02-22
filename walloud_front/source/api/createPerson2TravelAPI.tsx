import axios from "axios";
import { SetterOrUpdater } from "recoil";
import { PersonProps } from "../recoils/travel";
import GetPersonListToHomeView from "./getPersonListToHomeViewAPI";

const CreatePerson2TravelAPI = async (
  travelId: number,
  email: string,
  personList: PersonProps[],
  setPersonList: SetterOrUpdater<PersonProps[]>
) => {
  console.log(email);
  return axios
    .post(`/api/${travelId}/createPerson2Travel`, null, {
      params: {
        email: email,
      },
      headers: {
        contentType: "application/json",
      },
    })
    .then((response) => {
      console.log(response);
      //   GetPersonListToHomeView(travelId, personList, setPersonList);
      location.reload();
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
