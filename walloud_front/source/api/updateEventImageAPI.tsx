import axios from "axios";

const UpdateEventImageAPI = async (eventId: number, formData: FormData) => {
  return axios.put(`/api/${eventId}/updateEventImage`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

export default UpdateEventImageAPI;
