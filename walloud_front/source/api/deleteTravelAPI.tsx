import axios from "axios"

const DeleteTravelAPI = async (travel_id: number) => {
    await axios
        .delete(`/api/${travel_id}/delete`)
        .then(() => {
            alert("여행이 삭제되었습니다");
        })
        .catch((error) => {
            if (error.response.data.status === 500) {
                alert(error.response.data.message);
            }
            console.log(error)
        });
};

export default DeleteTravelAPI;