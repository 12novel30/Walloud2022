import axios from "axios"
import { SetterOrUpdater } from "recoil";

export interface TravelDetailAPIProps {
    userId: number;
    travelId: number;
    setEventList: SetterOrUpdater<any[]>;
    setPersonList: SetterOrUpdater<any[]>;
    setPeriod: React.Dispatch<React.SetStateAction<string>>;
}
  
const GetTravelDetailAPI = async ({userId, travelId, setEventList, setPersonList, setPeriod}: TravelDetailAPIProps) => {
    axios.get(`/api/${userId}/${travelId}`)
        .then((response) => {
            console.log(response.data)
            setEventList(response.data.eventList)
            setPersonList(response.data.personList)
            setPeriod(response.data.period)
        })
        .catch((error) => {
            console.log(error)
            alert("예기치 못한 오류가 발생했습니다")
        })
}

export default GetTravelDetailAPI;