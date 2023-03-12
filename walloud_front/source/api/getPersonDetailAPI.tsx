import axios from "axios";
import { SetterOrUpdater } from "recoil";
import { PersonProps } from "../recoils/travel";

const GetPersonDetailAPI = async (
  travelId: number,
  personId: number,
  personList: PersonProps[],
  setPersonList: SetterOrUpdater<PersonProps[]>
) => {
  axios
    .get(`/api/${travelId}/${personId}/getPersonDetailView`)
    .then((response) => {
      console.log(response.data);
      setPersonList(
        [...personList].map((p) =>
          p.personId === personId
            ? {
                ...p,
                detail: {
                  isView: !p.detail.isView,
                  isSettled: true,
                  sumGet: response.data.sumGet,
                  sumSend: response.data.sumSend,
                  userAccount: response.data.userAccount,
                  userBank: response.data.userBank,
                  eventList: response.data.eventList,
                },
              }
            : p
        )
      );
    })
    .catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        alert("예기치 못한 오류가 발생했습니다");
      }
    });
};

export default GetPersonDetailAPI;
