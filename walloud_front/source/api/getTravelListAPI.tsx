import axios from "axios"
import { SetterOrUpdater, useRecoilState } from "recoil";
import { TravelProps } from "../recoils/travel";

const GetTravelListAPI = async (userId: number, setTravelList: SetterOrUpdater<TravelProps[]>) => {
    axios.get(`/api/${userId}`)
        .then((response) => {
            setTravelList(response.data)
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

export default GetTravelListAPI;
