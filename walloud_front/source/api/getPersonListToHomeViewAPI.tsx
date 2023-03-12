import axios from "axios";
import { PersonProps } from "../recoils/travel";
import { SetterOrUpdater } from "recoil";

const GetPersonListToHomeView = async (
  travelId: number,
  personList: PersonProps[],
  setPersonList: SetterOrUpdater<PersonProps[]>
) => {
  axios.get(`/api/${travelId}/getPersonListToHomeView`).then((response) => {
    console.log(response);
    setPersonList(
      response.data.map((person: object) => ({
        ...person,
        detail: { isView: false, sumGet: 0, sumSend: 0, isSettled: false },
      }))
    );
  });
};

export default GetPersonListToHomeView;
