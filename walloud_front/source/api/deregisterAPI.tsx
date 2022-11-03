import axios from "axios"

const DeregisterAPI = async (user_id: string) => {
    await axios
        .delete("/api/${user_id}/deregister")
        .then((response) => {
            console.log(response);
        })
        .catch((error) => {
            if (error.response.data.status === 500) {
                alert(error.response.data.message);
            }
        });
};

export default DeregisterAPI;