import axios from "axios";
import { SetterOrUpdater } from "recoil";
import { EventProps } from "../recoils/travel";

const CreateEventAPI = async (
  travelId: number,
  newEvent: EventProps,
  payerId: number,
  partiList: { spent: number; role: boolean; personId: number }[]
) => {
  const data = JSON.stringify({
    // need to fix with EventController's API
    event_name: newEvent.eventName,
    date: newEvent.date,
    price: newEvent.price,
    payer_person_id: payerId, // Long type (not in EventProps)
    parti_list: partiList, // need to fix "attri; spent, role, personId"
  })

  return axios
    .post(`/api/${travelId}/createEvent`, data, {
      headers: {
        "Content-Type": "application/json", // application/json 타입 선언
      },
    })
    .then((response) => {
      console.log(response);
      window.location.reload();
    })
    .catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        console.log(error);
      }
    });
};

export default CreateEventAPI;
