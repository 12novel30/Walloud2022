import axios from "axios"
import { SetterOrUpdater } from "recoil";
import { PersonProps, TravelProps } from "../recoils/travel";

const SetPersonSettleAPI = async (personId: number, isSettled: boolean,
    personList: PersonProps[], setPersonList: SetterOrUpdater<PersonProps[]>) => {
    return axios.post(`/api/${personId}/setSettle`, null, { params: {
            isSettled: !isSettled
        }})
        .then((response) => {
            console.log(response) 
            setPersonList([...personList].map((p) =>
            p.personId === personId ? {...p, detail: {
                ...p.detail, isSettled: !isSettled
            }} : p
            ));
        })
        .catch((error) => {
            if (error.response.data.status === 500) {
              alert(error.response.data.message);
            }
            else {
              alert("준비되지 않은 API입니다");
            }
        });
}

export default SetPersonSettleAPI;