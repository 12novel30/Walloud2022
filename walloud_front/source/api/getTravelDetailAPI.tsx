import axios from "axios"
import { SetterOrUpdater } from "recoil";
import { EventProps, PersonProps } from "../recoils/travel";

export interface TravelDetailAPIProps {
    userId: number;
    travelId: number;
    setEventList: SetterOrUpdater<EventProps[]>;
    setPersonList: SetterOrUpdater<PersonProps[]>;
    setPeriod: React.Dispatch<React.SetStateAction<string>>;
}
  
const GetTravelDetailAPI = async ({userId, travelId, setEventList, setPersonList, setPeriod}: TravelDetailAPIProps) => {
    axios.get(`/api/${userId}/${travelId}`)
        .then((response) => {
            console.log(response.data)
            setEventList(response.data.eventList.map(
                (event: object) => ({...event, isDetail: false, 
                    partiList: [{name: "default", chargedPrice: 0}]})))
            setPersonList(response.data.personList.map(
                (person: object) => ({...person, 
                    detail: {isView: false, sumGet: 0, sumSend: 0}})
            ))
            setPeriod(response.data.period)
        })
        .catch((error) => {
            console.log(error)
            alert("예기치 못한 오류가 발생했습니다")
        })
}

export default GetTravelDetailAPI;