import axios from "axios"
import { SetterOrUpdater } from "recoil";
import { EventProps } from "../recoils/travel";

const GetEventPartiAPI = async (id: number, 
    eventList: EventProps[], setEventList: SetterOrUpdater<EventProps[]>) => {
    axios.get(`/api/${id}/getPartiListInEvent`)
        .then((response) => {
            console.log(response.data)
            setEventList([...eventList].map((e) =>
            e.eventId === id ? {...e, isDetail: !e.isDetail, partiList: response.data} : e
            ));
        })
        .catch((error) => {
            if (error.response.data.status === 500) {
                alert(error.response.data.message)
            }
            else {
                alert("예기치 못한 오류가 발생했습니다")
            }
        })
}

export default GetEventPartiAPI;