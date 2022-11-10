import axios from "axios"
import { SetterOrUpdater } from "recoil";
import { EventProps } from "../recoils/travel";

const DeleteEventAPI = async (travel_id: number, event_id: number,
    eventList: EventProps[], setEventList: SetterOrUpdater<EventProps[]>) => {
    await axios
        .delete(`/api/100/${travel_id}/${event_id}/deleteEvent`)
        .then(() => {
            alert("이벤트가 삭제되었습니다");
            setEventList([...eventList].filter(
                e => e.eventId != event_id
            ))
        })
        .catch((error) => {
            if (error.response.data.status === 500) {
                alert(error.response.data.message);
            }
            console.log(error)
        });
};

export default DeleteEventAPI;