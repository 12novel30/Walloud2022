import axios from "axios";

const UploadImageAPI = async (
  userId: number,
  travelId: number,
  formData: FormData
) => {
  return axios
    .put(`localhost:8080/api/${userId}/${travelId}/updateTravelImage`, {
      key: "file",
      value: formData,
    })
    .then((response) => {
      console.log("failed:", response);
    })
    .catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        alert("Check The network");
      }
    });
};

export default UploadImageAPI;
