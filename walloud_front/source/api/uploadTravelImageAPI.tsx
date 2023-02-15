import axios from "axios";

const UploadTravelImageAPI = async (
  userId: number,
  travelId: number,
  formData: FormData
) => {
  return axios
    .put(`/api/${userId}/${travelId}/updateTravelImage`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        alert("Check The network");
      }
    });
};

export default UploadTravelImageAPI;
