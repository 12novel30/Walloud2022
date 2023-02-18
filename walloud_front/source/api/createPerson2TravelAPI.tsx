import axios from "axios";

const CreatePerson2TravelAPI = async (travelId: number, email: string) => {
  console.log(email);
  return axios
    .post(`/api/${travelId}/createPerson2Travel`, null, {
      params: {
        email: email,
      },
      headers: {
        contentType: "application/json",
      },
    })
    .then((response) => {
      console.log(response);
    })
    .catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        console.log(error);
      }
    });
};

export default CreatePerson2TravelAPI;
