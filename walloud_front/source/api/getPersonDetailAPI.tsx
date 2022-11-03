import axios from "axios"
import { NonIndexRouteObject } from "react-router-dom";
import { SetterOrUpdater, useRecoilState } from "recoil";
import { TravelProps } from "../recoils/travel";

const GetPersonDetailAPI = async (travelId: number, personId: number) => {
    axios.get(`/api/${travelId}/${personId}/personDetail`)
        .then((response) => {
            console.log(response.data)
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

export default GetPersonDetailAPI;