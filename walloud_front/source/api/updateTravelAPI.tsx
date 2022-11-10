import axios from "axios";

const UpdateTravelAPI = async (
  userId: number,
  travelId: number,
  travelName: string
) => {
  return axios
    .put(`/api/${userId}/${travelId}/updateTravelInfo`, {
      travel_name: travelName,
    })
    .then((response) => {
      console.log(response);
    })
    .catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        alert("Check The network");
      }
    });
};

export default UpdateTravelAPI;