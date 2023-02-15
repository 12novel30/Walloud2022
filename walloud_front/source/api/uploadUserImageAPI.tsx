import axios from "axios";

const UploadUserImageAPI = async (userId: number, formData: FormData) => {
  return axios
    .put(`/api/${userId}/updateUserImage`, formData, {
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

export default UploadUserImageAPI;
