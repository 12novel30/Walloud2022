import axios from "axios";
import { SetterOrUpdater, useRecoilState } from "recoil";
import { TravelProps } from "../recoils/travel";

const GetTravelImageAPI = async (userId: number, travelId: number) => {
  var image = "";
  axios
    .get(`/api/${travelId}/getTravelImage`)
    .then((response) => {
      image = String(response.data);
    })
    .catch((error) => {
      console.log(error);
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        alert("예기치 못한 오류가 발생했습니다");
      }
    });
  console.log(image);
  return image;
};

export default GetTravelImageAPI;
