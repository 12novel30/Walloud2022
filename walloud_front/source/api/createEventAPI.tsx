import axios from "axios";
import { SetterOrUpdater } from "recoil";
import { EventProps } from "../recoils/travel";

const CreateEventAPI = async (
  travelId: number,
  newEvent: EventProps,
  payerId: number,
  partiList: { spent: number; role: boolean; personId: number }[]
) => {
  return axios
    .post(`/api/${travelId}/CreateEvent`, null, {
      params: {
        // need to fix with EventController's API
        event_name: newEvent.name,
        event_date: newEvent.date,
        price: newEvent.price,
        payer_person_id: payerId, // Long type (not in EventProps)
        parti_list: partiList, // need to fix "attri; spent, role, personId"
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

export default CreateEventAPI;
