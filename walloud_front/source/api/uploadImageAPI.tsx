import axios from "axios";

const UploadImageAPI = async (userId: number, formData: FormData) => {
  return axios
    .post("api/s3/files/upload", { KEY: userId, VALUE: formData })
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

export default UploadImageAPI;
